/**
 *
 * Copyright 2008 (C) The original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.livetribe.util.style;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.livetribe.util.ClassUtils;


/**
 * Converts objects to String form, generally for debugging purposes,
 * using Spring's <code>toString</code> styling conventions.
 * <p/>
 * <p>Uses the reflective visitor pattern underneath the hood to nicely
 * encapsulate styling algorithms for each type of styled object.
 *
 * @author Keith Donald
 * @author Juergen Hoeller
 * @version $Revision$ $Date$
 */
public class DefaultValueStyler implements ValueStyler
{

    private static final String EMPTY = "[empty]";
    private static final String NULL = "[null]";
    private static final String COLLECTION = "collection";
    private static final String SET = "set";
    private static final String LIST = "list";
    private static final String MAP = "map";
    private static final String ARRAY = "array";


    public String style(Object value)
    {
        if (value == null)
        {
            return NULL;
        }
        else if (value instanceof String)
        {
            return "\'" + value + "\'";
        }
        else if (value instanceof Class)
        {
            return ClassUtils.getShortName((Class) value);
        }
        else if (value instanceof Method)
        {
            Method method = (Method) value;
            return method.getName() + "@" + ClassUtils.getShortName(method.getDeclaringClass());
        }
        else if (value instanceof Map)
        {
            return style((Map) value);
        }
        else if (value instanceof Map.Entry)
        {
            return style((Map.Entry) value);
        }
        else if (value instanceof Collection)
        {
            return style((Collection) value);
        }
        else if (value.getClass().isArray())
        {
            return styleArray(toObjectArray(value));
        }
        else
        {
            return String.valueOf(value);
        }
    }

    /**
     * Convert the given array (which may be a primitive array) to an
     * object array (if necessary of primitive wrapper objects).
     * <p>A <code>null</code> source value will be converted to an
     * empty Object array.
     *
     * @param source the (potentially primitive) array
     * @return the corresponding object array (never <code>null</code>)
     * @throws IllegalArgumentException if the parameter is not an array
     */
    public static Object[] toObjectArray(Object source)
    {
        if (source instanceof Object[])
        {
            return (Object[]) source;
        }
        if (source == null)
        {
            return new Object[0];
        }
        if (!source.getClass().isArray())
        {
            throw new IllegalArgumentException("Source is not an array: " + source);
        }
        int length = Array.getLength(source);
        if (length == 0)
        {
            return new Object[0];
        }
        Class wrapperType = Array.get(source, 0).getClass();
        Object[] newArray = (Object[]) Array.newInstance(wrapperType, length);
        for (int i = 0; i < length; i++)
        {
            newArray[i] = Array.get(source, i);
        }
        return newArray;
    }

    private String style(Map value)
    {
        StringBuffer buffer = new StringBuffer(value.size() * 8 + 16);
        buffer.append(MAP + "[");
        for (Iterator it = value.entrySet().iterator(); it.hasNext();)
        {
            Map.Entry entry = (Map.Entry) it.next();
            buffer.append(style(entry));
            if (it.hasNext())
            {
                buffer.append(',').append(' ');
            }
        }
        if (value.isEmpty())
        {
            buffer.append(EMPTY);
        }
        buffer.append("]");
        return buffer.toString();
    }

    private String style(Map.Entry value)
    {
        return style(value.getKey()) + " -> " + style(value.getValue());
    }

    private String style(Collection value)
    {
        StringBuffer buffer = new StringBuffer(value.size() * 8 + 16);
        buffer.append(getCollectionTypeString(value)).append('[');
        for (Iterator i = value.iterator(); i.hasNext();)
        {
            buffer.append(style(i.next()));
            if (i.hasNext())
            {
                buffer.append(',').append(' ');
            }
        }
        if (value.isEmpty())
        {
            buffer.append(EMPTY);
        }
        buffer.append("]");
        return buffer.toString();
    }

    private String getCollectionTypeString(Collection value)
    {
        if (value instanceof List)
        {
            return LIST;
        }
        else if (value instanceof Set)
        {
            return SET;
        }
        else
        {
            return COLLECTION;
        }
    }

    private String styleArray(Object[] array)
    {
        StringBuffer buffer = new StringBuffer(array.length * 8 + 16);
        buffer.append(ARRAY + "<" + ClassUtils.getShortName(array.getClass().getComponentType()) + ">[");
        for (int i = 0; i < array.length - 1; i++)
        {
            buffer.append(style(array[i]));
            buffer.append(',').append(' ');
        }
        if (array.length > 0)
        {
            buffer.append(style(array[array.length - 1]));
        }
        else
        {
            buffer.append(EMPTY);
        }
        buffer.append("]");
        return buffer.toString();
    }

}