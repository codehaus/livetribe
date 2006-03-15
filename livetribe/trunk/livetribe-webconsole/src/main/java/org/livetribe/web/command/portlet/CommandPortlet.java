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

import java.io.IOException;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletMode;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * $Rev: 51 $
 */
public class CommandPortlet extends GenericPortlet
{
    protected Log logger = LogFactory.getLog(getClass());

    public void processAction(ActionRequest actionRequest, ActionResponse actionResponse) throws PortletException, IOException
    {
        PortletCommandManager portletCommandManager = PortletCommandManagerFactory.newCommandManager();
        portletCommandManager.execute(this, getPortletConfig(), actionRequest, actionResponse, null);
    }

    /**
     * Override if we want to support more portlet modes other than edit / view / help.
     * Default behavior is to call doEdit() / doView() (if not minimized) / doHelp()
     */
    protected void doDispatch(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException, IOException
    {
        PortletMode mode = renderRequest.getPortletMode();
        if (PortletMode.VIEW.equals(mode))
        {
            doView(renderRequest, renderResponse);
        }
        else if (PortletMode.EDIT.equals(mode))
        {
            doEdit(renderRequest, renderResponse);
        }
        else if (PortletMode.HELP.equals(mode))
        {
            doHelp(renderRequest, renderResponse);
        }
        else
        {
            throw new PortletException("Unsupported Portlet Mode: " + mode);
        }
    }

    protected void doView(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException, IOException
    {
        PortletCommandManager portletCommandManager = PortletCommandManagerFactory.newCommandManager();
        portletCommandManager.execute(this, getPortletConfig(), renderRequest, renderResponse, "show" + getPortletName().toLowerCase());
    }

    protected void doEdit(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException, IOException
    {
        PortletCommandManager portletCommandManager = PortletCommandManagerFactory.newCommandManager();
        portletCommandManager.execute(this, getPortletConfig(), renderRequest, renderResponse, "edit" + getPortletName().toLowerCase());
    }

    protected void doHelp(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException, IOException
    {
        PortletCommandManager portletCommandManager = PortletCommandManagerFactory.newCommandManager();
        portletCommandManager.execute(this, getPortletConfig(), renderRequest, renderResponse, "help" + getPortletName().toLowerCase());
    }
}
