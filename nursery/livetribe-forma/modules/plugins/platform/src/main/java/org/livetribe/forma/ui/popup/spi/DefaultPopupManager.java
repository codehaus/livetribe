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

import java.awt.Component;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.livetribe.forma.ui.action.Action;
import org.livetribe.forma.ui.action.ActionContext;
import org.livetribe.forma.ui.action.ActionManager;
import org.livetribe.forma.ui.popup.PopupException;
import org.livetribe.forma.ui.popup.PopupManager;
import org.livetribe.ioc.Inject;

/**
 * @version $Rev$ $Date$
 */
public class DefaultPopupManager implements PopupManager
{
    @Inject
    private ActionManager actionManager;
    private final Map<String, PopupInfo> popupInfos = new HashMap<String, PopupInfo>();

    public void spiAddPopupInfo(PopupInfo popupInfo)
    {
        String popupId = popupInfo.getPopupId();
        PopupInfo existing = popupInfos.get(popupId);
        if (existing == null)
            popupInfos.put(popupId, popupInfo);
        else
            existing.merge(popupInfo);
    }

    public void displayPopupMenu(String popupId, ActionContext context, Component component, int x, int y)
    {
        PopupInfo popupInfo = popupInfos.get(popupId);
        if (popupInfo == null) throw new PopupException("No popup with id '" + popupId + "' is registered in the popup extensions");

        final JPopupMenu result = new JPopupMenu();
        newPopup(new JPopupMenuAdapter(result), popupInfo.getPopupSectionInfos(), context);
        result.show(component, x, y);
    }

    private void newPopup(Menu menu, List<PopupSectionInfo> popupSectionInfos, ActionContext context)
    {
        for (int i = 0; i < popupSectionInfos.size(); ++i)
        {
            PopupSectionInfo popupSectionInfo = popupSectionInfos.get(i);

            List<PopupItemInfo> popupItemInfos = popupSectionInfo.getPopupItemInfos();
            for (PopupItemInfo popupItemInfo : popupItemInfos)
            {
                if (i > 0) menu.addSeparator();
                JMenuItem item = new JMenuItem();
                item.setName(popupItemInfo.getPopupMenuItemId());
                String actionId = popupItemInfo.getActionId();
                Action action = actionManager.getAction(actionId, context);
                item.setAction(action);
                menu.addMenuItem(item);
            }

            List<PopupMenuInfo> popupMenuInfos = popupSectionInfo.getPopupMenuInfos();
            for (PopupMenuInfo popupMenuInfo : popupMenuInfos)
            {
                final JMenu item = new JMenu();
                item.setName(popupMenuInfo.getPopupMenuId());
                item.setText(popupMenuInfo.getPopupMenuText());
                String mnemonic = popupMenuInfo.getPopupMenuMnemonic();
                if (mnemonic != null && mnemonic.length() > 0) item.setMnemonic(mnemonic.charAt(0));
                menu.addMenuItem(item);

                List<PopupSectionInfo> subPopupSectionInfos = popupMenuInfo.getPopupSectionInfos();
                newPopup(new JMenuAdapter(item), subPopupSectionInfos, context);
            }
        }
    }

    private interface Menu
    {
        public void addSeparator();

        public void addMenuItem(JMenuItem child);
    }

    private class JPopupMenuAdapter implements Menu
    {
        private final JPopupMenu popupMenu;

        public JPopupMenuAdapter(JPopupMenu popupMenu)
        {
            this.popupMenu = popupMenu;
        }

        public void addSeparator()
        {
            popupMenu.addSeparator();
        }

        public void addMenuItem(JMenuItem child)
        {
            popupMenu.add(child);
        }
    }

    private class JMenuAdapter implements Menu
    {
        private final JMenu menu;

        public JMenuAdapter(JMenu menu)
        {
            this.menu = menu;
        }

        public void addSeparator()
        {
            menu.addSeparator();
        }

        public void addMenuItem(JMenuItem child)
        {
            menu.add(child);
        }
    }
}
