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
package org.livetribe.server;

import java.util.List;

import junit.framework.TestCase;
import org.livetribe.slp.api.Configuration;
import org.livetribe.slp.api.sa.ServiceAgent;
import org.livetribe.slp.api.sa.StandardServiceAgent;
import org.livetribe.slp.api.da.DirectoryAgent;
import org.livetribe.slp.api.da.StandardDirectoryAgent;
import org.livetribe.agent.Agent;
import org.livetribe.jmx.mbeans.gateway.MountPoint;

/**
 * @version $Rev$ $Date$
 */
public class ServerTest extends TestCase
{
    public void testStartStop() throws Exception
    {
        Configuration configuration = new Configuration();
        configuration.setPort(1427);

        DirectoryAgent da = new StandardDirectoryAgent();
        da.setConfiguration(configuration);

        Server server = new Server();
        server.setDirectoryAgent(da);

        server.start();
        assertTrue(server.isRunning());

        server.stop();
        assertFalse(server.isRunning());
    }

    public void testServiceRegistration() throws Exception
    {
        Configuration configuration = new Configuration();
        configuration.setPort(1427);

        DirectoryAgent da = new StandardDirectoryAgent();
        da.setConfiguration(configuration);

        Server server = new Server();
        server.setDirectoryAgent(da);

        server.start();

        try
        {
            ServiceAgent sa = new StandardServiceAgent();
            sa.setConfiguration(configuration);

            Agent agent = new Agent();
            agent.setServiceAgent(sa);

            agent.start();

            try
            {
                List mounted = server.getMounted();
                assertNotNull(mounted);
                assertEquals(1, mounted.size());
                MountPoint mountPoint = (MountPoint)mounted.get(0);
                assertNotNull(mountPoint);
            }
            finally
            {
                agent.stop();
            }
        }
        finally
        {
            server.stop();
        }
    }
}
