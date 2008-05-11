/**
 *
 * Copyright 2008 (C) The original author or authors
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
package org.livetribe.test;

import javax.servlet.Servlet;

import junit.framework.TestCase;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.servlet.ServletHandler;
import org.mortbay.jetty.servlet.ServletHolder;
import org.junit.After;
import org.junit.Before;


/**
 * @version $Revision$ $Date$
 */
public abstract class AbstractHttpTest
{
    private final Server server;

    protected AbstractHttpTest(Servlet servlet, String pattern)
    {
        server = new Server();

        SelectChannelConnector connector = new SelectChannelConnector();
        connector.setPort(8085);
        server.setConnectors(new Connector[]{connector});

        ServletHandler servletHandler = new ServletHandler();
        servletHandler.addServletWithMapping(new ServletHolder(servlet), pattern);
        server.setHandler(servletHandler);
    }


    @Before
    public void setUp() throws Exception
    {
        server.start();
    }

    @After
    public void tearDown() throws Exception
    {
        server.stop();
    }
}