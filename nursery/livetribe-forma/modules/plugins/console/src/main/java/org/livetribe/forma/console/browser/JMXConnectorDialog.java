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
package org.livetribe.forma.console.browser;

import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.io.IOException;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.swing.JDialog;
import javax.swing.JLabel;

import org.livetribe.forma.i18n.Bundle;
import org.livetribe.forma.i18n.InternationalizationManager;
import org.livetribe.forma.ui.action.ActionContext;
import org.livetribe.ioc.Inject;
import org.livetribe.ioc.PostConstruct;
import org.livetribe.slp.api.sa.ServiceInfo;

/**
 * @version $Rev$ $Date$
 */
public class JMXConnectorDialog extends JDialog
{
    private Bundle bundle;
    private final ActionContext context;
    private JMXConnector connector;

    public JMXConnectorDialog(Frame owner, ActionContext context) throws HeadlessException
    {
        super(owner, true);
        this.context = context;
    }

    @Inject
    public void setTranslator(InternationalizationManager translator)
    {
        this.bundle = translator.getBundle("Console", null);
    }

    @PostConstruct
    private void initComponents()
    {
        setTitle(bundle.get("connector.dialog.connecting.title"));

        JLabel label = new JLabel(bundle.get("connector.dialog.connecting.text"));
        add(label);

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(getOwner());
        pack();
    }

    public JMXConnector getJMXConnector()
    {
        return connector;
    }

    public void display()
    {
        EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                connect();
            }
        });
        setVisible(true);
    }

    private void undisplay()
    {
        setVisible(false);
    }

    private void connect()
    {
        try
        {
            ServiceInfo model = (ServiceInfo)context.get(ManageableNodePanel.MODEL_KEY);
            JMXServiceURL jmxServiceURL = new JMXServiceURL(model.getServiceURL().getURL());
            connector = JMXConnectorFactory.connect(jmxServiceURL);
            undisplay();
        }
        catch (IOException x)
        {
            throw new RuntimeException(x);
        }
    }
}
