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
package org.livetribe.forma;

import java.util.logging.Logger;
import java.util.ResourceBundle;
import java.util.MissingResourceException;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Element;
import org.livetribe.forma.ExtensionInfo;

/**
 * @version $Rev$ $Date$
 */
public abstract class ExtensionParser
{
    protected final Logger logger = Logger.getLogger(getClass().getName());

    public abstract void parse(Element extensionElement, ExtensionInfo extensionInfo);

    protected String evaluateI18nElement(Element element, ResourceBundle bundle) throws XPathExpressionException
    {
        if (element == null) return null;

        XPath xpath = XPathFactory.newInstance().newXPath();
        String bundleKey = xpath.evaluate("@bundleKey", element);
        if (bundleKey == null || bundleKey.length() == 0)
        {
            return xpath.evaluate("text()", element);
        }
        else
        {
            if (bundle == null)
            {
                return bundleKey;
            }
            else
            {
                try
                {
                    return bundle.getString(bundleKey);
                }
                catch (MissingResourceException x)
                {
                    return bundleKey;
                }
            }
        }
    }
}
