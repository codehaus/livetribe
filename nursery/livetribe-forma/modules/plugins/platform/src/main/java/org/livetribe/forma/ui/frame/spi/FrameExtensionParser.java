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
package org.livetribe.forma.ui.frame.spi;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.livetribe.forma.ExtensionInfo;
import org.livetribe.forma.ExtensionParser;
import org.livetribe.forma.ManagerRegistry;
import org.livetribe.forma.ui.frame.FrameManager;
import org.livetribe.forma.ui.frame.FrameException;
import org.livetribe.ioc.Container;
import org.livetribe.ioc.Inject;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * @version $Rev$ $Date$
 */
public class FrameExtensionParser extends ExtensionParser
{
    @Inject private Container containerManager;
    @Inject private ManagerRegistry managerRegistry;

    public void parse(Element extensionElement, ExtensionInfo extensionInfo)
    {
        try
        {
            FrameManagerSpi frameManager = managerRegistry.get(FrameManager.ID, FrameManagerSpi.class);
            if (frameManager == null)
            {
                frameManager = new DefaultFrameManager();
                containerManager.resolve(frameManager);
                managerRegistry.put(FrameManager.ID, FrameManagerSpi.class, frameManager);
            }

            XPath xpath = XPathFactory.newInstance().newXPath();
            NodeList frames = (NodeList)xpath.evaluate("frames/frame", extensionElement, XPathConstants.NODESET);
            for (int i = 0; i < frames.getLength(); ++i)
            {
                Element frameElement = (Element)frames.item(i);
                FrameInfo frameInfo = new FrameInfo();

                String frameId = xpath.evaluate("@id", frameElement);
                frameInfo.setFrameId(frameId);

                String frameClassName = xpath.evaluate("@frame-class", frameElement);
                frameInfo.setFrameClassName(frameClassName);

                frameManager.addFrameInfo(frameInfo);
            }
        }
        catch (XPathExpressionException x)
        {
            throw new FrameException(x);
        }
    }
}
