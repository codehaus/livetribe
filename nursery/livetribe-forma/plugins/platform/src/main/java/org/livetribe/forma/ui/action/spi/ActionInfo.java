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
package org.livetribe.forma.ui.action.spi;

import org.livetribe.forma.AbstractInfo;

/**
 * @version $Rev$ $Date$
 */
public class ActionInfo extends AbstractInfo
{
    private String actionId;
    private String actionClassName;
    private String actionText;
    private String actionTooltip;
    private String actionIconPath;
    private String actionMnemonic;
    private String actionAccelerator;

    public String getActionId()
    {
        return actionId;
    }

    public void setActionId(String actionId)
    {
        this.actionId = actionId;
    }

    public String getActionClassName()
    {
        return actionClassName;
    }

    public void setActionClassName(String actionClassName)
    {
        this.actionClassName = actionClassName;
    }

    public String getActionText()
    {
        return actionText;
    }

    public void setActionText(String actionText)
    {
        this.actionText = actionText;
    }

    public String getActionTooltip()
    {
        return actionTooltip;
    }

    public void setActionTooltip(String actionTooltip)
    {
        this.actionTooltip = actionTooltip;
    }

    public String getActionIconPath()
    {
        return actionIconPath;
    }

    public void setActionIconPath(String actionIconPath)
    {
        this.actionIconPath = actionIconPath;
    }

    public String getActionMnemonic()
    {
        return actionMnemonic;
    }

    public void setActionMnemonic(String actionMnemonic)
    {
        this.actionMnemonic = actionMnemonic;
    }

    public String getActionAccelerator()
    {
        return actionAccelerator;
    }

    public void setActionAccelerator(String actionAccelerator)
    {
        this.actionAccelerator = actionAccelerator;
    }
}
