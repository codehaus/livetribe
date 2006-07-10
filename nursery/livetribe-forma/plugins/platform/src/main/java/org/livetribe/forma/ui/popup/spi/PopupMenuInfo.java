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

/**
 * @version $Rev$ $Date$
 */
public class PopupMenuInfo
{
    private String popupMenuId;
    private String popupMenuText;
    private String popupMenuMnemonic;
    private final List<PopupSectionInfo> popupSectionInfos = new ArrayList<PopupSectionInfo>();

    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        final PopupMenuInfo that = (PopupMenuInfo)obj;
        return popupMenuId.equals(that.popupMenuId);
    }

    public int hashCode()
    {
        return popupMenuId.hashCode();
    }

    public String getPopupMenuId()
    {
        return popupMenuId;
    }

    public void setPopupMenuId(String popupMenuId)
    {
        this.popupMenuId = popupMenuId;
    }

    public String getPopupMenuText()
    {
        return popupMenuText;
    }

    public void setPopupMenuText(String popupMenuText)
    {
        this.popupMenuText = popupMenuText;
    }

    public String getPopupMenuMnemonic()
    {
        return popupMenuMnemonic;
    }

    public void setPopupMenuMnemonic(String popupMenuMnemonic)
    {
        this.popupMenuMnemonic = popupMenuMnemonic;
    }

    public void addPopupSectionInfo(PopupSectionInfo popupSectionInfo)
    {
        if (!popupSectionInfos.contains(popupSectionInfo)) popupSectionInfos.add(popupSectionInfo);
    }

    public List<PopupSectionInfo> getPopupSectionInfos()
    {
        return popupSectionInfos;
    }

    public void merge(PopupMenuInfo other)
    {
        Set<PopupSectionInfo> toBeMerged = new HashSet<PopupSectionInfo>();
        for (PopupSectionInfo thisPopupSectionInfo : getPopupSectionInfos())
        {
            String thisPopupSectionId = thisPopupSectionInfo.getPopupSectionId();
            for (PopupSectionInfo otherPopupSectionInfo : other.getPopupSectionInfos())
            {
                String otherPopupSectionId = otherPopupSectionInfo.getPopupSectionId();
                if (thisPopupSectionId.equals(otherPopupSectionId))
                    thisPopupSectionInfo.merge(otherPopupSectionInfo);
                else
                    toBeMerged.add(otherPopupSectionInfo);
            }
        }
        for (PopupSectionInfo menuSectionInfo : toBeMerged) addPopupSectionInfo(menuSectionInfo);
    }
}
