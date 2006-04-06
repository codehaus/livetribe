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
package org.livetribe.jmx.remote.provider.ltw;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorProvider;
import javax.management.remote.JMXServiceURL;

import org.livetribe.jmx.remote.ltw.WrapperConnector;

/**
 * @version $Revision$ $Date$
 */
public class ClientProvider implements JMXConnectorProvider
{
    public JMXConnector newJMXConnector(JMXServiceURL serviceURL, Map environment) throws IOException
    {
        String protocol = serviceURL.getProtocol();
        if (!"ltw".equals(protocol))
            throw new MalformedURLException("Wrong protocol " + protocol + " for provider " + this);

        return new WrapperConnector(serviceURL, environment);
    }
}