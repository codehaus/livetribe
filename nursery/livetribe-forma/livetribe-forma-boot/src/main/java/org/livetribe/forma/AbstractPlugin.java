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

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @version $Rev$ $Date$
 */
public abstract class AbstractPlugin implements Plugin
{
    protected Logger logger = Logger.getLogger(getClass().getName());

    public void init() throws Exception
    {
        if (logger.isLoggable(Level.FINE)) logger.fine("Initializing plugin " + this);
        doInit();
        if (logger.isLoggable(Level.FINE)) logger.fine("Initialized plugin " + this);
    }

    protected void doInit() throws Exception
    {
    }

    public void start() throws Exception
    {
        if (logger.isLoggable(Level.FINE)) logger.fine("Starting plugin " + this);
        doStart();
        if (logger.isLoggable(Level.FINE)) logger.fine("Started plugin " + this);
    }

    protected void doStart() throws Exception
    {
    }

    public void stop() throws Exception
    {
        if (logger.isLoggable(Level.FINE)) logger.fine("Stopping plugin " + this);
        doStop();
        if (logger.isLoggable(Level.FINE)) logger.fine("Stopped plugin " + this);
    }

    protected void doStop() throws Exception
    {
    }

    public void destroy() throws Exception
    {
        if (logger.isLoggable(Level.FINE)) logger.fine("Destroying plugin " + this);
        doDestroy();
        if (logger.isLoggable(Level.FINE)) logger.fine("Destroyed plugin " + this);
    }

    protected void doDestroy() throws Exception
    {
    }
}
