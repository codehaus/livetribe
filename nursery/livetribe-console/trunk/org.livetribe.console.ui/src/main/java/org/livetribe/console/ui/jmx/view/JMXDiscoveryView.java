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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.IPostSelectionProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.part.ViewPart;
import org.livetribe.console.ui.Activator;
import org.livetribe.console.ui.Messages;
import org.livetribe.console.ui.jmx.JMXConnectorInfo;
import org.livetribe.console.ui.jmx.JMXConnectorManager;
import org.livetribe.ioc.Inject;

/**
 * @version $Revision: 157 $ $Date$
 */
public class JMXDiscoveryView extends ViewPart implements JMXConnectorManager.Listener, IPostSelectionProvider
{
    public static final String ID = JMXDiscoveryView.class.getName();

    private final Map<JMXConnectorInfo, ElementView> elements = new HashMap<JMXConnectorInfo, ElementView>();
    private final List<ISelectionChangedListener> selectionListeners = new ArrayList<ISelectionChangedListener>();
    private final List<ISelectionChangedListener> postSelectionListeners = new ArrayList<ISelectionChangedListener>();
    @Inject
    private JMXConnectorManager jmxConnectorManager;
    private Image elementIcon;
    private Cursor elementCursor;
    private Composite container;
    private Menu popupMenu;
    private ISelection selection;
    
    @Override
    public void createPartControl(Composite parent)
    {
        Activator.getDefault().getContainer().resolve(this);
        
        elementIcon = Activator.getImageDescriptor(Messages.MENUITEM_EXIT_IMAGE).createImage(true);
        elementCursor = new Cursor(parent.getDisplay(), SWT.CURSOR_HAND);
        
        container = new Composite(parent, SWT.BORDER);
        container.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
        RowLayout layout = new RowLayout();
        layout.type = SWT.HORIZONTAL;
        layout.justify = false;
        layout.pack = false;
        layout.wrap = true;
        layout.marginTop = 10;
        layout.marginRight = 10;
        layout.marginBottom = 10;
        layout.marginLeft = 10;
        container.setLayout(layout);

        initPopupMenu(container);
        
        getSite().setSelectionProvider(this);
        
        jmxConnectorManager.addListener(this);
        final List<JMXConnectorInfo> infos = jmxConnectorManager.getJMXConnectorInfos();
        for (JMXConnectorInfo info : infos) createAndAddElementView(info);
        if (!infos.isEmpty()) setSelection(infos.get(0));
/*
        parent.getDisplay().asyncExec(new Runnable() 
        {
            public void run()
            {
                if (!infos.isEmpty()) setSelection(infos.get(0));
            }
        });
*/        
    }

    private void initPopupMenu(Composite parent)
    {
        MenuManager popupMenuManager = new MenuManager();
        popupMenuManager.add(new GroupMarker("content"));
        popupMenuManager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
        popupMenu = popupMenuManager.createContextMenu(parent);
        getSite().registerContextMenu(popupMenuManager, this);
    }

    @Override
    public void dispose()
    {
        super.dispose();
        jmxConnectorManager.removeListener(this);
        elementIcon.dispose();
        elementCursor.dispose();
    }

    @Override
    public void setFocus()
    {
    }
    
    private void setSelection(JMXConnectorInfo jmxConnectorInfo)
    {
        if (jmxConnectorInfo == null) setSelection(StructuredSelection.EMPTY);
        setSelection(new StructuredSelection(jmxConnectorInfo));
    }
    
    public void jmxConnectorAdded(final JMXConnectorManager.Event event)
    {
        container.getDisplay().asyncExec(new Runnable()
        {
            public void run()
            {
                JMXConnectorInfo info = event.getJMXConnectorInfo();
                ElementView elementView = createAndAddElementView(info);
                elementView.show(info);
                container.layout();
            }
        });
    }

    public void jmxConnectorChanged(final JMXConnectorManager.Event event)
    {
        container.getDisplay().asyncExec(new Runnable() 
        {
            public void run()
            {
                JMXConnectorInfo info = event.getJMXConnectorInfo();
                ElementView elementView = elements.get(info);
                if (elementView != null) elementView.show(info);
            }
        });
    }

    public void jmxConnectorRemoved(final JMXConnectorManager.Event event)
    {
        container.getDisplay().asyncExec(new Runnable()
        {
            public void run()
            {
                JMXConnectorInfo info = event.getJMXConnectorInfo();
                ElementView element = elements.remove(info);
                element.dispose();
            }
        });
    }
    
