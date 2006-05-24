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
package org.livetribe.forma.console.editor;

import java.awt.Component;
import java.net.URI;
import javax.management.remote.JMXConnector;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.livetribe.forma.ui.PartContainer;
import org.livetribe.forma.ui.action.ActionContext;
import org.livetribe.forma.ui.editor.Editor;
import org.livetribe.ioc.PostConstruct;

/**
 * @version $Rev$ $Date$
 */
public class JMXEditor extends JPanel implements Editor
{
    public static final String JMXCONNECTOR_KEY = JMXEditor.class.getName() + ".jmxconnector";

    private PartContainer container;

    @PostConstruct
    private void initComponents()
    {
        JLabel label = new JLabel("JMXEDITOR");
        add(label);
    }

    public Component spiGetComponent()
    {
        return this;
    }

    public void spiDisplayIn(PartContainer partContainer)
    {
        this.container = partContainer;
        container.spiDisplay(this);
    }

    public void spiUndisplay()
    {
        if (container == null) return;
        container.spiDisplay(null);
        container = null;
    }

    public void spiOpen(URI uri, ActionContext context)
    {
        JMXConnector connector = (JMXConnector)context.get(JMXCONNECTOR_KEY);

    }
}
