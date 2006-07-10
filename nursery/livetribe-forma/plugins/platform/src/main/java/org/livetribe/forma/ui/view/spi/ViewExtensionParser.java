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
package org.livetribe.forma.ui.view.spi;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.livetribe.forma.ExtensionInfo;
import org.livetribe.forma.ExtensionParser;
import org.livetribe.forma.ManagerRegistry;
import org.livetribe.forma.ui.view.ViewException;
import org.livetribe.forma.ui.view.ViewManager;
import org.livetribe.ioc.Container;
import org.livetribe.ioc.Inject;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * @version $Rev$ $Date$
 */
public class ViewExtensionParser extends ExtensionParser
{
    @Inject private Container containerManager;
    @Inject private ManagerRegistry managerRegistry;

    public void parse(Element element, ExtensionInfo extensionInfo)
    {
        try
        {
            ViewManager viewManager = managerRegistry.get(ViewManager.ID, ViewManager.class);
            if (viewManager == null)
            {
                viewManager = new DefaultViewManager();
                containerManager.resolve(viewManager);
                managerRegistry.put(ViewManager.ID, ViewManager.class, viewManager);
            }
            String resourceBundleName = extensionInfo.getPluginInfo().getResourceBundleName();
            XPath xpath = XPathFactory.newInstance().newXPath();
            NodeList views = (NodeList)xpath.evaluate("views/view", element, XPathConstants.NODESET);
            for (int i = 0; i < views.getLength(); ++i)
            {
                Element viewElement = (Element)views.item(i);
                ViewInfo viewInfo = new ViewInfo();

                String viewId = evaluateText(xpath.evaluate("@id", viewElement));
                if (viewId == null) throw new ViewException("Missing required attribute 'id' of element 'view' in " + extensionInfo.getPluginInfo().getConfigurationFile());
                viewInfo.setViewId(viewId);

                String viewClassName = xpath.evaluate("@view-class", viewElement);
                if (viewClassName == null) throw new ViewException("Missing required attribute 'view-class' of element 'view' in " + extensionInfo.getPluginInfo().getConfigurationFile());
                viewInfo.setViewClassName(viewClassName);

                viewManager.spiAddViewInfo(viewInfo);
            }
        }
        catch (XPathExpressionException x)
        {
            throw new ViewException(x);
        }
    }
}
