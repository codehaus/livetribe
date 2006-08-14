/**
 *
 * Copyright 2005 (C) The LiveTribe Group. All Rights Reserved.
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
package org.livetribe;

import javax.management.Attribute;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;

import junit.framework.TestCase;

import org.livetribe.jmx.DynamicMBeanAdapter;
import org.livetribe.jmx.MBeanUtils;


/**
 * @version $Revision: $ $Date: $
 */
public class MBeanUtilsTest extends TestCase
{
    MBeanServer mbeanServer = MBeanServerFactory.newMBeanServer();

    public void testPOJO() throws Exception
    {
        ObjectName objectName = ObjectName.getInstance(":type=JMXService");
        try
        {
            mbeanServer.registerMBean(new DynamicMBeanAdapter(new MockPOJO(32, new Integer(64), false),
                                                              MBeanUtils.createMBeanInfo(MockPOJO.class, "TestPOJO")),
                                      objectName);

            assertTrue(mbeanServer.isRegistered(objectName));

            assertEquals("BAR", mbeanServer.getAttribute(objectName, "foo"));
            try
            {
                mbeanServer.setAttribute(objectName, new Attribute("foo", "CDR"));
                fail("Should have thrown an AttributeNotFoundException");
            }
            catch (AttributeNotFoundException doNothing)
            {
            }

            // iniital value should have been set by spring
            assertEquals(new Integer(32), mbeanServer.getAttribute(objectName, "attr1"));
            mbeanServer.setAttribute(objectName, new Attribute("attr1", new Integer(5)));
            assertEquals(new Integer(5), mbeanServer.getAttribute(objectName, "attr1"));

            // iniital value should have been set by spring
            assertEquals(new Integer(64), mbeanServer.getAttribute(objectName, "attr2"));
            mbeanServer.setAttribute(objectName, new Attribute("attr2", new Integer(7)));
            assertEquals(new Integer(7), mbeanServer.getAttribute(objectName, "attr2"));

            mbeanServer.setAttribute(objectName, new Attribute("dog", Boolean.FALSE));
            assertEquals(Boolean.FALSE, mbeanServer.getAttribute(objectName, "dog"));
            mbeanServer.setAttribute(objectName, new Attribute("dog", Boolean.TRUE));
            assertEquals(Boolean.TRUE, mbeanServer.getAttribute(objectName, "dog"));

            mbeanServer.setAttribute(objectName, new Attribute("bar", "FOO"));
            try
            {
                mbeanServer.getAttribute(objectName, "bar");
                fail("Should have thrown an AttributeNotFoundException");
            }
            catch (AttributeNotFoundException doNothing)
            {
            }
        }
        finally
        {
            try
            {
                mbeanServer.unregisterMBean(objectName);
            }
            catch (InstanceNotFoundException doNothing)
            {
            }
            catch (MBeanRegistrationException doNothing)
            {
            }
        }
    }
}
