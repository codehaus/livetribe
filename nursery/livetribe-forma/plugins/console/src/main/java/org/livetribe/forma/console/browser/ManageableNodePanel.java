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

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.livetribe.forma.console.action.ManageNodeAction;
import org.livetribe.forma.i18n.Bundle;
import org.livetribe.forma.i18n.InternationalizationManager;
import org.livetribe.forma.ui.Context;
import org.livetribe.forma.ui.action.ActionCommand;
import org.livetribe.forma.ui.action.ActionManager;
import org.livetribe.forma.ui.popup.PopupManager;
import org.livetribe.ioc.Inject;
import org.livetribe.ioc.PostConstruct;
import org.livetribe.slp.Attributes;
import org.livetribe.slp.api.sa.ServiceInfo;

/**
 * @version $Rev$ $Date$
 */
public class ManageableNodePanel extends JPanel
{
    public static final String POPUP_ID = ManageableNodePanel.class.getName() + ".popup";
    public static final String MODEL_KEY = ManageableNodePanel.class.getName() + ".model";

    @Inject private ActionManager actionManager;
    @Inject private PopupManager popupManager;
    private final ServiceInfo serviceInfo;
    private Bundle bundle;

    public ManageableNodePanel(ServiceInfo serviceInfo)
    {
        this.serviceInfo = serviceInfo;
    }

    @Inject
    public void setTranslator(InternationalizationManager translator)
    {
        this.bundle = translator.getBundle("Console", null);
    }

    @PostConstruct
    private void initComponents()
    {
        setOpaque(false);
        setLayout(new FormLayout("center:48dlu", "48dlu, 10dlu, pref, 10dlu, pref"));
        CellConstraints cc = new CellConstraints();

        JLabel icon = new JLabel();
        add(icon, cc.xy(1, 1));
        String iconPath = bundle.get("manageable.node.icon");
        if (iconPath != null)
        {
            URL iconURL = getClass().getClassLoader().getResource(iconPath);
            if (iconURL != null) icon.setIcon(new ImageIcon(iconURL));
        }

        JLabel url = new JLabel();
        add(url, cc.xy(1, 3));
        url.setText(serviceInfo.getServiceURL().getURL());

        JLabel attribs = new JLabel();
        attribs.setOpaque(true);
        attribs.setBackground(Color.GREEN);
        add(attribs, cc.xy(1, 5));
        Attributes attributes = serviceInfo.getAttributes();
        if (attributes != null) attribs.setText(attributes.asString());

        addMouseListener(new ManageableNodeMouseListener());
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private class ManageableNodeMouseListener extends MouseAdapter
    {
        @Override
        public void mouseClicked(MouseEvent e)
        {
            Context context = new Context();
            context.put(MODEL_KEY, serviceInfo);

            if (SwingUtilities.isLeftMouseButton(e))
            {
                ActionCommand actionCommand = actionManager.getActionCommand(ManageNodeAction.ID);
                actionCommand.setContext(context);
                actionCommand.execute();
            }
            else if (SwingUtilities.isRightMouseButton(e))
            {
                popupManager.displayPopupMenu(POPUP_ID, context, e.getComponent(), e.getX(), e.getY());
            }
        }
    }
/*
    private class NetworkServiceMouseListener extends MouseAdapter
    {
        @Override
        public void mouseClicked(MouseEvent e)
        {
            JLabel label = (JLabel)e.getSource();
            String jmxServiceURL = label.getText();

            Map<String, Object> context = new HashMap<String, Object>();
            context.put("jmxServiceURL", jmxServiceURL);

            if (SwingUtilities.isRightMouseButton(e))
            {
                popupManager.displayPopupMenu(SLPNetworkPanel.POPUP_ID, context, e.getComponent(), e.getX(), e.getY());
            }
            else if (SwingUtilities.isLeftMouseButton(e))
            {
                Action action = actionManager.getAction(ManageNodeAction.ID, context);
                ActionEvent actionEvent = new ActionEvent(e.getSource(), ActionEvent.ACTION_PERFORMED, action.getCommand(), System.currentTimeMillis(), e.getModifiersEx());
                action.actionPerformed(actionEvent);
            }
        }
    }
*/
}
