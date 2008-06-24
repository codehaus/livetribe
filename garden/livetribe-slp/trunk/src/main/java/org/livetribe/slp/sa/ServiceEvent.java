/*
 * Copyright 2005 the original author or authors
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
package org.livetribe.slp.sa;

import java.util.EventObject;

import org.livetribe.slp.ServiceInfo;

/**
 * @version $Revision$ $Date$
 */
public class ServiceEvent extends EventObject
{
    private final ServiceInfo previousService;
    private final ServiceInfo currentService;

    public ServiceEvent(Object source, ServiceInfo previousService, ServiceInfo currentService)
    {
        super(source);
        this.previousService = previousService;
        this.currentService = currentService;
    }

    public ServiceInfo getPreviousService()
    {
        return previousService;
    }

    public ServiceInfo getCurrentService()
    {
        return currentService;
    }
}
