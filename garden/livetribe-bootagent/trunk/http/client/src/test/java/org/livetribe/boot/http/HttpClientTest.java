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
package org.livetribe.boot.http;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;

import org.livetribe.boot.protocol.BootException;
import org.livetribe.boot.protocol.DoNothing;
import org.livetribe.boot.protocol.ProvisionDirective;
import org.livetribe.boot.protocol.ProvisionEntry;
import org.livetribe.boot.protocol.YouMust;
import org.livetribe.boot.protocol.YouShould;
import org.livetribe.test.AbstractHttpTest;


/**
 * @version $Revision$ $Date$
 */
public class HttpClientTest extends AbstractHttpTest
{
    private static final String UUID_1 = "1234-5678-9abc";
    private static final String UUID_2 = "e9d8-309a-3fe6";
    private static final String UUID_3 = "43e0-a936-0ef5";
    private static final String UUID_4 = "73da-328d-b031";

    public HttpClientTest()
    {
        super(new HttpServlet()
        {
            @Override
            protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
            {
                if (request.getPathInfo() != null)
                {
                    String[] tokens = request.getPathInfo().split("/");

                    if ("provision".equals(tokens[1]))
                    {
                        if (UUID_1.equals(tokens[2]))
                        {
                            if (!"current".equals(tokens[3]))
                            {
                                response.setStatus(404);
                            }
                            else
                            {
                                PrintWriter writer = response.getWriter();

                                writer.println("SHOULD com.acme.Boot 37 2");
                                writer.println("com.acme.service.Foo 15");
                                writer.println("com.acme.service.Bar 1");

                                response.setStatus(200);
                            }
                        }
                        else if (UUID_2.equals(tokens[2]))
                        {
                            if (!"current".equals(tokens[3]))
                            {
                                response.setStatus(404);
                            }
                            else
                            {
                                PrintWriter writer = response.getWriter();

                                writer.println("MUST com.acme.Boot 37 2 true");
                                writer.println("com.acme.service.Foo 15");
                                writer.println("com.acme.service.Bar 1");

                                response.setStatus(200);
                            }
                        }
                        else if (UUID_3.equals(tokens[2]))
                        {
                            if (!"current".equals(tokens[3]))
                            {
                                response.setStatus(404);
                            }
                            else
                            {
                                PrintWriter writer = response.getWriter();

                                writer.println("MUST com.acme.Boot 37 2 true");
                                writer.println("com.acme.service.Foo 15");
                                writer.println("com.acme.service.Bar 1");

                                response.setStatus(200);
                            }
                        }
                        else if ("current".equals(tokens[2]))
                        {
                            PrintWriter writer = response.getWriter();

                            writer.println("MUST com.acme.BootDefault 37 2 true");
                            writer.println("com.acme.service.FooDefault 15");
                            writer.println("com.acme.service.BarDefault 1");

                            response.setStatus(200);
                        }
                        else
                        {
                            response.setStatus(404);

                        }
                    }
                    else if ("content".equals(tokens[1]))
                    {
                        if ("com.acme.service.Foo".equals(tokens[2]) && "15".equals(tokens[3]))
                        {
                            PrintWriter writer = response.getWriter();

                            writer.println("HOW NOW BROWN COW");

                            response.setStatus(200);
                        }
                        else if ("com.acme.service.Bar".equals(tokens[2]) && "1".equals(tokens[3]))
                        {
                            PrintWriter writer = response.getWriter();

                            writer.println("THE RAIN IN SPAIN");

                            response.setStatus(200);
                        }
                        else
                        {
                            response.setStatus(404);
                        }
                    }
                    else
                    {
                        response.setStatus(404);
                    }
                }
            }
        }, "/test/*");
    }

