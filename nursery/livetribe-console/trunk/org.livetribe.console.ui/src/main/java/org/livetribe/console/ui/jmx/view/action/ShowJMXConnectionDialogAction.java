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
package org.livetribe.console.ui.jmx.view.action;

import java.io.IOException;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.livetribe.console.ui.Activator;
import org.livetribe.console.ui.jmx.JMXConnectorInfo;
import org.livetribe.console.ui.jmx.JMXConnectorManager;
import org.livetribe.console.ui.jmx.view.JMXConnectionDialog;
import org.livetribe.ioc.Inject;

/**
 * @version $Revision: 157 $ $Date$
 */
public class ShowJMXConnectionDialogAction implements IWorkbenchWindowActionDelegate
{
    @Inject
    private JMXConnectorManager jmxConnectorManager;
    private IWorkbenchWindow window;

    public void init(IWorkbenchWindow window)
    {
        this.window = window;
        Activator.getDefault().getContainer().resolve(this);
    }

    public void run(IAction action)
    {
        JMXConnectionDialog dialog = new JMXConnectionDialog(window.getShell());
        int result = dialog.open();
        if (result != JMXConnectionDialog.OK) return;
        JMXConnectorInfo jmxConnectorInfo = dialog.getJMXConnectorInfo();
        try
        {
            jmxConnectorManager.add(jmxConnectorInfo);
            jmxConnectorManager.connect(jmxConnectorInfo);
        }
        catch (IOException x)
        {
            // Could not connect, mark as non connected
            jmxConnectorInfo.setConnected(false);
        }
    }

    public void selectionChanged(IAction action, ISelection selection)
    {
    }

    public void dispose()
    {
    }
}
