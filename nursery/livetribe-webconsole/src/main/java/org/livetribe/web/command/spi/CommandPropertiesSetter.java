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

import java.util.Iterator;
import java.util.Map;

/**
 * $Rev: 51 $
 */
public class CommandPropertiesSetter extends AbstractCommandPerformer
{
    private static final String PROPERTY_PREFIX = ".property.";

    public CommandPropertiesSetter(Map configuration)
    {
        super(configuration);
    }

    public void perform(CommandContext context)
    {
        String commandAlias = context.getCommandAlias();
        Map config = getConfiguration();
        for (Iterator configEntries = config.entrySet().iterator(); configEntries.hasNext();)
        {
            Map.Entry configEntry = (Map.Entry)configEntries.next();
            String configKey = (String)configEntry.getKey();
            if (configKey.startsWith(commandAlias))
            {
                String key = configKey.substring(commandAlias.length());
                if (key.startsWith(PROPERTY_PREFIX))
                {
                    String propertyName = key.substring(PROPERTY_PREFIX.length());
                    Object propertyValue = configEntry.getValue();
                    convertAndSetProperty(context.getCommand(), propertyName, propertyValue);
                }
            }
        }
    }
}
