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

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.livetribe.web.command.spi.CommandContext;

/**
 * $Rev: 51 $
 */
public class ServletCommandContext extends CommandContext
{
    private Servlet servlet;
    private ServletConfig servletConfig;
    private HttpServletRequest servletRequest;
    private HttpServletResponse servletResponse;

    public Servlet getServlet()
    {
        return servlet;
    }

    public void setServlet(Servlet servlet)
    {
        this.servlet = servlet;
    }

    public ServletConfig getServletConfig()
    {
        return servletConfig;
    }

    public void setServletConfig(ServletConfig servletConfig)
    {
        this.servletConfig = servletConfig;
    }

    public HttpServletRequest getServletRequest()
    {
        return servletRequest;
    }

    public void setServletRequest(HttpServletRequest servletRequest)
    {
        this.servletRequest = servletRequest;
    }

    public HttpServletResponse getServletResponse()
    {
        return servletResponse;
    }

    public void setServletResponse(HttpServletResponse servletResponse)
    {
        this.servletResponse = servletResponse;
    }
}
