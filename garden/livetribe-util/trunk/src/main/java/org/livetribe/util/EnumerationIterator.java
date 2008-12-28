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
package org.livetribe.util;

import java.util.Enumeration;
import java.util.Iterator;

/**
 * A handy adapter which is useful for using old collections in foreach loops.
 * <p/>
 * Brazenly stolen from http://java.sun.com/developer/JDCTechTips/2003/tt1021.html
 *
 * @version $Revision$ $Date$
 */
public class EnumerationIterator
{
    public static <T> Iterator<T> iterator(final Enumeration<T> enumeration)
    {
        return new Iterator<T>()
        {
            public boolean hasNext()
            {
                return enumeration.hasMoreElements();
            }

            public T next()
            {
                return enumeration.nextElement();
            }

            public void remove()
            {
                throw new UnsupportedOperationException();
            }
        };
    }

    public static <T> Iterable<T> iterable(final Enumeration<T> enumeration)
    {
        return new Iterable<T>()
        {
            public Iterator<T> iterator()
            {
                return new Iterator<T>()
                {
                    public boolean hasNext()
                    {
                        return enumeration.hasMoreElements();
                    }

                    public T next()
                    {
                        return enumeration.nextElement();
                    }

                    public void remove()
                    {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }

    private EnumerationIterator() { }
}