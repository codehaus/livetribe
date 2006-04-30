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
package org.livetribe.util.logging;

import java.util.logging.LogManager;
import java.io.InputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @version $Rev$ $Date$
 */
public class LoggingManager
{
    private final String DEFAULT_CONFIG_FILE = "logging.properties";

    private String configurationResource = DEFAULT_CONFIG_FILE;

    public void setConfigurationResource(String configurationResource)
    {
        this.configurationResource = configurationResource;
    }

    public String getConfigurationResource()
    {
        return configurationResource;
    }

    public void start() throws IOException
    {
        String resource = getConfigurationResource();
        InputStream stream = getClass().getClassLoader().getResourceAsStream(resource);
        if (stream == null) throw new FileNotFoundException(resource + " not found in classpath");
        LogManager logManager = LogManager.getLogManager();
        logManager.readConfiguration(stream);
    }

    public void stop()
    {
    }
}
