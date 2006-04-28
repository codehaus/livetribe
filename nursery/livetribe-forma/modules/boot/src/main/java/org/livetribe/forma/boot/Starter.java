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
package org.livetribe.forma.boot;

import java.awt.EventQueue;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * @version $Rev$ $Date$
 */
public class Starter
{
    // Depth of the Marianas Trench: 10911 meters
    private static final int PORT = 10911;

    private Logger logger = Logger.getLogger(getClass().getName());
    private ServerSocket serverSocket;

    public void start()
    {
        try
        {
            serverSocket = new ServerSocket(PORT);
            if (logger.isLoggable(Level.FINE)) logger.fine("Application instance bound TCP port " + PORT + " to avoid multiple instances");
        }
        catch (IOException x)
        {
            showOnlyOneInstanceDialogAndExit();
        }
    }

    protected void showOnlyOneInstanceDialogAndExit()
    {
        try
        {
            // At this point the services provided by Forma are not ready yet (threading, internationalization, etc).
            // Just stick with JOptionPane and english.
            EventQueue.invokeAndWait(new Runnable()
            {
                public void run()
                {
                    JOptionPane.showMessageDialog(null, "Only one instance of the LiveTribe Console can be run at a time", "Error", JOptionPane.ERROR_MESSAGE);
                    System.exit(PORT);
                }
            });
        }
        catch (Exception x)
        {
            if (logger.isLoggable(Level.WARNING)) logger.log(Level.WARNING, "Exception during ");
        }
    }

    public void stop()
    {
        try
        {
            serverSocket.close();
            if (logger.isLoggable(Level.FINE)) logger.fine("Application instance unbound TCP port " + PORT);
        }
        catch (IOException x)
        {
            if (logger.isLoggable(Level.FINEST)) logger.log(Level.FINEST, "Exception ignored", x);
        }
    }
}
