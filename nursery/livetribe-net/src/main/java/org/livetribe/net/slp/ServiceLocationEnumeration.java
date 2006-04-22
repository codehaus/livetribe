/**
 *
 * Copyright 2005 (C) The original author or authors
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
package org.livetribe.net.slp;

import java.util.Enumeration;


/**
 * The ServiceLocationEnumeration class is the return type for all
 * Locator SLP operations. The Java API library may implement this
 * class to block until results are available from the SLP operation, so
 * that the client can achieve asynchronous operation by retrieving
 * results from the enumeration in a separate thread. Clients use the
 * superclass nextElement() method if they are unconcerned with SLP
 * exceptions.
 *
 * @version $Revision: 1.1 $ $Date: 2005/01/26 04:05:03 $
 */
public interface ServiceLocationEnumeration extends Enumeration
{
    /**
     * @return the next value or block until it becomes available.
     * @throws ServiceLocationException if the SLP operation encounters an error.
     * @throws java.util.NoSuchElementException
     *                                  if no more elements exist.
     */
    public Object next() throws ServiceLocationException;
}
