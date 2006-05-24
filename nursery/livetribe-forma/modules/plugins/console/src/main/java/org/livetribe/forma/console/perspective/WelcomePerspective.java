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
package org.livetribe.forma.console.perspective;

import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.livetribe.forma.console.action.SLPNetworkScanAction;
import org.livetribe.forma.i18n.Bundle;
import org.livetribe.forma.i18n.InternationalizationManager;
import org.livetribe.forma.ui.Part;
import org.livetribe.forma.ui.action.Action;
import org.livetribe.forma.ui.action.ActionManager;
import org.livetribe.forma.ui.perspective.AbstractPerspective;
import org.livetribe.ioc.Inject;
import org.livetribe.ioc.PostConstruct;

/**
 * @version $Rev$ $Date$
 */
public class WelcomePerspective extends AbstractPerspective
{
    public static final String ID = WelcomePerspective.class.getName();

    @Inject private ActionManager actionManager;
    private Bundle bundle;

    @Inject
    public void setTranslator(InternationalizationManager translator)
    {
        this.bundle = translator.getBundle("Console", null);
    }

    @PostConstruct
    private void initComponents()
    {
        setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        FormLayout layout = new FormLayout("pref, 5dlu, left:default", "pref");
        setLayout(layout);

        URL iconURL = getClass().getClassLoader().getResource(bundle.get("perspective.welcome.slp.scan.button.icon"));
        Icon icon = null;
        if (iconURL != null) icon = new ImageIcon(iconURL);
        final JButton slpScanButton = new JButton(icon);
        slpScanButton.setFocusPainted(false);
        Action action = actionManager.getAction(SLPNetworkScanAction.ID, null);
        slpScanButton.setToolTipText(action.getTooltip());
        slpScanButton.addActionListener(action);

        String slpScanText = bundle.get("perspective.welcome.slp.scan.label.text");
        JLabel slpScanLabel = new JLabel(slpScanText);
        slpScanLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        slpScanLabel.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                slpScanButton.doClick();
            }
        });

        CellConstraints cc = new CellConstraints();
        add(slpScanButton, cc.xy(1, 1));
        add(slpScanLabel, cc.xy(3, 1));
    }

    public String getPerspectiveId()
    {
        return ID;
    }

    public void spiDisplay(Part part)
    {
        throw new AssertionError("Children components are not supported by this class: " + getClass().getName());
    }
}
