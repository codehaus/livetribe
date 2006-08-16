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
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.management.MBeanServerConnection;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.remote.JMXConnectionNotification;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import edu.emory.mathcs.backport.java.util.concurrent.Callable;
import org.livetribe.slp.ServiceInfo;

/**
 * @version $Revision$ $Date$
 */
public class ServiceInfoDetailPanel extends JPanel implements NotificationListener
{
    private final Application application;
    private final ServiceInfo serviceInfo;
    private MBeanServerConnection connection;

    public ServiceInfoDetailPanel(Application application, ServiceInfo serviceInfo)
    {
        this.application = application;
        this.serviceInfo = serviceInfo;
    }

    public void init()
    {
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setLayout(new BorderLayout());

        Summary summary = new Summary();
        summary.init();
        add(summary, BorderLayout.NORTH);

        connection = createMBeanServerConnection();

        JettySummary jetty = new JettySummary();
        jetty.init();
        add(jetty, BorderLayout.CENTER);
    }

    private MBeanServerConnection createMBeanServerConnection()
    {
        return (MBeanServerConnection)application.post(new Callable()
        {
            public Object call() throws Exception
            {
                JMXServiceURL jmxServiceURL = new JMXServiceURL(serviceInfo.getServiceURL().toString());
                JMXConnector connector = JMXConnectorFactory.connect(jmxServiceURL);
                connector.addConnectionNotificationListener(ServiceInfoDetailPanel.this, null, connector);
                return connector.getMBeanServerConnection();
            }
        });
    }

    public void handleNotification(final Notification notification, final Object handback)
    {
        EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                if (notification.getType().equals(JMXConnectionNotification.CLOSED) || notification.getType().equals(JMXConnectionNotification.FAILED))
                {
                    System.out.println("notification = " + notification);
                    JMXConnector connector = (JMXConnector)handback;
                    try
                    {
                        connector.close();
                    }
                    catch (IOException ignored)
                    {
                    }
                    connection = createMBeanServerConnection();
                }
            }
        });
    }

    public ServiceInfo getServiceInfo()
    {
        return serviceInfo;
    }

    private class Summary extends JPanel
    {
        public void init()
        {
            setBorder(new TitledBorder(" Service Summary "));
            setLayout(new FormLayout("5dlu, pref, 4dlu, pref, 5dlu", "5dlu, pref, 4dlu, pref, 5dlu"));
            CellConstraints cc = new CellConstraints();

            JLabel hostAddressLabel = new JLabel("Host Address:");
            add(hostAddressLabel, cc.xy(2, 2));
            JLabel hostAddress = new JLabel((String)serviceInfo.getAttributes().getValue("hostAddress"));
            add(hostAddress, cc.xy(4, 2));

            JLabel nameLabel = new JLabel("Name:");
            add(nameLabel, cc.xy(2, 4));
            JLabel name = new JLabel((String)serviceInfo.getAttributes().getValue("name"));
            add(name, cc.xy(4, 4));
        }
    }

    private class JettySummary extends JPanel
    {
        private JLabel status;
        private JLabel statusIcon;
        private JLabel port;

        public void init()
        {
            setBorder(new TitledBorder(" Jetty Summary "));
            setLayout(new FormLayout("5dlu, pref, 4dlu, pref, 4dlu, pref, 10dlu, pref, 5dlu", "5dlu, pref, 4dlu, pref, 5dlu"));

            CellConstraints cc = new CellConstraints();
            JLabel statusLabel = new JLabel("Running:");
            add(statusLabel, cc.xy(2, 2));
            status = new JLabel();
            add(status, cc.xy(4, 2));
            statusIcon = new JLabel();
            add(statusIcon, cc.xy(6, 2));
            JLabel restart = new JLabel("<html><u>Restart</u></html>");
            add(restart, cc.xy(8, 2));
            restart.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            restart.addMouseListener(new MouseAdapter()
            {
                public void mouseClicked(MouseEvent e)
                {
                    if (SwingUtilities.isLeftMouseButton(e))
                    {
                        restartJetty();
                    }
                }
            });

            JLabel portLabel = new JLabel("Listening on port(s):");
            add(portLabel, cc.xy(2, 4));
            port = new JLabel();
            add(port, cc.xy(4, 4));
            JLabel changePort = new JLabel("<html><u>Change Port</u></html>");
            add(changePort, cc.xy(8, 4));
            changePort.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            changePort.addMouseListener(new MouseAdapter()
            {
                public void mouseClicked(MouseEvent e)
                {
                    if (SwingUtilities.isLeftMouseButton(e))
                    {
                        changePort();
                    }
                }
            });

            update();
        }

        public void update()
        {
            Boolean statusValue = (Boolean)application.getAttribute(connection, "org.mortbay.jetty:type=server,id=0", "running");
            status.setText(statusValue.toString());
            statusIcon.setIcon(statusValue.booleanValue() ? application.getIcon("images/button_ok.png") : application.getIcon("images/button_cancel.png"));

            StringBuffer portValue = new StringBuffer();
            ObjectName[] connectorObjectNames = (ObjectName[])application.getAttribute(connection, "org.mortbay.jetty:type=server,id=0", "connectors");
            for (int i = 0; i < connectorObjectNames.length; ++i)
            {
                ObjectName connectorObjectName = connectorObjectNames[i];
                Integer port = (Integer)application.getAttribute(connection, connectorObjectName.toString(), "port");
                if (portValue.length() > 0) portValue.append(", ");
                portValue.append(port);
            }
            port.setText(portValue.toString());
        }

        private void restartJetty()
        {
            int result = JOptionPane.showConfirmDialog(application, "Are you sure you want to restart Jetty ?", "Confirm Restart ?", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (result == JOptionPane.YES_OPTION)
            {
                application.invoke(connection, "org.mortbay.jetty:type=server,id=0", "stop");
                update();
                application.flushEvents();
                application.invoke(connection, "org.mortbay.jetty:type=server,id=0", "start");
                update();
            }
        }

        private void changePort()
        {
            int result = JOptionPane.showConfirmDialog(application, "Changing port requires Jetty restart. Continue ?", "Confirm Port Change", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (result == JOptionPane.YES_OPTION)
            {
                String portString = JOptionPane.showInputDialog(application, "Insert new port:", "Insert Port", JOptionPane.QUESTION_MESSAGE);
                Integer port = Integer.valueOf(portString);
                application.invoke(connection, "org.mortbay.jetty:type=server,id=0", "stop");
                update();
                application.flushEvents();
                ObjectName connectorObjectName = ((ObjectName[])application.getAttribute(connection, "org.mortbay.jetty:type=server,id=0", "connectors"))[0];
                application.setAttribute(connection, connectorObjectName.toString(), "port", port);
                application.invoke(connection, "org.mortbay.jetty:type=server,id=0", "start");
                update();
            }
        }
    }
}
