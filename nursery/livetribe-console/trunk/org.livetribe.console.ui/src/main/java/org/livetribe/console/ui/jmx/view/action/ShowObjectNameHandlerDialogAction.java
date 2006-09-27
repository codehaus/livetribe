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

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.livetribe.console.ui.Activator;
import org.livetribe.console.ui.jmx.view.ObjectNameHandlerDialog;
import org.livetribe.console.ui.jmx.view.ObjectNameView;

/**
 * @version $Revision$ $Date$
 */
public class ShowObjectNameHandlerDialogAction implements IViewActionDelegate
{
    private ObjectNameView view;

    public void init(IViewPart view)
    {
        this.view = (ObjectNameView)view;
    }

    public void run(IAction action)
    {
        ObjectNameHandlerDialog dialog = new ObjectNameHandlerDialog(view.getSite().getShell());
        Activator.getDefault().getContainer().resolve(dialog);
        dialog.open();
        view.refresh();
    }

    public void selectionChanged(IAction action, ISelection selection)
    {
    }
}
