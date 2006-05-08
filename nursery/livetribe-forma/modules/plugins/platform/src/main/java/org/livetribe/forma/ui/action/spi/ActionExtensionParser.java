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
package org.livetribe.forma.ui.action.spi;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.livetribe.forma.ExtensionInfo;
import org.livetribe.forma.ExtensionParser;
import org.livetribe.forma.ManagerRegistry;
import org.livetribe.forma.ui.action.ActionException;
import org.livetribe.forma.ui.action.ActionManager;
import org.livetribe.ioc.Container;
import org.livetribe.ioc.Inject;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * @version $Rev$ $Date$
 */
public class ActionExtensionParser extends ExtensionParser
{
    @Inject private Container containerManager;
    @Inject private ManagerRegistry managerRegistry;

    public void parse(Element extensionElement, ExtensionInfo extensionInfo)
    {
        try
        {
            ActionManager actionManager = managerRegistry.get(ActionManager.ID, ActionManager.class);
            if (actionManager == null)
            {
                actionManager = new DefaultActionManager();
                containerManager.resolve(actionManager);
                managerRegistry.put(ActionManager.ID, ActionManager.class, actionManager);
            }
            String resourceBundleName = extensionInfo.getPluginInfo().getResourceBundleName();
            XPath xpath = XPathFactory.newInstance().newXPath();
            NodeList actions = (NodeList)xpath.evaluate("actions/action", extensionElement, XPathConstants.NODESET);
            for (int i = 0; i < actions.getLength(); ++i)
            {
                Element actionElement = (Element)actions.item(i);
                ActionInfo actionInfo = new ActionInfo();

                String actionId = evaluateId(xpath.evaluate("@id", actionElement));
                if (actionId == null) throw new ActionException("Missing required attribute 'id' of element 'action' in " + extensionInfo.getPluginInfo().getConfigurationFile());
                actionInfo.setActionId(actionId);

                String actionClassName = xpath.evaluate("@action-class", actionElement);
                if (actionClassName == null) throw new ActionException("Missing required attribute 'action-class' of element 'action' in " + extensionInfo.getPluginInfo().getConfigurationFile());
                actionInfo.setActionClassName(actionClassName);

                Element textElement = (Element)xpath.evaluate("text", actionElement, XPathConstants.NODE);
                String actionText = evaluateI18nElement(textElement, resourceBundleName);
                actionInfo.setActionText(actionText);

                Element tooltipElement = (Element)xpath.evaluate("tooltip", actionElement, XPathConstants.NODE);
                String actionTooltip = evaluateI18nElement(tooltipElement, resourceBundleName);
                actionInfo.setActionTooltip(actionTooltip);

                Element iconElement = (Element)xpath.evaluate("icon", actionElement, XPathConstants.NODE);
                String iconPath = evaluateI18nElement(iconElement, resourceBundleName);
                actionInfo.setActionIconPath(iconPath);

                Element mnemonicElement = (Element)xpath.evaluate("mnemonic", actionElement, XPathConstants.NODE);
                String mnemonic = evaluateI18nElement(mnemonicElement, resourceBundleName);
                actionInfo.setActionMnemonic(mnemonic);

                Element acceleratorElement = (Element)xpath.evaluate("accelerator", actionElement, XPathConstants.NODE);
                String accelerator = evaluateI18nElement(acceleratorElement, resourceBundleName);
                actionInfo.setActionAccelerator(accelerator);

                actionManager.spiAddActionInfo(actionInfo);
            }
        }
        catch (XPathExpressionException x)
        {
            throw new ActionException(x);
        }
    }
}
