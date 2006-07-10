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
package org.livetribe.forma.ui.browser.spi;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.livetribe.forma.ExtensionInfo;
import org.livetribe.forma.ExtensionParser;
import org.livetribe.forma.ManagerRegistry;
import org.livetribe.forma.ui.browser.BrowserException;
import org.livetribe.forma.ui.browser.BrowserManager;
import org.livetribe.ioc.Container;
import org.livetribe.ioc.Inject;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * @version $Rev$ $Date$
 */
public class BrowserExtensionParser extends ExtensionParser
{
    @Inject private Container containerManager;
    @Inject private ManagerRegistry managerRegistry;

    public void parse(Element extensionElement, ExtensionInfo extensionInfo)
    {
        try
        {
            BrowserManager browserManager = managerRegistry.get(BrowserManager.ID, BrowserManager.class);
            if (browserManager == null)
            {
                browserManager = new DefaultBrowserManager();
                containerManager.resolve(browserManager);
                managerRegistry.put(BrowserManager.ID, BrowserManager.class, browserManager);
            }
            XPath xpath = XPathFactory.newInstance().newXPath();
            NodeList browsers = (NodeList)xpath.evaluate("browsers/browser", extensionElement, XPathConstants.NODESET);
            for (int i = 0; i < browsers.getLength(); ++i)
            {
                Element browserElement = (Element)browsers.item(i);
                BrowserInfo browserInfo = new BrowserInfo();

                String browserId = evaluateText(xpath.evaluate("@id", browserElement));
                if (browserId == null) throw new BrowserException("Missing required attribute 'id' of element 'browser' in " + extensionInfo.getPluginInfo().getConfigurationFile());
                browserInfo.setBrowserId(browserId);

                String browserClassName = xpath.evaluate("@browser-class", browserElement);
                if (browserClassName == null) throw new BrowserException("Missing required attribute 'browser-class' of element 'browser' in " + extensionInfo.getPluginInfo().getConfigurationFile());
                browserInfo.setBrowserClassName(browserClassName);

                browserManager.spiAddBrowserInfo(browserInfo);
            }
        }
        catch (XPathExpressionException x)
        {
            throw new BrowserException(x);
        }
    }
}
