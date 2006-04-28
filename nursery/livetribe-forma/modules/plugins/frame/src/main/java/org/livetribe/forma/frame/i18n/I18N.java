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
package org.livetribe.forma.frame.i18n;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.MissingResourceException;
import java.text.MessageFormat;

/**
 * @version $Rev$ $Date$
 */
public class I18N
{
    private final String bundleName;
    private final Locale locale;
    private final ClassLoader classLoader;

    public I18N(String bundleName, Locale locale, ClassLoader classLoader)
    {
        this.bundleName = bundleName;
        this.locale = locale;
        this.classLoader = classLoader;
    }

    public String get(String key, Object... args)
    {
        String translated = getString(key);
        if (args != null && args.length > 0) translated = MessageFormat.format(translated, args);
        return translated;
    }

    private String getString(String key)
    {
        try
        {
            ResourceBundle bundle = ResourceBundle.getBundle(bundleName, locale, classLoader);
            return bundle.getString(key);
        }
        catch (MissingResourceException x)
        {
            return key;
        }
    }
}
