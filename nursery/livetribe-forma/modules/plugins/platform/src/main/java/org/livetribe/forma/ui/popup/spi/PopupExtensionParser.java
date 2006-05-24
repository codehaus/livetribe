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
import java.util.List;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.livetribe.forma.ExtensionInfo;
import org.livetribe.forma.ExtensionParser;
import org.livetribe.forma.ManagerRegistry;
import org.livetribe.forma.ui.popup.PopupException;
import org.livetribe.forma.ui.popup.PopupManager;
import org.livetribe.ioc.Container;
import org.livetribe.ioc.Inject;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * @version $Rev$ $Date$
 */
public class PopupExtensionParser extends ExtensionParser
{
    @Inject private Container containerManager;
    @Inject private ManagerRegistry managerRegistry;

    public void parse(Element extensionElement, ExtensionInfo extensionInfo)
    {
        try
        {
            PopupManager popupManager = managerRegistry.get(PopupManager.ID, PopupManager.class);
            if (popupManager == null)
            {
                popupManager = new DefaultPopupManager();
                containerManager.resolve(popupManager);
                managerRegistry.put(PopupManager.ID, PopupManager.class, popupManager);
            }
            String resourceBundleName = extensionInfo.getPluginInfo().getResourceBundleName();
            XPath xpath = XPathFactory.newInstance().newXPath();
            NodeList popups = (NodeList)xpath.evaluate("popups/popup", extensionElement, XPathConstants.NODESET);
            for (int i = 0; i < popups.getLength(); ++i)
            {
                Element popupElement = (Element)popups.item(i);
                PopupInfo popupInfo = new PopupInfo();

                String popupId = evaluateId(xpath.evaluate("@id", popupElement));
                if (popupId == null) throw new PopupException("Missing required attribute 'id' of element 'popup' in " + extensionInfo.getPluginInfo().getConfigurationFile());
                popupInfo.setPopupId(popupId);

                NodeList popupSections = (NodeList)xpath.evaluate("sections/section", popupElement, XPathConstants.NODESET);
                List<PopupSectionInfo> popupSectionInfos = parseSections(extensionInfo, popupSections, resourceBundleName);
                for (PopupSectionInfo popupSectionInfo : popupSectionInfos) popupInfo.addPopupSectionInfo(popupSectionInfo);

                popupManager.spiAddPopupInfo(popupInfo);
            }
        }
        catch (XPathExpressionException x)
        {
            throw new PopupException(x);
        }
    }

    private List<PopupSectionInfo> parseSections(ExtensionInfo extensionInfo, NodeList popupSections, String resourceBundleName) throws XPathExpressionException
    {
        List<PopupSectionInfo> result = new ArrayList<PopupSectionInfo>();
        XPath xpath = XPathFactory.newInstance().newXPath();
        for (int i = 0; i < popupSections.getLength(); ++i)
        {
            Element sectionElement = (Element)popupSections.item(i);
            PopupSectionInfo popupSectionInfo = new PopupSectionInfo();

            String popupsectionId = evaluateId(xpath.evaluate("@id", sectionElement));
            if (popupsectionId == null) throw new PopupException("Missing required attribute 'id' of element 'section' in " + extensionInfo.getPluginInfo().getConfigurationFile());
            popupSectionInfo.setPopupSectionId(popupsectionId);

            NodeList popupItems = (NodeList)xpath.evaluate("items/item", sectionElement, XPathConstants.NODESET);
            List<PopupItemInfo> popupItemInfos = parsePopupItems(extensionInfo, popupItems);
            for (PopupItemInfo popupItemInfo : popupItemInfos) popupSectionInfo.addPopupItemInfo(popupItemInfo);

            NodeList menus = (NodeList)xpath.evaluate("items/menu", sectionElement, XPathConstants.NODESET);
            List<PopupMenuInfo> popupMenuInfos = parsePopupMenus(extensionInfo, menus, resourceBundleName);
            for (PopupMenuInfo popupMenuInfo : popupMenuInfos) popupSectionInfo.addPopupMenuInfo(popupMenuInfo);

            result.add(popupSectionInfo);
        }
        return result;
    }

    private List<PopupItemInfo> parsePopupItems(ExtensionInfo extensionInfo, NodeList popupItems) throws XPathExpressionException
    {
        List<PopupItemInfo> result = new ArrayList<PopupItemInfo>();
        XPath xpath = XPathFactory.newInstance().newXPath();
        for (int i = 0; i < popupItems.getLength(); ++i)
        {
            Element popupItemElement = (Element)popupItems.item(i);
            PopupItemInfo popupItemInfo = new PopupItemInfo();

            String popupItemId = evaluateId(xpath.evaluate("@id", popupItemElement));
            if (popupItemId == null) throw new PopupException("Missing required attribute 'id' of element 'item' in " + extensionInfo.getPluginInfo().getConfigurationFile());
            popupItemInfo.setPopupMenuItemId(popupItemId);

            Element actionElement = (Element)xpath.evaluate("action", popupItemElement, XPathConstants.NODE);
            String actionId = evaluateId(xpath.evaluate("@refid", actionElement));
            if (actionId == null) throw new PopupException("Missing required attribute 'refid' of element 'action' in " + extensionInfo.getPluginInfo().getConfigurationFile());
            popupItemInfo.setActionId(actionId);

            result.add(popupItemInfo);
        }
        return result;
    }

    private List<PopupMenuInfo> parsePopupMenus(ExtensionInfo extensionInfo, NodeList popupMenus, String resourceBundleName) throws XPathExpressionException
    {
        List<PopupMenuInfo> result = new ArrayList<PopupMenuInfo>();
        XPath xpath = XPathFactory.newInstance().newXPath();
        for (int i = 0; i < popupMenus.getLength(); ++i)
        {
            Element popupMenuElement = (Element)popupMenus.item(i);
            PopupMenuInfo popupMenuInfo = new PopupMenuInfo();

            String popupMenuId = evaluateId(xpath.evaluate("@id", popupMenuElement));
            if (popupMenuId == null) throw new PopupException("Missing required attribute 'id' of element 'menu' in " + extensionInfo.getPluginInfo().getConfigurationFile());
            popupMenuInfo.setPopupMenuId(popupMenuId);

            Element textElement = (Element)xpath.evaluate("text", popupMenuElement, XPathConstants.NODE);
            String popupMenuText = evaluateI18nElement(textElement, resourceBundleName);
            popupMenuInfo.setPopupMenuText(popupMenuText);

            Element mnemonicElement = (Element)xpath.evaluate("mnemonic", popupMenuElement, XPathConstants.NODE);
            String popupMnemonic = evaluateI18nElement(mnemonicElement, resourceBundleName);
            popupMenuInfo.setPopupMenuMnemonic(popupMnemonic);

            NodeList subSections = (NodeList)xpath.evaluate("sections/section", popupMenuElement, XPathConstants.NODESET);
            List<PopupSectionInfo> subSectionInfos = parseSections(extensionInfo, subSections, resourceBundleName);
            for (PopupSectionInfo subPopupSectionInfo : subSectionInfos) popupMenuInfo.addPopupSectionInfo(subPopupSectionInfo);

            result.add(popupMenuInfo);
        }
        return result;
    }
}
