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

import java.util.Enumeration;
import java.util.Map;

/**
 * $Rev: 51 $
 */
public abstract class AbstractParametersSetter extends AbstractCommandPerformer
{
    protected AbstractParametersSetter(Map configuration)
    {
        super(configuration);
    }

    protected void setParameters(CommandContext context, AttributeContainer parameters)
    {
        for (Enumeration params = parameters.enumeration(); params.hasMoreElements();)
        {
            String paramName = (String)params.nextElement();
            if (!CommandContext.COMMAND_REQUEST_PARAM_KEY.equals(paramName))
            {
                Object[] paramValues = parameters.getAttributeValues(paramName);
                convertAndSetProperty(context.getCommand(), paramName, paramValues);
            }
        }
    }
}
