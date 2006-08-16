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

import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.livetribe.slp.ServiceInfo;

/**
 * @version $Revision$ $Date$
 */
public class ServiceInfoPanel extends JPanel
{
    private final Application application;
    private final ServiceInfo serviceInfo;

    public ServiceInfoPanel(Application application, ServiceInfo serviceInfo)
    {
        this.application = application;
        this.serviceInfo = serviceInfo;
    }

    public void init()
    {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel iconLabel = new JLabel(application.getIcon("images/server.png"));
        add(iconLabel);

        add(Box.createVerticalStrut(10));

        String address = (String)serviceInfo.getAttributes().getValue("hostAddress");
        JLabel addressLabel = new JLabel(address);
        add(addressLabel);

        add(Box.createVerticalStrut(10));

        String name = (String)serviceInfo.getAttributes().getValue("name");
        JLabel nameLabel = new JLabel(name);
        add(nameLabel);

        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addMouseListener(new MouseEventListener());
    }

    private class MouseEventListener extends MouseAdapter
    {
        public void mouseClicked(MouseEvent e)
        {
            if (SwingUtilities.isLeftMouseButton(e))
            {
                ServiceInfoDetailPanel detail = new ServiceInfoDetailPanel(application, serviceInfo);
                detail.init();
                application.showPanel(detail);
            }
        }
    }
}
