/*
 * Copyright 2006 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.livetribe.web.command.spi;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import org.livetribe.web.command.Command;

/**
 * $Rev: 51 $
 */
public abstract class AbstractCommandPerformer implements CommandPerformer
{
    private final Map configuration;

    protected AbstractCommandPerformer(Map configuration)
    {
        this.configuration = configuration;
    }

    protected Map getConfiguration()
    {
        return configuration;
    }

    private void setProperty(Method setter, Command command, Object value)
    {
        try
        {
            setter.invoke(command, new Object[]{value});
        }
        catch (IllegalArgumentException x)
        {
            throw new CommandException(x);
        }
        catch (IllegalAccessException x)
        {
            throw new CommandException(x);
        }
        catch (InvocationTargetException x)
        {
            throw new CommandException(x.getCause());
        }
    }

    protected void convertAndSetProperty(Command command, String propertyName, Object propertyValue)
    {
        Class commandClass = command.getClass();
        PropertyDescriptor property = findPropertyDescriptor(command, propertyName, null);
        if (property == null)
            throw new CommandException("Could not find property " + propertyName + " in command class " + commandClass.getName());

        Method setter = property.getWriteMethod();
        if (setter == null)
            throw new CommandException("Property " + propertyName + " is not writable in command class " + commandClass.getName());

        Class propertyType = property.getPropertyType();

        Object value = convertValue(propertyValue, propertyType);

        setProperty(setter, command, value);
    }

    private Object convertValue(Object propertyValue, Class propertyType)
    {
        if (propertyValue == null) return null;
        // Same type, no conversion needed
        if (propertyType.isAssignableFrom(propertyValue.getClass())) return propertyValue;

        if (propertyValue.getClass().isArray())
        {
            int length = Array.getLength(propertyValue);
            if (propertyType.isArray())
            {
                // Here converting from source[] to target[]; we loop to convert source[i] to target[i]

                Object[] values = new Object[length];
                for (int i = 0; i < length; ++i)
                {
                    Object element = Array.get(propertyValue, i);
                    values[i] = convertValue(element, propertyType.getComponentType());
                }
                return values;
            }
            else
            {
                // Here converting from source[] to target; conversion is possible only if source.length == 1
                // so that we convert source[0] to target.

                if (length != 1) throw new CommandException("Could not convert " + propertyValue + " into " + propertyType.getName());

                Object element = Array.get(propertyValue, 0);
                return convertValue(element, propertyType);
            }
        }
        else
        {
            // Here we convert to source to target; since we use PropertyEditors, source must be String;
            // the PropertyEditor will do the right conversion

            if (!(propertyValue instanceof String)) throw new CommandException("Could not convert " + propertyValue + " into " + propertyType.getName());

            // TODO: improve the lookup of the property editor, see property.getPropertyEditorClass() and property.createPropertyEditor(Object)
            PropertyEditor propertyEditor = PropertyEditorManager.findEditor(propertyType);
            if (propertyEditor == null) throw new CommandException("Could not convert " + propertyValue + " into " + propertyType.getName());

            propertyEditor.setAsText((String)propertyValue);
            return propertyEditor.getValue();
        }
    }

    private BeanInfo getBeanInfo(Class commandClass)
    {
        try
        {
            return Introspector.getBeanInfo(commandClass, Object.class);
        }
        catch (IntrospectionException x)
        {
            throw new CommandException(x);
        }
    }

    protected Object getPropertyAndConvert(Command command, String propertyName, Class propertyClass)
    {
        Class commandClass = command.getClass();
        PropertyDescriptor property = findPropertyDescriptor(command, propertyName, null);
        if (property == null)
            throw new CommandException("Could not find property " + propertyName + " in command class " + commandClass.getName());

        Method getter = property.getReadMethod();
        if (getter == null)
            throw new CommandException("Property " + propertyName + " is not readable in command class " + commandClass.getName());

        Object unconverted = getProperty(getter, command);

        return convertValue(unconverted, propertyClass);
    }

    private Object getProperty(Method method, Command command)
    {
        try
        {
            return method.invoke(command, (Object[])null);
        }
        catch (IllegalArgumentException x)
        {
            throw new CommandException(x);
        }
        catch (IllegalAccessException x)
        {
            throw new CommandException(x);
        }
        catch (InvocationTargetException x)
        {
            throw new CommandException(x.getCause());
        }
    }

    protected boolean hasProperty(Command command, String propertyName)
    {
        return findPropertyDescriptor(command, propertyName, null) != null;
    }

    private PropertyDescriptor findPropertyDescriptor(Command command, String propertyName, Class propertyClass)
    {
        Class commandClass = command.getClass();
        BeanInfo info = getBeanInfo(commandClass);
        PropertyDescriptor[] properties = info.getPropertyDescriptors();
        for (int i = 0; i < properties.length; ++i)
        {
            PropertyDescriptor property = properties[i];
            if (propertyName.equals(property.getName()))
            {
                if (propertyClass == null) return property;
                if (property.getPropertyType().isAssignableFrom(propertyClass)) return property;
            }
        }
        return null;
    }
}