    private ElementView createAndAddElementView(JMXConnectorInfo jmxConnectorInfo)
    {
        ElementView element = new ElementView(container, jmxConnectorInfo);
        elements.put(jmxConnectorInfo, element);
        return element;
    }
    
    public void addSelectionChangedListener(ISelectionChangedListener listener)
    {
        selectionListeners.add(listener);
    }

    public void removeSelectionChangedListener(ISelectionChangedListener listener)
    {
        selectionListeners.remove(listener);
    }
    
    public void addPostSelectionChangedListener(ISelectionChangedListener listener)
    {
        postSelectionListeners.add(listener);
    }

    public void removePostSelectionChangedListener(ISelectionChangedListener listener)
    {
        postSelectionListeners.remove(listener);
    }

    public ISelection getSelection()
    {
        return selection;
    }

    public void setSelection(ISelection selection)
    {
        this.selection = selection;
        SelectionChangedEvent event = new SelectionChangedEvent(this, selection);
        notifySelectionListeners(event);
        notifyPostSelectionListeners(event);
    }
    
    private void notifySelectionListeners(SelectionChangedEvent event)
    {
        for (ISelectionChangedListener listener : selectionListeners) listener.selectionChanged(event);
    }

    private void notifyPostSelectionListeners(SelectionChangedEvent event)
    {
        for (ISelectionChangedListener listener : postSelectionListeners) listener.selectionChanged(event);
    }

    private class ElementView extends Composite
    {
        private final JMXConnectorInfo jmxConnectorInfo;
        private final int eventMask = SWT.MouseUp;
        private Label iconLabel;
        private Label textLabel;

        public ElementView(Composite parent, JMXConnectorInfo jmxConnectorInfo)
        {
            super(parent, SWT.BORDER);
            this.jmxConnectorInfo = jmxConnectorInfo;

            GridLayout layout = new GridLayout(1, false);
            layout.marginTop = layout.marginRight = layout.marginBottom = layout.marginLeft = 5;
            setLayout(layout);
            setCursor(elementCursor);
            
            EventForwarder eventForwarder = new EventForwarder(eventMask);
            
            iconLabel = new Label(this, SWT.NONE);
            iconLabel.setImage(elementIcon);
            GridData layoutData = new GridData();
            layoutData.horizontalAlignment = GridData.CENTER;
            layoutData.grabExcessHorizontalSpace = true;
            iconLabel.setLayoutData(layoutData);
            iconLabel.addListener(eventMask, eventForwarder);
            
            textLabel = new Label(this, SWT.NONE);
            layoutData = new GridData();
            layoutData.horizontalAlignment = GridData.CENTER;
            layoutData.grabExcessHorizontalSpace = true;
            textLabel.setLayoutData(layoutData);
            textLabel.addListener(eventMask, eventForwarder);
            
            addListener(eventMask, new Listener()
            {
                public void handleEvent(Event event)
                {
                    if (event.button == 1)
                    {
                        setSelection(ElementView.this.jmxConnectorInfo);
                    }
                }
            });
            
            setMenu(popupMenu);
            iconLabel.setMenu(popupMenu);
            textLabel.setMenu(popupMenu);
            
            show(jmxConnectorInfo);
        }
        
        private void show(JMXConnectorInfo jmxConnectorInfo)
        {
            JMXConnectorInfo selected = null;
            ISelection currentSelection = getSelection();
            if (currentSelection != null) selected = (JMXConnectorInfo)((IStructuredSelection)currentSelection).getFirstElement();
            
            Color background = getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND);
            if (selected == jmxConnectorInfo) background = getDisplay().getSystemColor(SWT.COLOR_LIST_SELECTION);
            setBackground(background);

            iconLabel.setEnabled(jmxConnectorInfo.isConnected());

            String text = jmxConnectorInfo.getName();
            if (text == null || text.length() == 0) text = Messages.LABEL_UNKNOWN_HOST_TEXT;
            textLabel.setText(text);
            textLabel.setBackground(background);
            textLabel.setEnabled(jmxConnectorInfo.isConnected());
        }
        
        private class EventForwarder implements Listener
        {
            private final int eventMask;
            
            public EventForwarder(final int eventMask)
            {
                this.eventMask = eventMask;
            }

            public void handleEvent(Event event)
            {
                ElementView.this.notifyListeners(eventMask, event);
            }
        }
    }
}
