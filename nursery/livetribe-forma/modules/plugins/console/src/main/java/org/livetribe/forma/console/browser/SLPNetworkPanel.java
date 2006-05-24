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

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.livetribe.forma.i18n.Bundle;
import org.livetribe.forma.i18n.InternationalizationManager;
import org.livetribe.forma.ui.action.ActionManager;
import org.livetribe.forma.ui.popup.PopupManager;
import org.livetribe.ioc.Container;
import org.livetribe.ioc.Inject;
import org.livetribe.ioc.PostConstruct;
import org.livetribe.slp.api.sa.ServiceInfo;

/**
 * @version $Rev$ $Date$
 */
public class SLPNetworkPanel extends JPanel
{
    @Inject private Container containerManager;
    @Inject private PopupManager popupManager;
    @Inject private ActionManager actionManager;
    private Bundle bundle;
    private JPanel nodesPanel;
    private JLabel emptyNetworkLabel;

    @Inject
    public void setTranslator(InternationalizationManager translator)
    {
        this.bundle = translator.getBundle("Console", null);
    }

    @PostConstruct
    private void initComponents()
    {
        nodesPanel = new JPanel();
        emptyNetworkLabel = new JLabel(bundle.get("network.empty.text"));

        setBackground(SystemColor.WHITE);
        setLayout(new BorderLayout());
        add(nodesPanel, BorderLayout.NORTH);
    }

    public void display(List<ServiceInfo> services)
    {
        if (services == null || services.isEmpty())
        {
            nodesPanel.removeAll();
            nodesPanel.setLayout(new GridBagLayout());
            nodesPanel.add(emptyNetworkLabel);
        }
        else
        {
            nodesPanel.removeAll();
            nodesPanel.setLayout(new GridBagLayout());
            for (int i = 0; i < services.size(); ++i)
            {
                ServiceInfo service = services.get(i);
                ManageableNodePanel manageableNode = new ManageableNodePanel(service);
                containerManager.resolve(manageableNode);

                GridBagConstraints gbc = new GridBagConstraints();
                gbc.gridx = i % 4;
                gbc.gridy = i / 4;
                gbc.insets = new Insets(20, 20, 0, 0);
                nodesPanel.add(manageableNode, gbc);
            }
        }
    }
}
