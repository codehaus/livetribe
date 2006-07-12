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

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.livetribe.forma.console.action.DisplayJMXServiceNodeAction;
import org.livetribe.forma.console.action.SLPNetworkScanAction;
import org.livetribe.forma.console.model.service.JMXServiceNode;
import org.livetribe.forma.ui.Context;
import org.livetribe.forma.ui.Part;
import org.livetribe.forma.ui.action.ActionCommand;
import org.livetribe.forma.ui.action.ActionManager;
import org.livetribe.forma.ui.feedback.Feedback;
import org.livetribe.forma.ui.feedback.FeedbackManager;
import org.livetribe.forma.ui.frame.Frame;
import org.livetribe.forma.ui.frame.FrameManager;
import org.livetribe.forma.ui.perspective.AbstractPerspective;
import org.livetribe.ioc.Inject;
import org.livetribe.ioc.PostConstruct;

/**
 * @version $Rev$ $Date$
 */
public class ServicesSummaryPerspective extends AbstractPerspective
{
    public static final String ID = ServicesSummaryPerspective.class.getName();

    @Inject
    private ActionManager actionManager;
    @Inject
    private FrameManager frameManager;
    @Inject
    private FeedbackManager feedbackManager;
    private Preferences preferences;

    public String getPerspectiveId()
    {
        return ID;
    }

    @PostConstruct
    private void initComponents()
    {
        // TODO: initialize the borders, etc; load of the UI is done in spiLoad
        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        setBackground(Color.WHITE);
    }

    public void spiDisplay(Part part, Object constraint)
    {
        // TODO:
    }

    @Override
    public void spiLoad(Context context)
    {
        preferences = Preferences.userNodeForPackage(getClass()).node(getClass().getSimpleName());
        // TODO: load perspective configuration for views and editors, and eventually for other things
    }

    @Override
    public void spiOpen(Context context)
    {
        Context newContext = context == null ? new Context() : context;

        ActionCommand scan = actionManager.getActionCommand(SLPNetworkScanAction.ID);
        scan.setContext(newContext);
        Frame frame = frameManager.getCurrentFrame();
        Feedback feedback = feedbackManager.showWaitCursor(frame.getJFrame().getRootPane());
        try
        {
            scan.execute();
        }
        finally
        {
            feedback.stop();
        }





        List<JMXServiceNode> jmxServiceNodes = (List<JMXServiceNode>)newContext.get(SLPNetworkScanAction.RESULT);
        List<JMXServiceNodePanel> panels = createJMXServiceNodePanels(jmxServiceNodes);
        for (JMXServiceNodePanel panel : panels) add(panel);
    }

    private List<JMXServiceNodePanel> createJMXServiceNodePanels(List<JMXServiceNode> nodes)
    {
        List<JMXServiceNodePanel> result = new ArrayList<JMXServiceNodePanel>();
        for (JMXServiceNode node : nodes) result.add(new JMXServiceNodePanel(node));
        return result;
    }

    private class JMXServiceNodePanel extends JPanel
    {
        private final JMXServiceNode serviceNode;
        private final Dimension dimension = new Dimension(200, 150);

        private JMXServiceNodePanel(JMXServiceNode serviceNode)
        {
            this.serviceNode = serviceNode;

            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBackground(new Color(0xFFFFDD));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setPreferredSize(dimension);
            setMaximumSize(dimension);

            add(new JLabel(serviceNode.getJMXServiceURL().toString()));
            addMouseListener(new MouseAdapter()
            {
                @Override
                public void mouseClicked(MouseEvent e)
                {
                    if (SwingUtilities.isLeftMouseButton(e))
                    {
                        Context context = new Context();
                        context.put(DisplayJMXServiceNodeAction.JMX_SERVICE_NODE_KEY, JMXServiceNodePanel.this.serviceNode);
                        ActionCommand actionCommand = actionManager.getActionCommand(DisplayJMXServiceNodeAction.ID);
                        actionCommand.setContext(context);
                        actionCommand.execute();
                    }
                }
            });
        }
    }
}
