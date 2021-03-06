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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * @version $Rev$ $Date$
 */
public abstract class ExtensionParser
{
    protected final Logger logger = Logger.getLogger(getClass().getName());

    public abstract void parse(Element extensionElement, ExtensionInfo extensionInfo);

    protected String evaluateI18nElement(Element element, String resourceBundleName) throws XPathExpressionException
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
            if (resourceBundleName == null)
            {
                return bundleKey;
            }
            else
            {
                try
                {
                    ResourceBundle bundle = ResourceBundle.getBundle(resourceBundleName, Locale.getDefault(), Thread.currentThread().getContextClassLoader());
                    return bundle.getString(bundleKey);
                }
                catch (MissingResourceException x)
                {
                    return bundleKey;
                }
            }
        }
    }

    protected void parseProperties(ExtensionInfo extensionInfo, AbstractInfo abstractInfo, Element element) throws XPathExpressionException
    {
        XPath xpath = XPathFactory.newInstance().newXPath();
        NodeList properties = (NodeList)xpath.evaluate("property", element, XPathConstants.NODESET);
        for (int i = 0; i < properties.getLength(); ++i)
        {
            Element propertyElement = (Element)properties.item(i);
            String propertyName = xpath.evaluate("@name", propertyElement);
            if (propertyName == null || propertyName.length() == 0)
                throw new XPathExpressionException("Missing required attribute 'name' in element 'property' in " + extensionInfo.getPluginInfo().getConfigurationFile());
            String propertyValue = evaluateText(xpath.evaluate("text()", propertyElement));
            abstractInfo.put(propertyName, propertyValue);
        }
    }

    public static String evaluateText(String id)
    {
        if (id == null) return null;
        int lastDot = id.lastIndexOf('.');
        if (lastDot > 0)
        {
            String className = id.substring(0, lastDot);
            String fieldName = id.substring(lastDot + 1);
            Class cls = loadClass(className);
            if (cls != null)
            {
                Field field = getStaticStringField(cls, fieldName);
                if (field != null)
                {
                    String fieldValue = getStaticStringValue(field);
                    if (fieldValue != null) return fieldValue;
                }
            }
        }
        return id;
    }

    private static Class loadClass(String className)
    {
        try
        {
            return Thread.currentThread().getContextClassLoader().loadClass(className);
        }
        catch (ClassNotFoundException x)
        {
            return null;
        }
    }

    private static Field getStaticStringField(Class cls, String fieldName)
    {
        try
        {
            Field field = cls.getField(fieldName);
            if (Modifier.isStatic(field.getModifiers()) && field.getType() == String.class) return field;
            return null;
        }
        catch (NoSuchFieldException x)
        {
            return null;
        }
    }

    private static String getStaticStringValue(Field field)
    {
        try
        {
            return (String)field.get(null);
        }
        catch (IllegalAccessException x)
        {
            return null;
        }
    }
}
