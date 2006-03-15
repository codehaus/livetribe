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
import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;

import org.livetribe.web.command.spi.AbstractRequestDispatcher;
import org.livetribe.web.command.spi.CommandContext;
import org.livetribe.web.command.spi.CommandException;

/**
 * $Rev: 51 $
 */
public class CommandDispatcher extends AbstractRequestDispatcher
{
    private static final String REDIRECT_PREFIX = "redirect:";

    public CommandDispatcher(Map configuration)
    {
        super(configuration);
    }

    protected String getResultDispatch(CommandContext context, String preferredResult)
    {
        ServletCommandContext servletCommandContext = (ServletCommandContext)context;
        StringBuffer key = new StringBuffer();
        String commandAlias = servletCommandContext.getCommandAlias();
        key.append(commandAlias).append(COMMAND_RESULT_PREFIX);
        String commandResult = preferredResult == null ? servletCommandContext.getCommandResult() : preferredResult;
        key.append(commandResult);

        Map config = getConfiguration();
        String dispatch = (String)config.get(key.toString());
        if (dispatch == null) throw new CommandException("Could not find result " + commandResult + " for command " + context.getCommandAlias());

        return dispatch;
    }

    protected void dispatch(CommandContext context, String dispatch)
    {
        ServletCommandContext servletCommandContext = (ServletCommandContext)context;

        if (dispatch.startsWith(REDIRECT_PREFIX))
        {
            dispatch = dispatch.substring(REDIRECT_PREFIX.length());
            try
            {
                servletCommandContext.getServletResponse().sendRedirect(dispatch);
            }
            catch (IOException x)
            {
                throw new CommandException(x);
            }
        }
        else
        {
            RequestDispatcher dispatcher = servletCommandContext.getServletConfig().getServletContext().getRequestDispatcher(dispatch);
            if (dispatcher == null) throw new CommandException("Could not dispatch to " + dispatch + " for command " + servletCommandContext.getCommandAlias());

            servletCommandContext.getServletRequest().setAttribute("model", servletCommandContext.getCommand());

            try
            {
                dispatcher.include(servletCommandContext.getServletRequest(), servletCommandContext.getServletResponse());
            }
            catch (ServletException x)
            {
                throw new CommandException(x);
            }
            catch (IOException x)
            {
                throw new CommandException(x);
            }
        }
    }

    protected String resolveImplicitVariable(CommandContext context, String variableName)
    {
        if ("contextPath".equals(variableName))
        {
            return ((ServletCommandContext)context).getServletRequest().getContextPath();
        }
        return null;
    }
}
