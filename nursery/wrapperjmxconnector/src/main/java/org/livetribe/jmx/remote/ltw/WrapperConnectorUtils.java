/*
 * Copyright 2005 the original author or authors
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
package org.livetribe.jmx.remote.ltw;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.management.remote.JMXServiceURL;

/**
 * $Rev$
 */
public class WrapperConnectorUtils
{
    private WrapperConnectorUtils()
    {
    }

    public static JMXServiceURL unwrap(JMXServiceURL jmxServiceURL) throws MalformedURLException
    {
        String configResource = parseConfigurationResource(jmxServiceURL);

        String originalPath = jmxServiceURL.getURLPath();
        if (originalPath.startsWith("/")) originalPath = originalPath.substring(1);
        int index = originalPath.indexOf(configResource);

        String wrappedPath = originalPath.substring(index + configResource.length() + 1);
        if (wrappedPath.length() == 0) throw new MalformedURLException("Missing wrapped connector URL in JMXServiceURL: " + jmxServiceURL);

        return new JMXServiceURL("service:jmx:" + wrappedPath);
    }

    public static JMXServiceURL wrap(JMXServiceURL wrapped, JMXServiceURL unwrapped) throws MalformedURLException
    {
        StringBuilder builder = new StringBuilder();
        builder.append("service:jmx:");
        builder.append(wrapped.getProtocol()).append(":///");
        String config = parseConfigurationResource(wrapped);
        if (config.length() > 0) builder.append(config);
        builder.append("/");
        String unwrappedString = unwrapped.toString();
        builder.append(unwrappedString.substring("service:jmx:".length()));
        return new JMXServiceURL(builder.toString());
    }

    public static List getForwarders(JMXServiceURL jmxServiceURL, Map environment, ClassLoader classLoader) throws IOException
    {
        String config = parseConfigurationResource(jmxServiceURL);
        InputStream configStream = null;
        if (config.length() > 0)
        {
            URL configURL = classLoader.getResource(config);
            if (configURL != null) configStream = configURL.openStream();
        }
        if (configStream == null) configStream = getConfigurationStream(environment);
        if (configStream == null) throw new FileNotFoundException("Could not find wrappers configuration");

        return parseConfigurationStream(configStream);
    }

    private static String parseConfigurationResource(JMXServiceURL jmxServiceURL) throws MalformedURLException
    {
        String urlPath = jmxServiceURL.getURLPath();
        if (urlPath == null || urlPath.length() == 0) throw new MalformedURLException("Missing path in JMXServiceURL " + jmxServiceURL);

        String path = urlPath;
        if (urlPath.startsWith("/")) path = urlPath.substring(1);
        int endOfConfig = path.indexOf("/");
        if (endOfConfig < 0) throw new MalformedURLException("Incomplete path '" + urlPath + "' in JMXServiceURL: " + jmxServiceURL);

        return path.substring(0, endOfConfig).trim();
    }

    private static InputStream getConfigurationStream(Map environment) throws IOException
    {
        Object configObject = environment.get(WrapperConnectorServer.CONFIG_RESOURCE_ATTRIBUTE);
        if (configObject instanceof URL)
        {
            URL configURL = (URL)configObject;
            return configURL.openStream();
        }
        else if (configObject instanceof String)
        {
            String configString = (String)configObject;
            return new ByteArrayInputStream(configString.getBytes("UTF-8"));
        }
        else if (configObject instanceof InputStream)
        {
            return (InputStream)configObject;
        }
        return null;
    }

    private static List parseConfigurationStream(InputStream configStream)
    {
        List result = new ArrayList();
        result.add(new I18NServerForwarder());
        return result;
    }


    public static List getClientForwarders(JMXServiceURL jmxServiceURL, Map environment, ClassLoader classLoader)
    {
        List result = new ArrayList();
        result.add(new I18NClientForwarder());
        return result;
    }
}
