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
package org.picateers.boot.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import org.livetribe.boot.protocol.BootException;
import org.livetribe.boot.protocol.ContentProvider;
import org.livetribe.boot.protocol.DoNothing;
import org.livetribe.boot.protocol.ProvisionDirective;
import org.livetribe.boot.protocol.ProvisionEntry;
import org.livetribe.boot.protocol.ProvisionProvider;
import org.livetribe.boot.protocol.YouMust;
import org.livetribe.boot.protocol.YouShould;


/**
 * @version $Revision$ $Date$
 */
public class BootServerServlet extends HttpServlet
{
    private final static String CLASS_NAME = BootServerServlet.class.getName();
    private final static Logger LOGGER = Logger.getLogger(CLASS_NAME);

    private ProvisionProvider provisionProvider;
    private ContentProvider contentProvider;

    public void init(ServletConfig servletConfig) throws ServletException
    {
        super.init(servletConfig);

        ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletConfig.getServletContext());
        if (applicationContext == null)
        {
            LOGGER.severe("Missing Spring application context");
            throw new ServletException("Missing Spring application context");
        }

        provisionProvider = (ProvisionProvider) applicationContext.getBean("provisionProvider");
        if (provisionProvider == null)
        {
            LOGGER.severe("Missing provision provider");
            throw new ServletException("Missing provision provider");
        }

        contentProvider = (ContentProvider) applicationContext.getBean("contentProvider");
        if (contentProvider == null)
        {
            LOGGER.severe("Missing content provider");
            throw new ServletException("Missing content provider");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        LOGGER.entering(CLASS_NAME, "doGet", new Object[]{request, response});

        if (request.getPathInfo() == null)
        {
            LOGGER.warning("Path was null");
            response.setStatus(400);
            return;
        }

        String[] tokens = request.getPathInfo().split("/");

        if (tokens.length != 4)
        {
            LOGGER.warning("Path '" + request.getPathInfo() + "' did not properly split into three parts");
            response.setStatus(400);
            return;
        }

        try
        {
            if ("hello".equalsIgnoreCase(tokens[1].trim()))
            {
                String uuid = tokens[2].trim();
                long version = Long.valueOf(tokens[3].trim());

                ProvisionDirective directive = provisionProvider.hello(uuid, version);

                response.setHeader("content-type", "text/plain");

                PrintWriter writer = response.getWriter();

                if (directive instanceof DoNothing)
                {
                    writer.println("NOTHING");
                }
                else
                {
                    if (directive instanceof YouMust) writer.print("MUST ");
                    else writer.print("SHOULD ");

                    YouShould should = (YouShould) directive;

                    writer.print(should.getBootClass());
                    writer.print(" ");

                    writer.print(should.getVersion());

                    if (directive instanceof YouMust)
                    {
                        writer.print(" ");
                        writer.print(((YouMust) directive).isRestart() ? "true" : "false");
                    }

                    writer.println();

                    for (ProvisionEntry entry : should.getEntries())
                    {
                        writer.print(entry.getName());
                        writer.print(" ");
                        writer.println(entry.getVersion());
                    }
                }

                response.setStatus(200);
            }
            else if ("provide".equalsIgnoreCase(tokens[1]))
            {
                int len;
                byte[] buffer = new byte[4096];

                InputStream in = contentProvider.pleaseProvide(tokens[2], Long.valueOf(tokens[3].trim()));

                response.setHeader("content-type", "application/x-java-archive");

                OutputStream out = response.getOutputStream();

                while ((len = in.read(buffer)) != -1) out.write(buffer, 0, len);

                out.flush();

                response.setStatus(200);
            }
            else
            {
                LOGGER.warning("Did not recogize the command " + tokens[1]);
                response.setStatus(400);
            }
        }
        catch (NumberFormatException nfe)
        {
            LOGGER.log(Level.WARNING, "Unable to parse a long from '" + tokens[3] + "'", nfe);
            response.setStatus(400);
        }
        catch (BootException bse)
        {
            LOGGER.log(Level.WARNING, "Boot exception", bse);
            response.setStatus(400);
        }
    }
}
