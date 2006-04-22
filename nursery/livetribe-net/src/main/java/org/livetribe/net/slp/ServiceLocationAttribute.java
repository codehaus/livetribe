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

import java.io.Serializable;
import java.util.Vector;


/**
 * The ServiceLocationAttribute class models SLP attributes. Instances
 * of this class are returned by Locator.findAttributes() and are
 * communicated along with register/deregister requests.
 *
 * @version $Revision: 1.2 $ $Date: 2005/03/05 00:59:49 $
 */
public class ServiceLocationAttribute implements Serializable
{
    /**
     * Construct a service location attribute. Errors vector result in an
     * IllegalArgumentException.
     *
     * @param id     The attribute name. The String can consist of any Unicode
     *               character.
     * @param values A Vector of one or more attribute values. Vector contents
     *               must be uniform in type and one of Integer, String,
     *               Boolean, or byte[].  If the attribute is a keyword
     *               attribute, then the parameter should be null. String
     *               values can consist of any Unicode character.
     * @throws IllegalArgumentException if vector is in error.
     */
    public ServiceLocationAttribute( String id, Vector values )
    {

    }
}
