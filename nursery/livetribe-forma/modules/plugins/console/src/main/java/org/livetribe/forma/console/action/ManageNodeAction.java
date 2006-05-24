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
package org.livetribe.forma.console.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.net.URISyntaxException;
import javax.management.remote.JMXConnector;

import org.livetribe.forma.console.browser.JMXConnectorDialog;
import org.livetribe.forma.console.browser.ManageableNodePanel;
import org.livetribe.forma.console.editor.JMXEditor;
import org.livetribe.forma.i18n.Bundle;
import org.livetribe.forma.i18n.InternationalizationManager;
import org.livetribe.forma.ui.action.ActionContext;
import org.livetribe.forma.ui.action.ActionContextAware;
import org.livetribe.forma.ui.editor.EditorManager;
import org.livetribe.forma.ui.frame.Frame;
import org.livetribe.forma.ui.frame.FrameManager;
import org.livetribe.forma.ui.perspective.PerspectiveManager;
import org.livetribe.ioc.Container;
import org.livetribe.ioc.Inject;
import org.livetribe.slp.api.sa.ServiceInfo;

/**
 * @version $Rev$ $Date$
 */
public class ManageNodeAction implements ActionListener, ActionContextAware
{
    public static final String ID = ManageNodeAction.class.getName();

    @Inject Container containerManager;
    @Inject FrameManager frameManager;
    @Inject PerspectiveManager perspectiveManager;
    @Inject EditorManager editorManager;
    private Bundle bundle;
    private ActionContext context;

    @Inject
    public void setTranslator(InternationalizationManager translator)
    {
        this.bundle = translator.getBundle("Console", null);
    }

    public void spiSetActionContext(ActionContext context)
    {
        this.context = context;
    }

    public void actionPerformed(ActionEvent e)
    {
        Frame frame = frameManager.getCurrentFrame();
        JMXConnectorDialog dialog = new JMXConnectorDialog(frame.getJFrame(), context);
        containerManager.resolve(dialog);
        dialog.display();

        JMXConnector connector = dialog.getJMXConnector();
        if (connector == null) return;
        context.put(JMXEditor.JMXCONNECTOR_KEY, connector);

        ServiceInfo model = (ServiceInfo)context.get(ManageableNodePanel.MODEL_KEY);
        URI uri = newURI(model.getServiceURL().toString());
//        Perspective perspective = perspectiveManager.displayPerspective(ManagementPerspective.ID, frame, context);
//        editorManager.displayEditor(uri, context, perspective);
    }

    private URI newURI(String uri)
    {
        try
        {
            return new URI(uri);
        }
        catch (URISyntaxException x)
        {
            throw new RuntimeException(x);
        }
    }
}
