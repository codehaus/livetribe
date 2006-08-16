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
package org.livetribe.slp.demo.client;

import java.awt.EventQueue;

import javax.swing.UIManager;

/**
 * @version $Revision$ $Date$
 */
public class Main
{
    public static void main(String[] args) throws Exception
    {
        EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
                Main main = new Main();
                main.start();
            }
        });
    }

    private void start()
    {
        assert EventQueue.isDispatchThread();

        setLookAndFeel("com.jgoodies.looks.plastic.Plastic3DLookAndFeel");

        Application application = new Application();
        application.init();

        application.setVisible(true);
    }

    private void setLookAndFeel(String lookAndFeel)
    {
        try
        {
            UIManager.setLookAndFeel(lookAndFeel);
        }
        catch (Exception x)
        {
            x.printStackTrace();
        }
    }
}
