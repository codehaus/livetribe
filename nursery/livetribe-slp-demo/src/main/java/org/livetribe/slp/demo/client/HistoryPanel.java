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
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.livetribe.slp.ServiceInfo;
import org.livetribe.slp.api.ServiceRegistrationEvent;

/**
 * @version $Revision$ $Date$
 */
public class HistoryPanel extends JPanel implements Observer
{
    private final History history = new History();
    private final Application application;
    private Toolbar toolbar;
    private Content content;

    public HistoryPanel(Application application)
    {
        this.application = application;
    }

    public void init()
    {
        setLayout(new BorderLayout());

        toolbar = new Toolbar();
        toolbar.init();
        add(toolbar, BorderLayout.NORTH);

        content = new Content();
        content.init();
        add(content, BorderLayout.CENTER);
    }

    public void showPanel(Component component)
    {
        if (!history.contains(component))
        {
            history.add(component);
            history.moveTo(history.indexOf(component));
        }
        content.show(component);
        toolbar.update();
    }

    public void update(Observable source, Object event)
    {
        ServiceRegistrationEvent registrationEvent = (ServiceRegistrationEvent)event;
        if (registrationEvent.getCurrentServiceInfo() == null)
        {
            ServiceInfo removed = registrationEvent.getPreviousServiceInfo();
            Component current = (Component)history.getCurrent();
            if (current instanceof ServiceInfoDetailPanel)
            {
                if (((ServiceInfoDetailPanel)current).getServiceInfo().getKey().equals(removed.getKey()))
                {
                    history.remove();
                    application.showPanel((Component)history.getCurrent());
                }
            }
        }
    }

    private class Toolbar extends JPanel implements ActionListener
    {
        private JButton back;
        private JButton forward;

        public void init()
        {
            setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));

            back = createButton("images/back.png");
            add(back);
            back.addActionListener(this);

            forward = createButton("images/forward.png");
            add(forward);
            forward.addActionListener(this);

            update();
        }

        public JButton createButton(String iconPath)
        {
            ToolbarButton button = new ToolbarButton();
            button.init();
            button.setIcon(application.getIcon(iconPath));
            return button;
        }

        public void actionPerformed(ActionEvent e)
        {
            Object source = e.getSource();
            if (source == back)
            {
                history.moveBackward();
                application.showPanel((Component)history.getCurrent());
            }
            else if (source == forward)
            {
                history.moveForward();
                application.showPanel((Component)history.getCurrent());
            }
            update();
        }

        private void update()
        {
            back.setEnabled(history.canMoveBackward());
            forward.setEnabled(history.canMoveForward());
        }
    }

    private class Content extends JPanel
    {
        public void init()
        {
            setLayout(new BorderLayout());
        }

        public void show(Component component)
        {
            removeAll();
            add(component, BorderLayout.CENTER);
            revalidate();
            repaint();
        }
    }

    private class ToolbarButton extends JButton implements MouseListener
    {
        public void init()
        {
            setFocusPainted(false);
            setBorderPainted(false);
            addMouseListener(this);
        }

        public void mouseClicked(MouseEvent e)
        {
        }

        public void mouseEntered(MouseEvent e)
        {
            setBorderPainted(true);
            repaint();
        }

        public void mouseExited(MouseEvent e)
        {
            setBorderPainted(false);
            repaint();
        }

        public void mousePressed(MouseEvent e)
        {
        }

        public void mouseReleased(MouseEvent e)
        {
        }
    }
}
