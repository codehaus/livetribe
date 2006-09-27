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
package org.livetribe.console.ui.jmx.view;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;
import org.livetribe.console.ui.Activator;
import org.livetribe.console.ui.jmx.IMBeanViewContent;
import org.livetribe.console.ui.jmx.JMXNode;
import org.livetribe.console.ui.jmx.ObjectNameInfo;
import org.livetribe.console.ui.jmx.ObjectNameKeyInfo;
import org.livetribe.ioc.Container;
import org.livetribe.ioc.Inject;

/**
 * The default view that shows MBean information.
 * TODO: This class is a mediator that delegates to another mediator; see if it's possible to simplify. 
 * @version $Rev$ $Date$
 */
public class MBeanView extends ViewPart
{
    public static final String ID = MBeanView.class.getName();
    
    @Inject
    private MBeanViewContentManager mbeanViewContentManager;
    private Composite container;
    private ISelectionListener listener;
    private ObjectNameInfo currentSelection;
    private IMBeanViewContent mbeanViewContent;
    
    @Override
    public void createPartControl(Composite parent)
    {
        Activator.getDefault().getContainer().resolve(this);
        
        this.container = parent;
        this.listener = new Listener();
        getSite().getPage().addPostSelectionListener(ObjectNameView.ID, this.listener);
    }
    
    private void show(ObjectNameInfo current)
    {
        this.currentSelection = current;
        if (mbeanViewContent != null) mbeanViewContent.dispose();
        mbeanViewContent = mbeanViewContentManager.getMBeanViewContent(current.getObjectName());
        if (mbeanViewContent != null)
        {
            if (!mbeanViewContent.isInitialized()) mbeanViewContent.init(container);
            mbeanViewContent.show(current);
            container.layout();
        }
    }

    @Override
    public void dispose()
    {
        super.dispose();
        getSite().getPage().removePostSelectionListener(ObjectNameView.ID, listener);
    }

    @Override
    public void setFocus()
    {
    }
    
    private class Listener implements ISelectionListener
    {
        public void selectionChanged(IWorkbenchPart part, ISelection selection)
        {
            JMXNode current = (JMXNode)((IStructuredSelection)selection).getFirstElement();
            if (!(current instanceof ObjectNameInfo)) return;
            ObjectNameInfo newSelection = (ObjectNameInfo)current;
            if ((currentSelection == null) || (!currentSelection.getObjectName().equals(newSelection.getObjectName())))
            {
                show((ObjectNameInfo)current);
            }
        }
    }
}
