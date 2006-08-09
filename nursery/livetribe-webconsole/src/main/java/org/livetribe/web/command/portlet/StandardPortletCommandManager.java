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
package org.livetribe.web.command.portlet;

import java.util.Map;
import javax.portlet.Portlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import org.livetribe.web.command.portlet.spi.CommandCreator;
import org.livetribe.web.command.portlet.spi.CommandDependencySetter;
import org.livetribe.web.command.portlet.spi.CommandDispatcher;
import org.livetribe.web.command.portlet.spi.CommandParametersSetter;
import org.livetribe.web.command.portlet.spi.PortletCommandContext;
import org.livetribe.web.command.portlet.spi.CommandAwarenessSetter;
import org.livetribe.web.command.spi.CommandExecutor;
import org.livetribe.web.command.spi.CommandPerformer;
import org.livetribe.web.command.spi.CommandPropertiesSetter;

/**
 * $Rev: 51 $
 */
public class StandardPortletCommandManager implements PortletCommandManager
{
    private Map configuration;

    public void setConfiguration(Map configuration)
    {
        this.configuration = configuration;
    }

    public void execute(Portlet portlet, PortletConfig config, PortletRequest request, PortletResponse response, String commandAlias)
    {
        PortletCommandContext context = createContext(portlet, config, request, response, commandAlias);
        prepare(context);
        execute(context);
        dispatch(context);
    }

    private PortletCommandContext createContext(Portlet portlet, PortletConfig config, PortletRequest request, PortletResponse response, String commandAlias)
    {
        PortletCommandContext context = new PortletCommandContext();
        context.setPortlet(portlet);
        context.setPortletConfig(config);
        context.setPortletRequest(request);
        context.setPortletResponse(response);
        if (commandAlias != null) context.setCommandAlias(commandAlias);
        return context;
    }

    private void prepare(PortletCommandContext context)
    {
        CommandPerformer performer = new CommandCreator(configuration);
        performer.perform(context);
        performer = new CommandAwarenessSetter(configuration);
        performer.perform(context);
        performer = new CommandDependencySetter(configuration);
        performer.perform(context);
        performer = new CommandPropertiesSetter(configuration);
        performer.perform(context);
        performer = new CommandParametersSetter(configuration);
        performer.perform(context);
    }

    private void execute(PortletCommandContext context)
    {
        CommandPerformer performer = new CommandExecutor(configuration);
        performer.perform(context);
    }

    public void dispatch(PortletCommandContext context)
    {
        CommandPerformer performer = new CommandDispatcher(configuration);
        performer.perform(context);
    }
}
