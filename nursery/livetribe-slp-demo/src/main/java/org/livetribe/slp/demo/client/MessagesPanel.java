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
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * @version $Revision$ $Date$
 */
public class MessagesPanel extends JPanel
{
    public void init()
    {
        setLayout(new BorderLayout());

        Toolbar toolbar = new Toolbar();
        toolbar.init();
        add(toolbar, BorderLayout.NORTH);

        Area area = new Area();
        area.init();
        add(area, BorderLayout.CENTER);
    }

    private class Toolbar extends JPanel
    {
        public void init()
        {
            setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));
            JButton clear = new JButton("Clear");
            add(clear);
        }
    }

    private class Area extends JPanel
    {
        public void init()
        {
            setLayout(new BorderLayout());
            JTextArea textArea = new JTextArea();
            add(textArea, BorderLayout.CENTER);
        }
    }
}
