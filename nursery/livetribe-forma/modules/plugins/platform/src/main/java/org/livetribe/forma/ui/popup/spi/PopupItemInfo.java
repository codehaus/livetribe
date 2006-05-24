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
package org.livetribe.forma.ui.popup.spi;

/**
 * @version $Rev$ $Date$
 */
public class PopupItemInfo
{
    private String popupMenuItemId;
    private String actionId;

    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        final PopupItemInfo that = (PopupItemInfo)obj;
        return popupMenuItemId.equals(that.popupMenuItemId);
    }

    public int hashCode()
    {
        return popupMenuItemId.hashCode();
    }

    public String getPopupMenuItemId()
    {
        return popupMenuItemId;
    }

    public void setPopupMenuItemId(String popupMenuItemId)
    {
        this.popupMenuItemId = popupMenuItemId;
    }

    public String getActionId()
    {
        return actionId;
    }

    public void setActionId(String actionId)
    {
        this.actionId = actionId;
    }
}
