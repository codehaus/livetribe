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

import org.livetribe.web.command.spi.AbstractCommandPerformer;
import org.livetribe.web.command.spi.CommandContext;
import org.livetribe.web.command.Command;
import org.livetribe.web.command.servlet.ServletAware;
import org.livetribe.web.command.servlet.ServletSessionAware;

/**
 * $Rev: 51 $
 */
public class CommandAwarenessSetter extends AbstractCommandPerformer
{
    public CommandAwarenessSetter(Map configuration)
    {
        super(configuration);
    }

    public void perform(CommandContext context)
    {
        ServletCommandContext servletCommandContext = (ServletCommandContext)context;
        Command command = servletCommandContext.getCommand();
        if (command instanceof ServletAware)
        {
            ((ServletAware)command).setServletRequest(servletCommandContext.getServletRequest());
            ((ServletAware)command).setServletResponse(servletCommandContext.getServletResponse());
        }

        if (command instanceof ServletSessionAware)
        {
            ((ServletSessionAware)command).setServletSession(servletCommandContext.getServletRequest().getSession());
        }
    }
}
