/**
 *
 * Copyright 2006 (C) The original author or authors.
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
package org.livetribe.arm40.util;

import java.util.Iterator;

import edu.emory.mathcs.backport.java.util.concurrent.CopyOnWriteArrayList;
import org.opengroup.arm40.transaction.ArmInterface;


/**
 * @version $Revision: $ $Date: $
 */
public class ArmAPIMonitorBroadcaster implements ArmAPIMonitor
{
    private final CopyOnWriteArrayList apiMonitors = new CopyOnWriteArrayList();

    public void addArmAPIMonitor(ArmAPIMonitor apiMonitor)
    {
        apiMonitors.addIfAbsent(apiMonitor);
    }

    public void removeArmAPIMonitor(ArmAPIMonitor apiMonitor)
    {
        apiMonitors.remove(apiMonitor);
    }

    /**
     * {@inheritDoc}
     */
    public void begin(ArmInterface object)
    {
        for (Iterator iterator = apiMonitors.iterator(); iterator.hasNext();)
        {
            ArmAPIMonitor apiMonitor = (ArmAPIMonitor) iterator.next();
            try
            {
                apiMonitor.begin(object);
            }
            catch (Throwable ignored)
            {
                // ignore - we did our best to notify the world
            }
        }
    }

    public void warning(int code)
    {
        for (Iterator iterator = apiMonitors.iterator(); iterator.hasNext();)
        {
            ArmAPIMonitor apiMonitor = (ArmAPIMonitor) iterator.next();
            try
            {
                apiMonitor.warning(code);
            }
            catch (Throwable ignored)
            {
                // ignore - we did our best to notify the world
            }
        }
    }

    public void error(int code)
    {
        for (Iterator iterator = apiMonitors.iterator(); iterator.hasNext();)
        {
            ArmAPIMonitor apiMonitor = (ArmAPIMonitor) iterator.next();
            try
            {
                apiMonitor.error(code);
            }
            catch (Throwable ignored)
            {
                // ignore - we did our best to notify the world
            }
        }
    }

    public void end()
    {
        for (Iterator iterator = apiMonitors.iterator(); iterator.hasNext();)
        {
            ArmAPIMonitor apiMonitor = (ArmAPIMonitor) iterator.next();
            try
            {
                apiMonitor.end();
            }
            catch (Throwable ignored)
            {
                // ignore - we did our best to notify the world
            }
        }
    }
}
