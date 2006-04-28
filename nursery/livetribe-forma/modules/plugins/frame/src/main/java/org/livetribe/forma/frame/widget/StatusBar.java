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
package org.livetribe.forma.frame.widget;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.UIManager;
import javax.swing.UIDefaults;
import javax.swing.border.Border;

import org.livetribe.ioc.PostConstruct;

/**
 * @version $Rev$ $Date$
 */
public class StatusBar extends JPanel
{
    private JLabel label;
    private JPanel panel;

    @PostConstruct
    private void initComponents()
    {
        setLayout(new BorderLayout());

        label = new JLabel();
        add(label, BorderLayout.CENTER);

        panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 2, 0));
        add(panel, BorderLayout.EAST);

        UIDefaults uiDefaults = UIManager.getLookAndFeelDefaults();
        Border inner = BorderFactory.createMatteBorder(1, 0, 1, 0, uiDefaults.getColor("control"));
        Border outer = BorderFactory.createMatteBorder(1, 0, 1, 0, uiDefaults.getColor("controlShadow"));
        Border border = BorderFactory.createCompoundBorder(outer, inner);
        setBorder(border);
    }

    public void setText(String text)
    {
        label.setText(text);
    }

    public void append(JComponent component)
    {
        panel.add(component, 0);
        revalidate();
    }

    public void appendSeparator()
    {
        append(new JSeparator(JSeparator.VERTICAL));
    }
}
