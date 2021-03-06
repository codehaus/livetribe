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
package org.livetribe.web.command.servlet.spi;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.livetribe.web.command.spi.AbstractParametersSetter;
import org.livetribe.web.command.spi.AttributeContainer;
import org.livetribe.web.command.spi.CommandContext;

/**
 * $Rev: 51 $
 */
public class CommandParametersSetter extends AbstractParametersSetter
{
    public CommandParametersSetter(Map configuration)
    {
        super(configuration);
    }

    public void perform(CommandContext context)
    {
        HttpServletRequest servletRequest = ((ServletCommandContext)context).getServletRequest();
        AttributeContainer attributes = new ServletParametersContainer(servletRequest);
        setParameters(context, attributes);
    }
}
