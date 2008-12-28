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
package org.livetribe.boot.client;

import java.util.concurrent.ScheduledThreadPoolExecutor;

import junit.framework.TestCase;


/**
 * @version $Revision$ $Date$
 */
public class ClientTest extends TestCase
{
    ScheduledThreadPoolExecutor executor;

    public void test() throws Exception
    {
        try
        {
            Client client = new Client(new MockProvisionProvider(), new MockContentProvider(), executor, new MockProvisionStore());

            client.start();

            client.stop();
        }
        finally
        {
            executor.shutdown();
        }
    }

    public void setUp() throws Exception
    {
        executor = new ScheduledThreadPoolExecutor(5);
    }

    public void tearDown() throws Exception
    {
        executor.shutdown();
    }
}
