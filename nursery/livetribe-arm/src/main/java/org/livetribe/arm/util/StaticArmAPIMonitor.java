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
package org.livetribe.arm.util;

import org.opengroup.arm40.transaction.ArmInterface;


/**
 * @version $Revision: $ $Date: $
 */
public class StaticArmAPIMonitor
{
    private static final ArmAPIMonitorBroadcaster apiMonitor = new ArmAPIMonitorBroadcaster();

    public static void addArmAPIMonitor(ArmAPIMonitor monitor)
    {
        if (monitor == null) throw new NullPointerException("monitor is null");
        apiMonitor.addArmAPIMonitor(monitor);
    }

    public static void removeArmAPIMonitor(ArmAPIMonitor monitor)
    {
        if (monitor == null) throw new NullPointerException("monitor is null");
        apiMonitor.removeArmAPIMonitor(monitor);
    }

    public static void begin(ArmInterface factory)
    {
        apiMonitor.begin(factory);
    }

    public static void warning(int code)
    {
        apiMonitor.warning(code);
    }

    public static void error(int code)
    {
        apiMonitor.error(code);
    }

    public static void end()
    {
        apiMonitor.end();
    }
}
