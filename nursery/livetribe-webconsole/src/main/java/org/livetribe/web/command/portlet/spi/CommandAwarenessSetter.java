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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.ResourceBundle;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.livetribe.web.command.Command;
import org.livetribe.web.command.portlet.PortletModesAware;
import org.livetribe.web.command.portlet.PortletProcessingAware;
import org.livetribe.web.command.portlet.PortletRenderingAware;
import org.livetribe.web.command.portlet.PortletStatesAware;
import org.livetribe.web.command.portlet.PortletSessionAware;
import org.livetribe.web.command.portlet.PortletResourceBundleAware;
import org.livetribe.web.command.spi.AbstractCommandPerformer;
import org.livetribe.web.command.spi.CommandContext;

/**
 * $Rev:28 $
 */
public class CommandAwarenessSetter extends AbstractCommandPerformer
{
    public CommandAwarenessSetter(Map configuration)
    {
        super(configuration);
    }

    public void perform(CommandContext context)
    {
        PortletCommandContext portletCommandContext = (PortletCommandContext)context;

        Command command = portletCommandContext.getCommand();
        PortletRequest portletRequest = portletCommandContext.getPortletRequest();

        if (command instanceof PortletProcessingAware && portletRequest instanceof ActionRequest)
        {
            ((PortletProcessingAware)command).setActionRequest((ActionRequest)portletRequest);
            ((PortletProcessingAware)command).setActionResponse((ActionResponse)portletCommandContext.getPortletResponse());
        }

        if (command instanceof PortletRenderingAware && portletRequest instanceof RenderRequest)
        {
            ((PortletRenderingAware)command).setRenderRequest((RenderRequest)portletRequest);
            ((PortletRenderingAware)command).setRenderResponse((RenderResponse)portletCommandContext.getPortletResponse());
        }

        if (command instanceof PortletSessionAware)
        {
            ((PortletSessionAware)command).setPortletSession(portletRequest.getPortletSession());
        }

        if (command instanceof PortletResourceBundleAware)
        {
            ResourceBundle bundle = portletCommandContext.getPortletConfig().getResourceBundle(portletRequest.getLocale());
            ((PortletResourceBundleAware)command).setResourceBundle(bundle);
        }

        if (command instanceof PortletModesAware)
        {
            PortletConfig config = portletCommandContext.getPortletConfig();
            String supportedModes = config.getInitParameter("supportedPortletModes");
            if (supportedModes != null)
            {
                String[] modes = supportedModes.split(",", -1);
                ((PortletModesAware)command).setPortletModes(new HashSet(Arrays.asList(modes)));
            }
        }

        if (command instanceof PortletStatesAware)
        {
            PortletConfig config = portletCommandContext.getPortletConfig();
            String supportedStates = config.getInitParameter("supportedWindowStates");
            if (supportedStates != null)
            {
                String[] states = supportedStates.split(",", -1);
                ((PortletStatesAware)command).setPortletStates(new HashSet(Arrays.asList(states)));
            }
        }
    }
}
