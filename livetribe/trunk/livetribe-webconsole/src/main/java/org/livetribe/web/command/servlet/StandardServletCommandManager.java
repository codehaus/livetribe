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
package org.livetribe.web.command.servlet;

import java.util.Map;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.livetribe.web.command.servlet.spi.CommandCreator;
import org.livetribe.web.command.servlet.spi.CommandDependencySetter;
import org.livetribe.web.command.servlet.spi.CommandDispatcher;
import org.livetribe.web.command.servlet.spi.CommandParametersSetter;
import org.livetribe.web.command.servlet.spi.ServletCommandContext;
import org.livetribe.web.command.servlet.spi.CommandAwarenessSetter;
import org.livetribe.web.command.spi.CommandExecutor;
import org.livetribe.web.command.spi.CommandPerformer;
import org.livetribe.web.command.spi.CommandPropertiesSetter;

/**
 * $Rev: 51 $
 */
public class StandardServletCommandManager implements ServletCommandManager
{
    private Map configuration;

    public void setConfiguration(Map configuration)
    {
        this.configuration = configuration;
    }

    public void execute(Servlet servlet, ServletConfig config, HttpServletRequest request, HttpServletResponse response)
    {
        ServletCommandContext context = createContext(servlet, config, request, response);
        prepare(context);
        execute(context);
        dispatch(context);
    }

    private ServletCommandContext createContext(Servlet servlet, ServletConfig config, HttpServletRequest request, HttpServletResponse response)
    {
        ServletCommandContext context = new ServletCommandContext();
        context.setServlet(servlet);
        context.setServletConfig(config);
        context.setServletRequest(request);
        context.setServletResponse(response);
        return context;
    }

    private void prepare(ServletCommandContext context)
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

    private void execute(ServletCommandContext context)
    {
        CommandPerformer performer = new CommandExecutor(configuration);
        performer.perform(context);
    }

    public void dispatch(ServletCommandContext context)
    {
        CommandPerformer performer = new CommandDispatcher(configuration);
        performer.perform(context);
    }
}
