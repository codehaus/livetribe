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
package org.livetribe.web.command.portlet.spi;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.PortletException;

import org.livetribe.web.command.spi.AbstractRequestDispatcher;
import org.livetribe.web.command.spi.CommandContext;
import org.livetribe.web.command.spi.CommandException;

/**
 * $Rev:28 $
 */
public class CommandDispatcher extends AbstractRequestDispatcher
{
    private static final String RENDER_PARAM_PREFIX = ".param.";
    private static final String REDIRECT_PREFIX = "redirect:";

    public CommandDispatcher(Map configuration)
    {
        super(configuration);
    }

    public void perform(CommandContext context)
    {
        setRenderParameters(context);
        super.perform(context);
    }

    protected String getResultDispatch(CommandContext context, String preferredResult)
    {
        PortletCommandContext portletCommandContext = (PortletCommandContext)context;
        StringBuffer key = new StringBuffer();
        String commandAlias = portletCommandContext.getCommandAlias();
        key.append(commandAlias).append(".");
        PortletRequest portletRequest = portletCommandContext.getPortletRequest();
        String mode = portletRequest.getPortletMode().toString().toLowerCase();
        key.append(mode).append(".");
        String window = portletRequest.getWindowState().toString().toLowerCase();
        key.append(window).append(COMMAND_RESULT_PREFIX);
        String commandResult = preferredResult == null ? portletCommandContext.getCommandResult() : preferredResult;
        key.append(commandResult);

        Map config = getConfiguration();
        String dispatch = (String)config.get(key.toString());
        if (dispatch == null)
        {
            if (!(portletRequest instanceof ActionRequest))
            {
                throw new CommandException("Could not find result " + commandResult + " for command " + context.getCommandAlias() + ", portlet mode " + mode + ", window state " + window);
            }
        }

        return dispatch;
    }

    protected void dispatch(CommandContext context, String dispatch)
    {
        PortletCommandContext portletCommandContext = (PortletCommandContext)context;
        PortletResponse portletResponse = portletCommandContext.getPortletResponse();

        if (dispatch.startsWith(REDIRECT_PREFIX))
        {
            dispatch = dispatch.substring(REDIRECT_PREFIX.length());
            if (portletResponse instanceof ActionResponse)
            {
                try
                {
                    ((ActionResponse)portletResponse).sendRedirect(dispatch);
                }
                catch (IOException x)
                {
                    throw new CommandException(x);
                }
            }
            else
            {
                throw new CommandException("Could not redirect during rendering to: " + dispatch);
            }
        }
        else
        {
            if (portletResponse instanceof RenderResponse)
            {
                PortletRequestDispatcher dispatcher = portletCommandContext.getPortletConfig().getPortletContext().getRequestDispatcher(dispatch);
                if (dispatcher == null) throw new CommandException("Could not dispatch to " + dispatch + " for command " + portletCommandContext.getCommandAlias());

                portletCommandContext.getPortletRequest().setAttribute("model", portletCommandContext.getCommand());

                try
                {
                    dispatcher.include((RenderRequest)portletCommandContext.getPortletRequest(), (RenderResponse)portletResponse);
                }
                catch (PortletException x)
                {
                    throw new CommandException(x);
                }
                catch (IOException x)
                {
                    throw new CommandException(x);
                }
            }
            else
            {
                throw new CommandException("Could not include during action processing to: " + dispatch);
            }
        }
    }

    protected String resolveImplicitVariable(CommandContext context, String variableName)
    {
        if ("portletName".equals(variableName))
        {
            return ((PortletCommandContext)context).getPortletConfig().getPortletName();
        }
        if ("contextPath".equals(variableName))
        {
            return ((PortletCommandContext)context).getPortletRequest().getContextPath();
        }
        return null;
    }

    private void setRenderParameters(CommandContext context)
    {
        PortletCommandContext portletCommandContext = (PortletCommandContext)context;
        PortletRequest portletRequest = portletCommandContext.getPortletRequest();
        if (portletRequest instanceof ActionRequest)
        {
            ActionResponse actionResponse = (ActionResponse)portletCommandContext.getPortletResponse();

            StringBuffer key = new StringBuffer();
            key.append(portletCommandContext.getCommandAlias());
            key.append(COMMAND_RESULT_PREFIX);
            key.append(portletCommandContext.getCommandResult());
            key.append(RENDER_PARAM_PREFIX);
            String keyString = key.toString();
            Map config = getConfiguration();
            for (Iterator keys = config.entrySet().iterator(); keys.hasNext();)
            {
                Map.Entry entry = (Map.Entry)keys.next();
                String k = (String)entry.getKey();
                if (k.startsWith(keyString))
                {
                    String paramName = k.substring(keyString.length());
                    Object paramValue = entry.getValue();
                    if (paramValue instanceof String)
                    {
                        String paramValueString = resolveVariables(context, (String)paramValue);
                        actionResponse.setRenderParameter(paramName, paramValueString);
                    }
                }
            }
        }
    }
}
