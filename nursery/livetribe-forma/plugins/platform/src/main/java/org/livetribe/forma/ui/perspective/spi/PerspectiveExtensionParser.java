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
package org.livetribe.forma.ui.perspective.spi;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.livetribe.forma.ExtensionParser;
import org.livetribe.forma.ManagerRegistry;
import org.livetribe.forma.ui.perspective.PerspectiveException;
import org.livetribe.forma.ui.perspective.PerspectiveManager;
import org.livetribe.ioc.Container;
import org.livetribe.ioc.Inject;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * @version $Rev$ $Date$
 */
public class PerspectiveExtensionParser extends ExtensionParser
{
    @Inject private Container containerManager;
    @Inject private ManagerRegistry managerRegistry;

    public void parse(Element extensionElement, org.livetribe.forma.ExtensionInfo extensionInfo)
    {
        try
        {
            PerspectiveManager perspectiveManager = managerRegistry.get(PerspectiveManager.ID, PerspectiveManager.class);
            if (perspectiveManager == null)
            {
                perspectiveManager = new DefaultPerspectiveManager();
                containerManager.resolve(perspectiveManager);
                managerRegistry.put(PerspectiveManager.ID, PerspectiveManager.class, perspectiveManager);
            }
            XPath xpath = XPathFactory.newInstance().newXPath();
            NodeList perspectives = (NodeList)xpath.evaluate("perspectives/perspective", extensionElement, XPathConstants.NODESET);
            for (int i = 0; i < perspectives.getLength(); ++i)
            {
                Element perspectiveElement = (Element)perspectives.item(i);
                PerspectiveInfo perspectiveInfo = new PerspectiveInfo();

                String perspectiveId = evaluateText(xpath.evaluate("@id", perspectiveElement));
                if (perspectiveId == null) throw new PerspectiveException("Missing required attribute 'id' of element 'perspective' in " + extensionInfo.getPluginInfo().getConfigurationFile());
                perspectiveInfo.setPerspectiveId(perspectiveId);

                String perspectiveClassName = xpath.evaluate("@perspective-class", perspectiveElement);
                if (perspectiveClassName == null) throw new PerspectiveException("Missing required attribute 'perspective-class' of element 'perspective' in " + extensionInfo.getPluginInfo().getConfigurationFile());
                perspectiveInfo.setPerspectiveClassName(perspectiveClassName);

                perspectiveManager.spiAddPerspectiveInfo(perspectiveInfo);
            }
        }
        catch (XPathExpressionException x)
        {
            throw new PerspectiveException(x);
        }
    }
}
