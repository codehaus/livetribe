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
package org.livetribe.forma.ui.statusbar.spi;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.livetribe.forma.ExtensionParser;
import org.livetribe.forma.ui.statusbar.StatusbarManager;
import org.livetribe.forma.ui.statusbar.StatusbarException;
import org.livetribe.ioc.Container;
import org.livetribe.ioc.Inject;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * @version $Rev$ $Date$
 */
public class StatusbarExtensionParser extends ExtensionParser
{
    @Inject private Container containerManager;
    @Inject private org.livetribe.forma.ManagerRegistry managerRegistry;

    public void parse(Element extensionElement, org.livetribe.forma.ExtensionInfo extensionInfo)
    {
        try
        {
            StatusbarManagerSpi statusbarManager = managerRegistry.get(StatusbarManager.ID, StatusbarManagerSpi.class);
            if (statusbarManager == null)
            {
                statusbarManager = new DefaultStatusbarManager();
                containerManager.resolve(statusbarManager);
                managerRegistry.put(StatusbarManager.ID, StatusbarManagerSpi.class, statusbarManager);
            }
            XPath xpath = XPathFactory.newInstance().newXPath();
            NodeList statusbars = (NodeList)xpath.evaluate("statusbars/statusbar", extensionElement, XPathConstants.NODESET);
            for (int i = 0; i < statusbars.getLength(); ++i)
            {
                Element statusbarElement = (Element)statusbars.item(i);
                StatusbarInfo statusbarInfo = new StatusbarInfo();

                String statusbarId = xpath.evaluate("@id", statusbarElement);
                if (statusbarId == null) throw new StatusbarException("Missing required attribute 'id' of element 'statusbar' in " + extensionInfo.getPluginInfo().getConfigurationFile());
                statusbarInfo.setStatusbarId(statusbarId);

                // TODO: figure out the XML structure and parse it

                statusbarManager.addStatusbarInfo(statusbarInfo);
            }
        }
        catch (XPathExpressionException x)
        {
            throw new StatusbarException(x);
        }
    }
}
