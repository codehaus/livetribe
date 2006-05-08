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
import java.util.List;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.livetribe.forma.ExtensionInfo;
import org.livetribe.forma.ExtensionParser;
import org.livetribe.forma.ManagerRegistry;
import org.livetribe.forma.ui.menubar.MenubarException;
import org.livetribe.forma.ui.menubar.MenubarManager;
import org.livetribe.ioc.Container;
import org.livetribe.ioc.Inject;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * @version $Rev$ $Date$
 */
public class MenubarExtensionParser extends ExtensionParser
{
    @Inject private Container containerManager;
    @Inject private ManagerRegistry managerRegistry;

    public void parse(Element extensionElement, ExtensionInfo extensionInfo)
    {
        try
        {
            MenubarManager menubarManager = managerRegistry.get(MenubarManager.ID, MenubarManager.class);
            if (menubarManager == null)
            {
                menubarManager = new DefaultMenubarManager();
                containerManager.resolve(menubarManager);
                managerRegistry.put(MenubarManager.ID, MenubarManager.class, menubarManager);
            }
            String resourceBundleName = extensionInfo.getPluginInfo().getResourceBundleName();
            XPath xpath = XPathFactory.newInstance().newXPath();
            NodeList menubars = (NodeList)xpath.evaluate("menubars/menubar", extensionElement, XPathConstants.NODESET);
            for (int i = 0; i < menubars.getLength(); ++i)
            {
                Element menubarElement = (Element)menubars.item(i);
                MenubarInfo menubarInfo = new MenubarInfo();

                String menubarId = evaluateId(xpath.evaluate("@id", menubarElement));
                if (menubarId == null) throw new MenubarException("Missing required attribute 'id' of element 'menubar' in " + extensionInfo.getPluginInfo().getConfigurationFile());
                menubarInfo.setMenubarId(menubarId);

                NodeList menus = (NodeList)xpath.evaluate("menus/menu", menubarElement, XPathConstants.NODESET);
                List<MenuInfo> menuInfos = parseMenus(extensionInfo, menus, resourceBundleName);
                for (MenuInfo menuInfo : menuInfos) menubarInfo.addMenuInfo(menuInfo);
                menubarManager.spiAddMenubarInfo(menubarInfo);
            }
        }
        catch (XPathExpressionException x)
        {
            throw new MenubarException(x);
        }
    }

    private List<MenuInfo> parseMenus(ExtensionInfo extensionInfo, NodeList menus, String resourceBundleName) throws XPathExpressionException
    {
        List<MenuInfo> result = new ArrayList<MenuInfo>();
        XPath xpath = XPathFactory.newInstance().newXPath();
        for (int j = 0; j < menus.getLength(); ++j)
        {
            Element menuElement = (Element)menus.item(j);
            MenuInfo menuInfo = new MenuInfo();

            String menuId = evaluateId(xpath.evaluate("@id", menuElement));
            if (menuId == null) throw new MenubarException("Missing required attribute 'id' of element 'menu' in " + extensionInfo.getPluginInfo().getConfigurationFile());
            menuInfo.setMenuId(menuId);

            Element textElement = (Element)xpath.evaluate("text", menuElement, XPathConstants.NODE);
            String menuText = evaluateI18nElement(textElement, resourceBundleName);
            menuInfo.setMenuText(menuText);

            Element mnemonicElement = (Element)xpath.evaluate("mnemonic", menuElement, XPathConstants.NODE);
            String mnemonic = evaluateI18nElement(mnemonicElement, resourceBundleName);
            menuInfo.setMenuMnemonic(mnemonic);

            NodeList sections = (NodeList)xpath.evaluate("sections/section", menuElement, XPathConstants.NODESET);
            for (int k = 0; k < sections.getLength(); ++k)
            {
                Element sectionElement = (Element)sections.item(k);
                MenuSectionInfo sectionInfo = new MenuSectionInfo();

                String sectionId = evaluateId(xpath.evaluate("@id", sectionElement));
                if (sectionId == null) throw new MenubarException("Missing required attribute 'id' of element 'section' in " + extensionInfo.getPluginInfo().getConfigurationFile());
                sectionInfo.setMenuSectionId(sectionId);

                NodeList items = (NodeList)xpath.evaluate("items/item", sectionElement, XPathConstants.NODESET);
                for (int m = 0; m < items.getLength(); ++m)
                {
                    Element itemElement = (Element)items.item(m);
                    MenuItemInfo itemInfo = new MenuItemInfo();

                    String itemId = evaluateId(xpath.evaluate("@id", itemElement));
                    if (itemId == null) throw new MenubarException("Missing required attribute 'id' of element 'item' in " + extensionInfo.getPluginInfo().getConfigurationFile());
                    itemInfo.setMenuItemId(itemId);

                    Element actionElement = (Element)xpath.evaluate("action", itemElement, XPathConstants.NODE);
                    String actionId = evaluateId(xpath.evaluate("@refid", actionElement));
                    if (actionId == null) throw new MenubarException("Missing required attribute 'refid' of element 'action' in " + extensionInfo.getPluginInfo().getConfigurationFile());
                    itemInfo.setActionId(actionId);

                    sectionInfo.addMenuItemInfo(itemInfo);
                }

                NodeList subMenus = (NodeList)xpath.evaluate("items/menu", sectionElement, XPathConstants.NODESET);
                List<MenuInfo> subMenuInfos = parseMenus(extensionInfo, subMenus, resourceBundleName);
                for (MenuInfo subMenuInfo : subMenuInfos) sectionInfo.addMenuInfo(subMenuInfo);

                menuInfo.addMenuSectionInfo(sectionInfo);
            }
            result.add(menuInfo);
        }
        return result;
    }
}
