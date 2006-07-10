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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.livetribe.forma.ui.action.Action;
import org.livetribe.forma.ui.action.ActionManager;
import org.livetribe.forma.ui.menubar.MenubarContainer;
import org.livetribe.forma.ui.menubar.MenubarException;
import org.livetribe.forma.ui.menubar.MenubarManager;
import org.livetribe.ioc.Inject;

/**
 * @version $Rev$ $Date$
 */
public class DefaultMenubarManager implements MenubarManager
{
    @Inject
    private ActionManager actionManager;
    private final Map<String, MenubarInfo> menubarInfos = new HashMap<String, MenubarInfo>();

    public void spiAddMenubarInfo(MenubarInfo menubarInfo)
    {
        String menubarId = menubarInfo.getMenubarId();
        MenubarInfo existing = menubarInfos.get(menubarId);
        if (existing == null)
            menubarInfos.put(menubarId, menubarInfo);
        else
            existing.merge(menubarInfo);
    }

    public void installMenuBar(MenubarContainer frame, String menubarId)
    {
        MenubarInfo menubarInfo = menubarInfos.get(menubarId);
        if (menubarInfo == null)
            throw new MenubarException("No menubar with id " + menubarId + " is registered in the menubar extensions");

        JMenuBar result = new JMenuBar();
        result.setName(menubarId);

        List<MenuInfo> menuInfos = menubarInfo.getMenuInfos();
        List<JMenu> menus = newMenus(menuInfos);
        for (JMenu menu : menus) result.add(menu);

        frame.setJMenuBar(result);
    }

    private List<JMenu> newMenus(List<MenuInfo> menuInfos)
    {
        List<JMenu> result = new ArrayList<JMenu>();
        for (MenuInfo menuInfo : menuInfos)
        {
            JMenu menu = new JMenu();
            menu.setName(menuInfo.getMenuId());
            menu.setText(menuInfo.getMenuText());
            String mnemonic = menuInfo.getMenuMnemonic();
            if (mnemonic != null && mnemonic.length() > 0) menu.setMnemonic(mnemonic.charAt(0));
            result.add(menu);

            List<MenuSectionInfo> sectionInfos = menuInfo.getMenuSectionInfos();
            for (int i = 0; i < sectionInfos.size(); ++i)
            {
                MenuSectionInfo sectionInfo = sectionInfos.get(i);

                List<MenuItemInfo> itemInfos = sectionInfo.getMenuItemInfos();
                for (MenuItemInfo itemInfo : itemInfos)
                {
                    if (i > 0) menu.addSeparator();
                    JMenuItem item = new JMenuItem();
                    item.setName(itemInfo.getMenuItemId());
                    String actionId = itemInfo.getActionId();
                    Action action = actionManager.getAction(actionId, null);
                    item.setAction(action);
                    menu.add(item);
                }

                List<MenuInfo> subMenuInfos = sectionInfo.getMenuInfos();
                List<JMenu> subMenus = newMenus(subMenuInfos);
                for (JMenu subMenu : subMenus) menu.add(subMenu);
            }
        }
        return result;
    }
}
