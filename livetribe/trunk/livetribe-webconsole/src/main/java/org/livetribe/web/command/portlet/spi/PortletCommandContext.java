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

import javax.portlet.Portlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import org.livetribe.web.command.spi.CommandContext;

/**
 * $Rev: 51 $
 */
public class PortletCommandContext extends CommandContext
{
    private Portlet portlet;
    private PortletConfig config;
    private PortletRequest request;
    private PortletResponse response;

    public Portlet getPortlet()
    {
        return portlet;
    }

    public void setPortlet(Portlet portlet)
    {
        this.portlet = portlet;
    }

    public PortletConfig getPortletConfig()
    {
        return config;
    }

    public void setPortletConfig(PortletConfig config)
    {
        this.config = config;
    }

    public PortletRequest getPortletRequest()
    {
        return request;
    }

    public void setPortletRequest(PortletRequest request)
    {
        this.request = request;
    }

    public PortletResponse getPortletResponse()
    {
        return response;
    }

    public void setPortletResponse(PortletResponse response)
    {
        this.response = response;
    }
}
