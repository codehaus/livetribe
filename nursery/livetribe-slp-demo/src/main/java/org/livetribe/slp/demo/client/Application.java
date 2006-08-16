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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.net.URL;

import javax.management.Attribute;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import edu.emory.mathcs.backport.java.util.concurrent.Callable;
import foxtrot.Task;
import foxtrot.Worker;
import foxtrot.utils.FlushJob;

/**
 * @version $Revision$ $Date$
 */
public class Application extends JFrame
{
    private ExceptionHandler exceptionHandler;
    private NetworkModel networkModel;
    private HistoryPanel historyPanel;

    public Application() throws HeadlessException
    {
        super("LiveTribe SLP Demo");
    }

    public void init()
    {
        installServices();
        installMenuBar();
        installComponents();

        setIconImage(getIcon("images/demo.png").getImage());

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screen.width - (screen.width >> 2);
        int height = screen.height - (screen.height >> 2);
        setSize(width, height);
        setLocationRelativeTo(null);
    }

    private void installServices()
    {
        System.setProperty("sun.awt.exception.handler", ExceptionHandler.class.getName());
        exceptionHandler = new ExceptionHandler();
        networkModel = new NetworkModel();
    }

    private void installMenuBar()
    {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu file = new JMenu("File");
        menuBar.add(file);

        JMenuItem startScan = new JMenuItem("Start Scanning");
        file.add(startScan);
        startScan.addActionListener(new StartScanAction(this, networkModel));
        startScan.setIcon(getIcon("images/ktalkd.png"));

        JMenuItem stopScan = new JMenuItem("Stop Scanning");
        file.add(stopScan);
        stopScan.addActionListener(new StopScanAction(this, networkModel));

        file.addSeparator();
        JMenuItem exit = new JMenuItem("Exit");
        file.add(exit);

        exit.setIcon(getIcon("images/exit.png"));
        exit.addActionListener(new ExitAction(this));
    }

    private void installComponents()
    {
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        historyPanel = new HistoryPanel(this);
        historyPanel.init();
        networkModel.addObserver(historyPanel);
        contentPane.add(historyPanel, BorderLayout.CENTER);

        NetworkPanel network = new NetworkPanel(this, networkModel);
        network.init();
        networkModel.addObserver(network);
        showPanel(network);
    }

    public void showPanel(Component component)
    {
        historyPanel.showPanel(component);
    }

    public Object post(final Callable callable)
    {
        Feedback.startWaitCursor(this);
        try
        {
            return Worker.post(new Task()
            {
                public Object run() throws Exception
                {
                    return callable.call();
                }
            });
        }
        catch (Exception x)
        {
            handleException(x);
            return null;
        }
        finally
        {
            Feedback.stopWaitCursor(this);
        }
    }

    public void flushEvents()
    {
        Worker.post(new FlushJob());
    }

    public Object getAttribute(final MBeanServerConnection connection, final String objectNameString, final String attributeName)
    {
        return post(new Callable()
        {
            public Object call() throws Exception
            {
                ObjectName objectName = ObjectName.getInstance(objectNameString);
                return connection.getAttribute(objectName, attributeName);
            }
        });
    }

    public void setAttribute(final MBeanServerConnection connection, final String objectNameString, final String attributeName, final Object attributeValue)
    {
        post(new Callable()
        {
            public Object call() throws Exception
            {
                ObjectName objectName = ObjectName.getInstance(objectNameString);
                connection.setAttribute(objectName, new Attribute(attributeName, attributeValue));
                return null;
            }
        });
    }

    public Object invoke(final MBeanServerConnection connection, final String objectNameString, final String methodName)
    {
        return post(new Callable()
        {
            public Object call() throws Exception
            {
                ObjectName objectName = ObjectName.getInstance(objectNameString);
                return connection.invoke(objectName, methodName, null, null);
            }
        });
    }

    public void handleException(Throwable exception)
    {
        exceptionHandler.handle(exception);
    }

    public ImageIcon getIcon(String iconPath)
    {
        URL iconURL = getClass().getClassLoader().getResource(iconPath);
        return new ImageIcon(iconURL);
    }
}
