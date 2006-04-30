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
package org.livetribe.forma.frame;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import org.livetribe.forma.platform.i18n.Bundle;

/**
 * @version $Rev$ $Date$
 */
public class MenuBarService implements IMenuBarService
{
    private IFrameServiceSpi frameService;

    public void setFrameService(IFrameServiceSpi frameService)
    {
        this.frameService = frameService;
    }

    public JMenu addMenu(JMenu parent, String menuKey, Bundle bundle)
    {
        JMenu menu = new JMenu();
        prepareJMenuItem(menu, menuKey, bundle);

        if (parent != null)
        {
            parent.add(menu);
        }
        else
        {
            for (IFrameSpi frame : frameService.getFrames())
            {
                if (getMenu(frame, menuKey) != null) continue;
                frame.getJMenuBar().add(menu);
                frame.revalidate();
            }
        }

        return menu;
    }

    public JMenuItem addMenuItem(JMenu menu, String menuItemKey, Bundle bundle, ActionListener action)
    {
        JMenuItem item = new JMenuItem();
        prepareJMenuItem(item, menuItemKey, bundle);

        String acceleratorKey = menuItemKey + ".accelerator";
        String accelerator = bundle.get(acceleratorKey);
        if (!accelerator.equals(acceleratorKey)) item.setAccelerator(KeyStroke.getKeyStroke(accelerator));

        menu.add(item);
        item.addActionListener(action);

        return item;
    }

    private void prepareJMenuItem(JMenuItem item, String key, Bundle bundle)
    {
        item.setName(key);
        item.setText(bundle.get(key + ".text"));

        String mnemonicKey = key + ".mnemonic";
        String mnemonic = bundle.get(mnemonicKey);
        if (!mnemonic.equals(mnemonicKey) && mnemonic.length() > 0) item.setMnemonic(mnemonic.charAt(0));

        // TODO: handle icons
    }

    public JMenu getMenu(IFrame frame, String menuKey)
    {
        return (JMenu)findComponent(frame.getJMenuBar(), menuKey);
    }

    public JMenuItem getMenuItem(IFrame frame, String menuItemKey)
    {
        return (JMenuItem)findComponent(frame.getJMenuBar(), menuItemKey);
    }

    private Component findComponent(Container root, String name)
    {
        for (Component c : root.getComponents())
        {
            if (name.equals(c.getName())) return c;
            if (c instanceof Container) return findComponent((Container)c, name);
        }
        return null;
    }

    public void addMenuItemSeparator(JMenu menu)
    {
        menu.addSeparator();
    }
}