    @Test
    public void testShould() throws Exception
    {
        HttpProvisionProvider provisionProvider = new HttpProvisionProvider(new URL("http://localhost:8085/test/provision/"));
        HttpContentProvider contentProvider = new HttpContentProvider(new URL("http://localhost:8085/test/content/"));

        YouShould directive = (YouShould)provisionProvider.hello(UUID_1, 1);

        assertNotNull(directive);
        assertEquals("com.acme.Boot", directive.getBootClass());
        assertEquals(37, directive.getVersion());
        assertEquals(2, directive.getEntries().size());

        for (ProvisionEntry entry : directive.getEntries())
        {
            if ("com.acme.service.Foo".equals(entry.getName()))
            {
                assertEquals(15, entry.getVersion());
            }
            else if ("com.acme.service.Bar".equals(entry.getName()))
            {
                assertEquals(1, entry.getVersion());
            }
            else
            {
                fail();
            }

            InputStream in = contentProvider.pleaseProvide(entry.getName(), entry.getVersion());

            assertNotNull(in);

            BufferedReader bin = new BufferedReader(new InputStreamReader(in));
            if ("com.acme.service.Foo".equals(entry.getName()))
            {
                assertEquals("HOW NOW BROWN COW", bin.readLine());
            }
            else if ("com.acme.service.Bar".equals(entry.getName())) assertEquals("THE RAIN IN SPAIN", bin.readLine());
        }

        try
        {
            contentProvider.pleaseProvide("BOGUS", 1);
            fail("Should not have loaded bogus entry");
        }
        catch (BootException ignore)
        {
        }
    }

    @Test
    public void testDefault() throws Exception
    {
        HttpProvisionProvider provisionProvider = new HttpProvisionProvider(new URL("http://localhost:8085/test/provision/"));

        YouShould directive = (YouShould)provisionProvider.hello(UUID_4, 1);

        assertNotNull(directive);
        assertEquals("com.acme.BootDefault", directive.getBootClass());
        assertEquals(37, directive.getVersion());
        assertEquals(2, directive.getEntries().size());

        for (ProvisionEntry entry : directive.getEntries())
        {
            if ("com.acme.service.FooDefault".equals(entry.getName()))
            {
                assertEquals(15, entry.getVersion());
            }
            else if ("com.acme.service.BarDefault".equals(entry.getName()))
            {
                assertEquals(1, entry.getVersion());
            }
            else
            {
                fail();
            }
        }
    }

    @Test
    public void testMust() throws Exception
    {
        HttpProvisionProvider provisionProvider = new HttpProvisionProvider(new URL("http://localhost:8085/test/provision/"));
        HttpContentProvider contentProvider = new HttpContentProvider(new URL("http://localhost:8085/test/content/"));

        YouMust directive = (YouMust)provisionProvider.hello(UUID_2, 5);

        assertNotNull(directive);
        assertEquals("com.acme.Boot", directive.getBootClass());
        assertEquals(37, directive.getVersion());
        assertEquals(2, directive.getEntries().size());
        assertTrue(directive.isRestart());

        for (ProvisionEntry entry : directive.getEntries())
        {
            if ("com.acme.service.Foo".equals(entry.getName()))
            {
                assertEquals(15, entry.getVersion());
            }
            else if ("com.acme.service.Bar".equals(entry.getName()))
            {
                assertEquals(1, entry.getVersion());
            }
            else
            {
                fail();
            }

            InputStream in = contentProvider.pleaseProvide(entry.getName(), entry.getVersion());

            assertNotNull(in);

            BufferedReader bin = new BufferedReader(new InputStreamReader(in));
            if ("com.acme.service.Foo".equals(entry.getName()))
            {
                assertEquals("HOW NOW BROWN COW", bin.readLine());
            }
            else if ("com.acme.service.Bar".equals(entry.getName())) assertEquals("THE RAIN IN SPAIN", bin.readLine());
        }
    }

    @Test
    public void testDoNothing() throws Exception
    {
        HttpProvisionProvider provisionProvider = new HttpProvisionProvider(new URL("http://localhost:8085/test/provision/"));

        ProvisionDirective directive = provisionProvider.hello(UUID_3, 1000);

        assertNotNull(directive);
        assertTrue(directive instanceof DoNothing);
    }

}
