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
package org.livetribe.forma.ui.editor.spi;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.livetribe.forma.ExtensionInfo;
import org.livetribe.forma.ExtensionParser;
import org.livetribe.forma.ManagerRegistry;
import org.livetribe.forma.ui.editor.EditorException;
import org.livetribe.forma.ui.editor.EditorManager;
import org.livetribe.ioc.Container;
import org.livetribe.ioc.Inject;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * @version $Rev$ $Date$
 */
public class EditorExtensionParser extends ExtensionParser
{
    @Inject
    private Container containerManager;
    @Inject
    private ManagerRegistry managerRegistry;

    public void parse(Element extensionElement, ExtensionInfo extensionInfo)
    {
        try
        {
            EditorManager editorManager = managerRegistry.get(EditorManager.ID, EditorManager.class);
            if (editorManager == null)
            {
                editorManager = new DefaultEditorManager();
                containerManager.resolve(editorManager);
                managerRegistry.put(EditorManager.ID, EditorManager.class, editorManager);
            }

            XPath xpath = XPathFactory.newInstance().newXPath();
            NodeList sources = (NodeList)xpath.evaluate("sources/source", extensionElement, XPathConstants.NODESET);
            for (int i = 0; i < sources.getLength(); ++i)
            {
                Element sourceElement = (Element)sources.item(i);
                EditorSourceInfo sourceInfo = new EditorSourceInfo();

                String sourceClassName = xpath.evaluate("@source-class", sourceElement);
                if (sourceClassName == null)
                    throw new EditorException("Missing required attribute 'source-class' of element 'source' in " + extensionInfo.getPluginInfo().getConfigurationFile());
                sourceInfo.setEditorSourceClassName(sourceClassName);

                String specializes = xpath.evaluate("@specializes", sourceElement);
                if (specializes != null && specializes.trim().length() > 0) sourceInfo.addSpecializedEditorSource(specializes);

                parseProperties(extensionInfo, sourceInfo, sourceElement);

                editorManager.spiAddEditorSourceInfo(sourceInfo);
            }

            NodeList editors = (NodeList)xpath.evaluate("editors/editor", extensionElement, XPathConstants.NODESET);
            for (int i = 0; i < editors.getLength(); ++i)
            {
                Element editorElement = (Element)editors.item(i);
                EditorInfo editorInfo = new EditorInfo();

                String editorClassName = xpath.evaluate("@editor-class", editorElement);
                if (editorClassName == null)
                    throw new EditorException("Missing required attribute 'editor-class' of element 'editor' in " + extensionInfo.getPluginInfo().getConfigurationFile());
                editorInfo.setEditorClassName(editorClassName);

                parseProperties(extensionInfo, editorInfo, editorElement);

                editorManager.spiAddEditorInfo(editorInfo);
            }
        }
        catch (XPathExpressionException x)
        {
            throw new EditorException(x);
        }
    }
}
