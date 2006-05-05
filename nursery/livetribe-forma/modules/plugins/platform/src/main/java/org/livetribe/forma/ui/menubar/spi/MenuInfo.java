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
package org.livetribe.forma.ui.menubar.spi;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

/**
 * @version $Rev$ $Date$
 */
public class MenuInfo
{
    private String menuId;
    private String menuText;
    private String menuMnemonic;
    private final List<MenuSectionInfo> menuSectionInfos = new ArrayList<MenuSectionInfo>();

    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        final MenuInfo menuInfo = (MenuInfo)obj;
        return menuId.equals(menuInfo.menuId);
    }

    public int hashCode()
    {
        return menuId.hashCode();
    }

    public String getMenuId()
    {
        return menuId;
    }

    public void setMenuId(String menuId)
    {
        this.menuId = menuId;
    }

    public String getMenuText()
    {
        return menuText;
    }

    public void setMenuText(String menuText)
    {
        this.menuText = menuText;
    }

    public String getMenuMnemonic()
    {
        return menuMnemonic;
    }

    public void setMenuMnemonic(String menuMnemonic)
    {
        this.menuMnemonic = menuMnemonic;
    }

    public void addMenuSectionInfo(MenuSectionInfo sectionInfo)
    {
        if (!menuSectionInfos.contains(sectionInfo)) menuSectionInfos.add(sectionInfo);
    }

    public List<MenuSectionInfo> getMenuSectionInfos()
    {
        return menuSectionInfos;
    }

    public void merge(MenuInfo other)
    {
        Set<MenuSectionInfo> toBeMerged = new HashSet<MenuSectionInfo>();
        for (MenuSectionInfo thisMenuSectionInfo : getMenuSectionInfos())
        {
            String thisMenuSectionId = thisMenuSectionInfo.getMenuSectionId();
            for (MenuSectionInfo otherMenuSectionInfo : other.getMenuSectionInfos())
            {
                String otherMenuSectionId = otherMenuSectionInfo.getMenuSectionId();
                if (thisMenuSectionId.equals(otherMenuSectionId))
                    thisMenuSectionInfo.merge(otherMenuSectionInfo);
                else
                    toBeMerged.add(otherMenuSectionInfo);
            }
        }
        for (MenuSectionInfo menuSectionInfo : toBeMerged) addMenuSectionInfo(menuSectionInfo);
    }
}
