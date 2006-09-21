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

import java.util.List;

import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;
import org.livetribe.console.ui.Activator;
import org.livetribe.console.ui.jmx.IObjectNameHandler;
import org.livetribe.console.ui.jmx.JMXNode;
import org.livetribe.ioc.Inject;

/**
 * @version $Revision$ $Date$
 */
public class ObjectNameView extends ViewPart
{
    public static final String ID = ObjectNameView.class.getName();
    private static final Object[] EMPTY_DATA = new Object[0];

    @Inject
    private ObjectNameHandlerManager objectNameHandlerManager;
    private TreeViewer view;
    private ISelectionListener listener;
    
    @Override
    public void createPartControl(Composite parent)
    {
        Activator.getDefault().getContainer().resolve(this);

        initPullDownMenu();
    	
        Tree tree = new Tree(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        view = new TreeViewer(tree);

        ObjectNameContentProvider data = new ObjectNameContentProvider();
        view.setContentProvider(data);
        
        List<IObjectNameHandler> handlers = objectNameHandlerManager.getObjectNameHandlers();
        handlers.add(new JMXObjectNameHandler());
        for (IObjectNameHandler handler : handlers) view.addFilter(new ObjectNameViewerFilter(handler));
        
        ObjectNameLabelProvider labels = new ObjectNameLabelProvider(handlers);
        view.setLabelProvider(labels);
        
        getSite().setSelectionProvider(view);
        listener = new DiscoveryListener();
        getSite().getPage().addPostSelectionListener(JMXDiscoveryView.ID, listener);
    }

    private void initPullDownMenu() 
    {
		IMenuManager menu = getViewSite().getActionBars().getMenuManager();
        menu.add(new GroupMarker("content"));
        menu.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	@Override
    public void dispose()
    {
        super.dispose();
        getSite().getPage().removePostSelectionListener(JMXDiscoveryView.ID, listener);
    }

    @Override
    public void setFocus()
    {
        view.getControl().setFocus();
    }
    
    private class ObjectNameContentProvider implements ITreeContentProvider
    {
        private Object[] data;
        
        public Object[] getChildren(Object node)
        {
            return ((JMXNode)node).getChildren().toArray();
        }

        public Object getParent(Object node)
        {
            return ((JMXNode)node).getParent();
        }

        public boolean hasChildren(Object node)
        {
            return getChildren(node).length > 0;
        }

        public Object[] getElements(Object node)
        {
            return data;
        }

        public void dispose()
        {
        }

        public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
        {
            if (newInput == null) 
                this.data = EMPTY_DATA;
            else
                this.data = (Object[])newInput;
        }
    }
    
    private static class ObjectNameLabelProvider extends LabelProvider
    {
        private final List<IObjectNameHandler> handlers;
        
        public ObjectNameLabelProvider(final List<IObjectNameHandler> handlers)
        {
            this.handlers = handlers;
        }

        @Override
        public Image getImage(Object node)
        {
            return null;
        }

        @Override
        public String getText(Object element)
        {
            for (IObjectNameHandler handler : handlers)
            {
                Boolean accepted = handler.accepts(element);
                if (accepted == null) continue;
                if (accepted) return handler.getLabel(element);
            }
            return super.getText(element);
        }
    }
    
    private static class ObjectNameViewerFilter extends ViewerFilter
    {
        private final IObjectNameHandler handler;

        public ObjectNameViewerFilter(final IObjectNameHandler handler)
        {
            this.handler = handler;
        }

        @Override
        public boolean select(Viewer viewer, Object parent, Object element)
        {
            Boolean accepted = handler.accepts(element);
            if (accepted == null) return true;
            return accepted;
        }
    }

    private class DiscoveryListener implements ISelectionListener
    {
        public void selectionChanged(IWorkbenchPart part, ISelection selection)
        {
            Object[] input = ((IStructuredSelection)selection).toArray(); 
            view.setInput(input);
        }
    }
}
