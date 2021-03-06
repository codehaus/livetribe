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
package org.livetribe.forma.i18n;

import java.util.Locale;

/**
 * @version $Rev$ $Date$
 */
public class DefaultInternationalizationManager implements InternationalizationManager
{
    public Bundle getBundle(String bundleName, Locale locale)
    {
        if (locale == null) locale = Locale.getDefault();
        return new Bundle(bundleName, locale, Thread.currentThread().getContextClassLoader());
    }
}
