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

import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.livetribe.forma.platform.i18n.Bundle;

/**
 * The API to operate on the frame's menu bar.
 * <br />
 * Menu and menu item information are looked up from localized bundles,
 * including text, icon, accelerator, mnemonic, etc.
 * <br />
 * The menu or menu item have a <b>key</b>, a string that identifies them uniquely
 * among all menu and menu items. The key is used as a root for looking up information
 * from the bundle.
 * <br />
 * Example:
 * <br />
 * This is the code to create the usual "File" menu on the menubar:
 * <pre>
 * JMenu file = menuService.addMenu(null, "menubar.menu.file", bundle);
 * </pre>
 * This should be the content of the english bundle:
 * <pre>
 * menubar.menu.file.text=File
 * menubar.menu.file.mnemonic=F
 * </pre>
 * This is the code to create a "Exit" entry in the "File" menu:
 * <pre>
 * menuService.addMenuItem(file, "menubar.menu.file.menuitem.exit", bundle, exitAction);
 * </pre>
 * And the corrispondent content of the english bundle:
 * <pre>
 * menubar.menu.file.menuitem.exit.text=Exit
 * menubar.menu.file.menuitem.exit.icon=images/exit.png
 * menubar.menu.file.menuitem.exit.mnemonic=X
 * menubar.menu.file.menuitem.exit.accelerator=ctrl Q
 * </pre>
 * @see javax.swing.KeyStroke#getKeyStroke(String) The accelerator syntax
 *
 * @version $Rev$ $Date$
 */
public interface IMenuBarService
{
    public JMenu getMenu(IFrame frame, String menuKey);

    /**
     * Adds a new menu; if parent is null, it will be added to the menu bar, otherwise
     * it will be added to the given parent.
     * @param parent The parent menu to add the new menu to, or null
     * @param menuKey The key of the new menu to be added
     * @param bundle The bundle used to lookup menu information
     * @return The new menu
     * @see #addMenuItem(JMenu, String, Bundle, ActionListener)
     */
    public JMenu addMenu(JMenu parent, String menuKey, Bundle bundle);

    public JMenuItem getMenuItem(IFrame frame, String menuItemKey);

    /**
     * Adds a new menu item.
     * @param menu The menu to add the new menu item to
     * @param menuItemKey The key of the new menu item to be added
     * @param bundle The bundle used to lookup menu information
     * @param action The action to perform when the menu item is clicked
     * @return The new menu item
     * @see #addMenu(JMenu, String, Bundle)
     */
    public JMenuItem addMenuItem(JMenu menu, String menuItemKey, Bundle bundle, ActionListener action);
}
