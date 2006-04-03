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
package org.livetribe.agent;

import junit.framework.TestCase;
import org.livetribe.slp.api.Configuration;
import org.livetribe.slp.api.sa.ServiceAgent;
import org.livetribe.slp.api.sa.StandardServiceAgent;

/**
 * @version $Rev$ $Date$
 */
public class AgentTest extends TestCase
{
    public void testStartStop() throws Exception
    {
        Configuration configuration = new Configuration();
        configuration.setPort(1427);

        ServiceAgent sa = new StandardServiceAgent();
        sa.setConfiguration(configuration);

        Agent agent = new Agent();
        agent.setServiceAgent(sa);

        agent.start();
        assertTrue(agent.isRunning());

        agent.stop();
        assertFalse(agent.isRunning());
    }
}
