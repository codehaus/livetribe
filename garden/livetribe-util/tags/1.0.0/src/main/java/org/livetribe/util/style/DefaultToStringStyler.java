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

import org.livetribe.util.ClassUtils;


/**
 * Spring's default <code>toString()</code> styler.
 * <p/>
 * <p>This class is used by {@link ToStringCreator} to style <code>toString()</code>
 * output in a consistent manner according to Spring conventions.
 *
 * @author Keith Donald
 * @author Juergen Hoeller
 * @version $Revision$ $Date$
 */
public class DefaultToStringStyler implements ToStringStyler
{
    private final ValueStyler valueStyler;

    /**
     * Create a new DefaultToStringStyler.
     *
     * @param valueStyler the ValueStyler to use
     */
    public DefaultToStringStyler(ValueStyler valueStyler)
    {
        assert valueStyler != null;
        this.valueStyler = valueStyler;
    }

    /**
     * Return the ValueStyler used by this ToStringStyler.
     */
    protected final ValueStyler getValueStyler()
    {
        return this.valueStyler;
    }


    public void styleStart(StringBuffer buffer, Object obj)
    {
        if (!obj.getClass().isArray())
        {
            buffer.append('[').append(ClassUtils.getShortName(obj.getClass()));
            styleIdentityHashCode(buffer, obj);
        }
        else
        {
            buffer.append('[');
            styleIdentityHashCode(buffer, obj);
            buffer.append(' ');
            styleValue(buffer, obj);
        }
    }

    private void styleIdentityHashCode(StringBuffer buffer, Object obj)
    {
        buffer.append('@');
        buffer.append(Integer.toHexString(System.identityHashCode(obj)));
    }

    public void styleEnd(StringBuffer buffer, Object o)
    {
        buffer.append(']');
    }

    public void styleField(StringBuffer buffer, String fieldName, Object value)
    {
        styleFieldStart(buffer, fieldName);
        styleValue(buffer, value);
        styleFieldEnd(buffer, fieldName);
    }

    protected void styleFieldStart(StringBuffer buffer, String fieldName)
    {
        buffer.append(' ').append(fieldName).append(" = ");
    }

    protected void styleFieldEnd(StringBuffer buffer, String fieldName)
    {
    }

    public void styleValue(StringBuffer buffer, Object value)
    {
        buffer.append(this.valueStyler.style(value));
    }

    public void styleFieldSeparator(StringBuffer buffer)
    {
        buffer.append(',');
    }
}