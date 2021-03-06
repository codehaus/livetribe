/**
 *
 * Copyright 2008-2010 (C) The original author or authors
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
 * An HTTP server implementation that an HTTP client can communicate with to
 * obtain its provisioning directives and content.  While this servlet can
 * support requests from the HTTP client, serious installations may want to
 * implement their own more robust implementations.
 *
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

        provisionProvider = (ProvisionProvider)applicationContext.getBean("provisionProvider");
        if (provisionProvider == null)
        {
            LOGGER.severe("Missing provision provider");
            throw new ServletException("Missing provision provider");
        }

        contentProvider = (ContentProvider)applicationContext.getBean("contentProvider");
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
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String[] tokens = request.getPathInfo().split("/");

        if (tokens.length != 3)
        {
            LOGGER.warning("Path '" + request.getPathInfo() + "' did not properly split into three parts");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String command = tokens[0].trim();
        try
        {
            if ("hello".equalsIgnoreCase(command))
            {
                String uuid = tokens[1].trim();
                long version = Long.valueOf(tokens[2].trim());

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

                    YouShould should = (YouShould)directive;

                    writer.print(should.getBootClass());
                    writer.print(" ");

                    writer.print(should.getVersion());

                    if (directive instanceof YouMust)
                    {
                        writer.print(" ");
                        writer.print(((YouMust)directive).isRestart() ? "true" : "false");
                    }

                    writer.println();

                    for (ProvisionEntry entry : should.getEntries())
                    {
                        writer.print(entry.getName());
                        writer.print(" ");
                        writer.println(entry.getVersion());
                    }
                }

                response.setStatus(HttpServletResponse.SC_OK);
            }
            else if ("provide".equalsIgnoreCase(command))
            {
                int len;
                byte[] buffer = new byte[4096];

                InputStream in = contentProvider.pleaseProvide(tokens[1], Long.valueOf(tokens[2].trim()));

                try
                {
                    response.setHeader("content-type", "application/x-java-archive");

                    OutputStream out = response.getOutputStream();

                    try
                    {
                        while ((len = in.read(buffer)) != -1) out.write(buffer, 0, len);

                        out.flush();
                    }
                    finally
                    {
                        try
                        {
                            out.close();
                        }
                        catch (IOException ioe)
                        {
                            LOGGER.log(Level.WARNING, "Problems closing output stream", ioe);
                        }
                    }
                }
                finally
                {
                    try
                    {
                        in.close();
                    }
                    catch (IOException ioe)
                    {
                        LOGGER.log(Level.WARNING, "Problems closing input stream", ioe);
                    }
                }

                response.setStatus(HttpServletResponse.SC_OK);
            }
            else
            {
                LOGGER.warning("Did not recogize the command " + command);
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        }
        catch (NumberFormatException nfe)
        {
            LOGGER.log(Level.WARNING, "Unable to parse a long from '" + tokens[2] + "'", nfe);
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        catch (BootException bse)
        {
            LOGGER.log(Level.WARNING, "Boot exception", bse);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
