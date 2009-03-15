/**
 *
 * Copyright 2008-2009 (C) The original author or authors
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
package org.livetribe.boot.protocol;

import java.io.InputStream;


/**
 * @version $Revision$ $Date$
 */
public interface ContentProvider
{
    /**
     * Obtain a specific version of content with a given name.  This name
     * and version were obtained from a provision directive.
     *
     * @param name    the name of the content
     * @param version the version of the content
     * @return an input stream of the content's contents
     * @throws BootException if there is a problem obtaining the content
     */
    InputStream pleaseProvide(String name, long version) throws BootException;
}
