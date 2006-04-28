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
package org.livetribe.forma.frame;

import java.awt.EventQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;

/**
 * @version $Rev$ $Date$
 */
public class FramePlugin
{
    private final Logger logger = Logger.getLogger(getClass().getName());
    private FrameService frameService;
    private ThreadService threadService;

    public void setFrameService(FrameService frameService)
    {
        this.frameService = frameService;
    }

    public void setThreadService(ThreadService threadService)
    {
        this.threadService = threadService;
    }

    public void start() throws Exception
    {
        threadService.postToEventQueue(new Runnable()
        {
            public void run()
            {
                doStart();
            }
        });
    }

    private void doStart()
    {
        assert EventQueue.isDispatchThread();

        // GUI thread should be fast
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);

        // Handle uncaught exceptions
        Thread.currentThread().setUncaughtExceptionHandler(new SwingUncaughtExceptionHandler());

        // Set look and feel
        // TODO: make look and feel a parameter ?
        setLookAndFeel("com.jgoodies.looks.plastic.Plastic3DLookAndFeel");

        frameService.openNewFrame();
    }

    public void stop()
    {
    }

    protected void setLookAndFeel(String lafClassName)
    {
        try
        {
            UIManager.setLookAndFeel(lafClassName);
        }
        catch (Exception x)
        {
            if (logger.isLoggable(Level.INFO)) logger.log(Level.INFO, "Could not set look and feel " + lafClassName + ", using default look and feel");
        }
    }
}
