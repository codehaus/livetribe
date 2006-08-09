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

import java.util.Map;

import org.livetribe.web.command.Command;

/**
 * $Rev: 51 $
 */
public abstract class AbstractCommandCreator extends AbstractCommandPerformer
{
    protected AbstractCommandCreator(Map configuration)
    {
        super(configuration);
    }

    protected void createCommand(CommandContext context, AttributeContainer parameters)
    {
        String commandAlias = (String)parameters.getAttribute(CommandContext.COMMAND_REQUEST_PARAM_KEY);
        if (commandAlias == null) commandAlias = context.getCommandAlias();
        if (commandAlias == null) throw new CommandException("Could not find command in request parameters: " + parameters);

        Map config = getConfiguration();
        String commandClassName = (String)config.get(commandAlias);
        if (commandClassName == null)
        {
            if (config.containsKey(commandAlias))
                throw new CommandException("Could not find command class for alias: " + commandAlias);
            else
                throw new CommandException("Could not find command: " + commandAlias);
        }

        try
        {
            Class commandClass = Thread.currentThread().getContextClassLoader().loadClass(commandClassName);
            Command result = (Command)commandClass.newInstance();
            context.setCommand(result);
            context.setCommandAlias(commandAlias);
        }
        catch (Exception x)
        {
            throw new CommandException(x);
        }
    }
}
