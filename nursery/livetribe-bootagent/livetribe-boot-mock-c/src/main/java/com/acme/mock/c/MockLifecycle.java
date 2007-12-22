/**
 *
 * Copyright 2007 (C) The original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.acme.mock.c;

import java.util.logging.Logger;

import com.acme.mock.Service;

import org.livetribe.boot.LifeCycle;


/**
 * @version $Revision$ $Date$
 */
public class MockLifecycle implements LifeCycle
{
    private final String className = MockLifecycle.class.getName();
    private final Logger logger = Logger.getLogger(className);
    private boolean started = false;
    private Service service;

    public Service getService()
    {
        return service;
    }

    public void start()
    {
        logger.entering(className, "start");

        started = true;

        try
        {
            service = (Service) Class.forName("com.acme.mock.b.MockService").newInstance();
            started = "test mocked by B".equals(service.test("test"));
        }
        catch (InstantiationException e)
        {
            logger.severe("Unable to instantiate com.acme.mock.b.MockService");
            started = false;
        }
        catch (IllegalAccessException e)
        {
            logger.severe("Unable to instantiate com.acme.mock.b.MockService");
            started = false;
        }
        catch (ClassNotFoundException e)
        {
            logger.severe("Unable to instantiate com.acme.mock.b.MockService");
            started = false;
        }

        logger.exiting(className, "start");
    }

    public void stop()
    {
        logger.entering(className, "stop");
        if (!started)
        {
            logger.severe("Not started");
        }
        logger.exiting(className, "stop");
    }
}
