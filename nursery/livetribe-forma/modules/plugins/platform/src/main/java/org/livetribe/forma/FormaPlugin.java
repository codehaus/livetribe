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

import org.livetribe.forma.i18n.DefaultInternationalizationManager;
import org.livetribe.forma.i18n.InternationalizationManager;
import org.livetribe.forma.threading.DefaultThreadingManager;
import org.livetribe.forma.threading.ThreadingManager;
import org.livetribe.ioc.Inject;

/**
 * @version $Rev$ $Date$
 */
public class FormaPlugin extends org.livetribe.forma.AbstractPlugin
{
    @Inject
    private ManagerRegistry managerRegistry;

    @Override
    public void init()
    {
        managerRegistry.put("i18nManager", InternationalizationManager.class, new DefaultInternationalizationManager());
        managerRegistry.put("threadingManager", ThreadingManager.class, new DefaultThreadingManager());
    }

    @Override
    public void destroy()
    {
        managerRegistry.remove("threadingManager");
        managerRegistry.remove("i18nManager");
    }
}
