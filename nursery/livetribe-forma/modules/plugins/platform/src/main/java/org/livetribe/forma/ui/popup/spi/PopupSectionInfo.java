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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.livetribe.forma.ui.popup.PopupException;

/**
 * @version $Rev$ $Date$
 */
public class PopupSectionInfo
{
    private String popupSectionId;
    private final List<PopupMenuInfo> popupMenuInfos = new ArrayList<PopupMenuInfo>();
    private final List<PopupItemInfo> popupItemInfos = new ArrayList<PopupItemInfo>();

    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        final PopupSectionInfo that = (PopupSectionInfo)obj;
        return popupSectionId.equals(that.popupSectionId);
    }

    public int hashCode()
    {
        return popupSectionId.hashCode();
    }

    public String getPopupSectionId()
    {
        return popupSectionId;
    }

    public void setPopupSectionId(String popupSectionId)
    {
        this.popupSectionId = popupSectionId;
    }

    public void addPopupMenuInfo(PopupMenuInfo menuInfo)
    {
        if (!popupMenuInfos.contains(menuInfo)) popupMenuInfos.add(menuInfo);
    }

    public List<PopupMenuInfo> getPopupMenuInfos()
    {
        return popupMenuInfos;
    }

    public void addPopupItemInfo(PopupItemInfo itemInfo)
    {
        if (popupItemInfos.contains(itemInfo)) throw new PopupException("Duplicate menu item id " + itemInfo.getPopupMenuItemId());
        popupItemInfos.add(itemInfo);
    }

    public List<PopupItemInfo> getPopupItemInfos()
    {
        return popupItemInfos;
    }

    public void merge(PopupSectionInfo other)
    {
        List<PopupMenuInfo> thisPopupMenuInfos = getPopupMenuInfos();
        if (thisPopupMenuInfos.isEmpty())
        {
            for (PopupMenuInfo otherPopupMenuInfo : other.getPopupMenuInfos()) addPopupMenuInfo(otherPopupMenuInfo);
        }
        else
        {
            Set<PopupMenuInfo> toBeMerged = new HashSet<PopupMenuInfo>();
            for (PopupMenuInfo thisPopupMenuInfo : getPopupMenuInfos())
            {
                String thisPopupMenuId = thisPopupMenuInfo.getPopupMenuId();
                for (PopupMenuInfo otherPopupMenuInfo : other.getPopupMenuInfos())
                {
                    String otherPopupMenuId = otherPopupMenuInfo.getPopupMenuId();
                    if (thisPopupMenuId.equals(otherPopupMenuId))
                        thisPopupMenuInfo.merge(otherPopupMenuInfo);
                    else
                        toBeMerged.add(otherPopupMenuInfo);
                }
            }
            for (PopupMenuInfo menuInfo : toBeMerged) addPopupMenuInfo(menuInfo);
        }

        for (PopupItemInfo menuItemInfo : other.getPopupItemInfos()) addPopupItemInfo(menuItemInfo);
    }
}
