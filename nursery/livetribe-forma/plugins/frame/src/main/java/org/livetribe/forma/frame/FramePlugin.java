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

import javax.swing.UIManager;

import org.livetribe.forma.AbstractPlugin;
import org.livetribe.forma.ui.frame.Frame;
import org.livetribe.forma.ui.frame.FrameManager;
import org.livetribe.ioc.Inject;

/**
 * @version $Rev$ $Date$
 */
public class FramePlugin extends AbstractPlugin
{
    @Inject
    private FrameManager frameManager;

    @Override
    protected void doInit()
    {
        assert EventQueue.isDispatchThread();
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);

        setLookAndFeel("com.jgoodies.looks.plastic.Plastic3DLookAndFeel");

        // TODO: Handle uncaught exceptions
//        Thread.currentThread().setUncaughtExceptionHandler(new SwingUncaughtExceptionHandler());
    }

    @Override
    protected void doStart()
    {
        // Delay the display of the Frame to the last moment possible
        EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                frameManager.displayNewFrame(Frame.ID);
            }
        });
    }

    private void setLookAndFeel(String lafClassName)
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
