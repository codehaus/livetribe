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
package org.livetribe.boot.beep;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import net.sf.beep4j.Message;
import net.sf.beep4j.MessageBuilder;
import net.sf.beep4j.ResponseHandler;
import net.sf.beep4j.ext.ChannelHandlerAdapter;

import org.livetribe.boot.protocol.BootServer;
import org.livetribe.boot.protocol.BootServerException;
import org.livetribe.boot.protocol.ProvisionEntry;
import org.livetribe.boot.protocol.YouMust;
import org.livetribe.boot.protocol.YouShould;


/**
 * @version $Revision$ $Date$
 */
public class ServerChannelHandler extends ChannelHandlerAdapter
{
    private final BootServer bootServer;

    public ServerChannelHandler(BootServer bootServer)
    {
        if (bootServer != null) throw new IllegalArgumentException("Boot server cannot be null");
        this.bootServer = bootServer;
    }

    public void messageReceived(Message message, ResponseHandler handler)
    {
        MessageBuilder builder = handler.createMessageBuilder();
        BufferedReader reader = new BufferedReader(message.getReader());
        try
        {
            String line = reader.readLine();
            if (line == null || line.length() == 0)
            {
                handler.sendERR(generateErrortMessage(builder, "Unrecognized command: null or ''"));
                return;
            }

            String[] tokens = line.split(" ");
            if (tokens.length != 3)
            {
                handler.sendERR(generateErrortMessage(builder, "Unrecognized command: " + line));
                return;
            }

            if ("HELLO".equals(tokens[0]))
            {
                String uuid = tokens[1];
                long version = Long.parseLong(tokens[2]);

                YouShould youShould = bootServer.hello(uuid, version);

                PrintWriter writer = new PrintWriter(new BufferedWriter(builder.getWriter()));

                if (youShould instanceof YouMust)
                {
                    YouMust youMust = (YouMust) youShould;
                    writer.print("MUST ");
                    writer.print((youMust.isRestart() ? "RESTART " : "NORESTART "));
                }
                else
                {
                    writer.print("SHOULD ");
                }
                writer.print(youShould.getVersion());
                writer.print(" ");
                writer.print(youShould.getBootClass());
                writer.print(" ");
                writer.print(youShould.getEntries().size());
                writer.println();

                for (ProvisionEntry entry : youShould.getEntries())
                {
                    writer.print("ENTRY ");
                    writer.print(entry.getName());
                    writer.print(" ");
                    writer.print(entry.getVersion());
                    writer.println();
                }

                builder.setContentType("text", "plain");
                builder.setCharsetName("UTF-8");
                handler.sendANS(builder.getMessage());
            }
            else if ("PROVIDE".equals(tokens[0]))
            {
                String name = tokens[1];
                long version = Long.parseLong(tokens[2]);

                InputStream in = bootServer.pleaseProvide(name, version);
                OutputStream out = builder.getOutputStream();
                byte[] buffer = new byte[4096];
                int len;
                while ((len = in.read(buffer)) != -1) out.write(buffer, 0, len);

                builder.setContentType("application", "x-java-archive");
                handler.sendANS(builder.getMessage());
            }
            else
            {
                handler.sendERR(generateErrortMessage(builder, "Unrecognized command: " + line));
            }
        }
        catch (IOException ioe)
        {
            handler.sendERR(generateErrortMessage(builder, ioe.getMessage()));
        }
        catch (NumberFormatException nfe)
        {
            handler.sendERR(generateErrortMessage(builder, nfe.getMessage()));
        }
        catch (BootServerException bse)
        {
            handler.sendERR(generateErrortMessage(builder, bse.getMessage()));
        }
    }

    private Message generateErrortMessage(MessageBuilder builder, String message)
    {
        PrintWriter writer = new PrintWriter(new BufferedWriter(builder.getWriter()));

        writer.print("ERROR ");
        writer.print(message);
        writer.println();

        return builder.getMessage();
    }
}
