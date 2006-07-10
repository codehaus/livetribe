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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.livetribe.forma.ui.menubar.MenubarException;

/**
 * @version $Rev$ $Date$
 */
public class MenuSectionInfo
{
    private String menuSectionId;
    private final List<MenuInfo> menuInfos = new ArrayList<MenuInfo>();
    private final List<MenuItemInfo> menuItemInfos = new ArrayList<MenuItemInfo>();

    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        final MenuSectionInfo that = (MenuSectionInfo)obj;
        return menuSectionId.equals(that.menuSectionId);
    }

    public int hashCode()
    {
        return menuSectionId.hashCode();
    }

    public String getMenuSectionId()
    {
        return menuSectionId;
    }

    public void setMenuSectionId(String menuSectionId)
    {
        this.menuSectionId = menuSectionId;
    }

    public void addMenuInfo(MenuInfo menuInfo)
    {
        if (!menuInfos.contains(menuInfo)) menuInfos.add(menuInfo);
    }

    public void addMenuItemInfo(MenuItemInfo menuItemInfo)
    {
        if (menuItemInfos.contains(menuItemInfo)) throw new MenubarException("Duplicate menu item id " + menuItemInfo.getMenuItemId());
        menuItemInfos.add(menuItemInfo);
    }

    public List<MenuInfo> getMenuInfos()
    {
        return menuInfos;
    }

    public List<MenuItemInfo> getMenuItemInfos()
    {
        return menuItemInfos;
    }

    public void merge(MenuSectionInfo other)
    {
        List<MenuInfo> thisMenuInfos = getMenuInfos();
        if (thisMenuInfos.isEmpty())
        {
            for (MenuInfo otherMenuInfo : other.getMenuInfos()) addMenuInfo(otherMenuInfo);
        }
        else
        {
            Set<MenuInfo> toBeMerged = new HashSet<MenuInfo>();
            for (MenuInfo thisMenuInfo : thisMenuInfos)
            {
                String thisMenuId = thisMenuInfo.getMenuId();
                for (MenuInfo otherMenuInfo : other.getMenuInfos())
                {
                    String otherMenuId = otherMenuInfo.getMenuId();
                    if (thisMenuId.equals(otherMenuId))
                        thisMenuInfo.merge(otherMenuInfo);
                    else
                        toBeMerged.add(otherMenuInfo);
                }
            }
            for (MenuInfo menuInfo : toBeMerged) addMenuInfo(menuInfo);
        }

        for (MenuItemInfo menuItemInfo : other.getMenuItemInfos()) addMenuItemInfo(menuItemInfo);
    }
}
