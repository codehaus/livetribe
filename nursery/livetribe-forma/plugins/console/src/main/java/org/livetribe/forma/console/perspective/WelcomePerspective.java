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

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.livetribe.forma.console.action.SLPNetworkScanAction;
import org.livetribe.forma.console.action.ScanSLPAndDisplayAction;
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

//    @PostConstruct
    private void initComponents2()
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

    @PostConstruct
    private void initComponents()
    {
        setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        setLayout(new BorderLayout(50, 50));

        JPanel welcomePanel = createWelcomePanel();
        welcomePanel.setPreferredSize(new Dimension(0, 150));
        add(welcomePanel, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(1, 0, 50, 50));
        add(center, BorderLayout.CENTER);

        JPanel scanPanel = createScanPanel();
        center.add(scanPanel);

        JPanel connectPanel = createConnectPanel();
        center.add(connectPanel);
    }

    private JPanel createWelcomePanel()
    {
        return new WelcomePanel();
    }

    private JPanel createScanPanel()
    {
        ScanPanel scanPanel = new ScanPanel();
        scanPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        FormLayout layout = new FormLayout("pref, 5dlu, left:default", "pref");
        scanPanel.setLayout(layout);

        URL iconURL = getClass().getClassLoader().getResource(bundle.get("perspective.welcome.slp.scan.button.icon"));
        Icon icon = null;
        if (iconURL != null) icon = new ImageIcon(iconURL);
        final JButton slpScanButton = new JButton(icon);
        slpScanButton.setFocusPainted(false);
        Action action = actionManager.getAction(ScanSLPAndDisplayAction.ID, null);
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
        scanPanel.add(slpScanButton, cc.xy(1, 1));
        scanPanel.add(slpScanLabel, cc.xy(3, 1));

        return scanPanel;
    }

    private JPanel createConnectPanel()
    {
        return new ConnectPanel();
    }

    public String getPerspectiveId()
    {
        return ID;
    }

    public void spiDisplay(Part part, Object constraint)
    {
        throw new AssertionError("Children components are not supported by this class: " + getClass().getName());
    }

    private class WelcomePanel extends JPanel
    {
        @Override
        protected void paintComponent(Graphics g)
        {
            Graphics2D g2d = (Graphics2D)g;
            int width = getWidth();
            int height = getHeight();

            // Background, gradient from top to bottom
//            GradientPaint gradient = new GradientPaint(0, 0, Color.BLUE, 0, height, Color.WHITE);
//            g2d.setPaint(gradient);
//            g2d.fillRect(0, 0, width, height);

            // Welcome text, white
            String text = "WELCOME";
            Font font = new Font("Arial", Font.BOLD, 40);
            FontMetrics metrics = g2d.getFontMetrics(font);
            int fontHeight = metrics.getHeight();
            int textWidth = metrics.stringWidth(text);
            g2d.setFont(font);
//            g2d.setColor(Color.WHITE);
            g2d.drawString(text, (width - textWidth) / 2, (height - fontHeight) / 2);
        }
    }

    private class ScanPanel extends JPanel
    {
    }

    private class ConnectPanel extends JPanel
    {
    }
}
