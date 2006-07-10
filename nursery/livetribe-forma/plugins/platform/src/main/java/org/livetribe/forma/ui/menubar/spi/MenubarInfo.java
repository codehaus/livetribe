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
public class MenubarInfo
{
    private String menubarId;
    private final List<MenuInfo> menuInfos = new ArrayList<MenuInfo>();

    public String getMenubarId()
    {
        return menubarId;
    }

    public void setMenubarId(String menubarId)
    {
        this.menubarId = menubarId;
    }

    public void addMenuInfo(MenuInfo menuInfo)
    {
        if (!menuInfos.contains(menuInfo)) menuInfos.add(menuInfo);
    }

    public List<MenuInfo> getMenuInfos()
    {
        return menuInfos;
    }

    public void merge(MenubarInfo other)
    {
        Set<MenuInfo> toBeMerged = new HashSet<MenuInfo>();
        for (MenuInfo thisMenuInfo : getMenuInfos())
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
}
