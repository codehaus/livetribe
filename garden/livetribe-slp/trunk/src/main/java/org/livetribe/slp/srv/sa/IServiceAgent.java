/*
 * Copyright 2007 the original author or authors
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
package org.livetribe.slp.srv.sa;

import org.livetribe.slp.Attributes;
import org.livetribe.slp.ServiceInfo;
import org.livetribe.slp.ServiceLocationException;
import org.livetribe.slp.ServiceURL;

/**
 * @version $Revision$ $Date$
 */
public interface IServiceAgent
{
    public void register(ServiceInfo service) throws ServiceLocationException;

    public void addAttributes(ServiceURL serviceURL, String language, Attributes attributes) throws ServiceLocationException;

    public void removeAttributes(ServiceURL serviceURL, String language, Attributes attributes) throws ServiceLocationException;

    public void deregister(ServiceURL serviceURL, String language) throws ServiceLocationException;
}
