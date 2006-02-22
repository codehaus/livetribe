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
package org.livetribe.jmx.util;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * $Rev$ $Date$
 */
public class I18NJMX
{
    public static final String OBJECT_NAME_PROPERTY = "lti18n";
    public static final Locale CLIENT = new Locale("client");

    private static final ThreadLocal locale = new ThreadLocal();

    private I18NJMX()
    {
    }

    public static Locale convert(String localeString)
    {
        if (localeString == null) return null;
        if (CLIENT.getLanguage().equals(localeString)) return CLIENT;
        String[] localeParts = localeString.split("_", 3);
        if (localeParts.length == 0) return null;
        if (localeParts.length == 1) return new Locale(localeParts[0]);
        if (localeParts.length == 2) return new Locale(localeParts[0], localeParts[1]);
        return new Locale(localeParts[0], localeParts[1], localeParts[2]);
    }

    public static void setLocale(Locale l)
    {
        locale.set(l);
    }

    public static Locale getLocale()
    {
        return (Locale)locale.get();
    }

    public static String translate(String key, String defaultValue)
    {
        return translate(key, defaultValue, null);
    }

    public static String translate(String key, String defaultValue, String bundleBaseName)
    {
        Locale l = getLocale();
        if (l == null)
        {
            // No support for translation
            return defaultValue == null ? key : defaultValue;
        }
        else if (CLIENT.equals(l))
        {
            // Translation is done on client side
            return key;
        }
        else
        {
            // Translation is done on server side
            if (bundleBaseName != null)
            {
                try
                {
                    ResourceBundle bundle = ResourceBundle.getBundle(bundleBaseName, l);
                    return bundle.getString(key);
                }
                catch (MissingResourceException x)
                {
                    // TODO: log that bundle or key is not found
                }
            }
            return defaultValue == null ? key : defaultValue;
        }
    }
}
