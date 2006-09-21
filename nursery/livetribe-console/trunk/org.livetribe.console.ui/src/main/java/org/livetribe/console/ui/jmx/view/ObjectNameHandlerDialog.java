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

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.livetribe.console.ui.Messages;
import org.livetribe.ioc.Inject;

/**
 * @version $Revision$ $Date$
 */
public class ObjectNameHandlerDialog extends Dialog
{
    @Inject 
    private ObjectNameHandlerManager objectNameHandlerManager;
    
    public ObjectNameHandlerDialog(Shell shell)
    {
        super(shell);
        setShellStyle(SWT.RESIZE | getShellStyle());
    }

    @Override
    protected Control createDialogArea(Composite parent)
    {
        Composite container = (Composite)super.createDialogArea(parent);
        GridLayout layout = new GridLayout(1, false);
        layout.verticalSpacing = 5;
        layout.marginTop = layout.marginRight = layout.marginBottom = layout.marginLeft = 5;
        container.setLayout(layout);
        
        Label instructions = new Label(container, SWT.NONE);
        instructions.setText("foo");
        
        CheckboxTableViewer viewer = CheckboxTableViewer.newCheckList(container, SWT.BORDER);
        GridData layoutData = new GridData();
        layoutData.horizontalAlignment = GridData.FILL;
        layoutData.grabExcessHorizontalSpace = true;
        layoutData.verticalAlignment = GridData.FILL;
        layoutData.grabExcessVerticalSpace = true;
        viewer.getControl().setLayoutData(layoutData);
        
        viewer.setContentProvider(new ObjectNameHandlerContentProvider());
        viewer.setLabelProvider(new ObjectNameHandlerLabelProvider());
        viewer.setInput(objectNameHandlerManager.getObjectNameHandlerInfos());
        
        Label descriptionLabel = new Label(container, SWT.NONE);
        descriptionLabel.setText(Messages.LABEL_OBJECTNAME_HANDLER_DESCRIPTION_TEXT);
        
        final Text descriptionText = new Text(container, SWT.BORDER | SWT.MULTI | SWT.WRAP);
        descriptionText.setEditable(false);
        layoutData = new GridData();
        layoutData.heightHint = convertHeightInCharsToPixels(2);
        layoutData.horizontalAlignment = GridData.FILL;
        layoutData.grabExcessHorizontalSpace = true;
        descriptionText.setLayoutData(layoutData);
        viewer.addSelectionChangedListener(new ISelectionChangedListener() 
        {
            public void selectionChanged(SelectionChangedEvent event)
            {
                IStructuredSelection selection = (IStructuredSelection)event.getSelection();
                if (!selection.isEmpty()) 
                {
                    ObjectNameHandlerInfo info = (ObjectNameHandlerInfo)selection.getFirstElement();
                    descriptionText.setText(info.getDescription());
                }
            }
        });
        
        return container;
    }
    
    private static class ObjectNameHandlerContentProvider implements IStructuredContentProvider
    {
        private Object[] elements;
        
        public Object[] getElements(Object arg0)
        {
            return elements;
        }

        public void dispose()
        {
        }

        public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
        {
            List<ObjectNameHandlerInfo> input = (List<ObjectNameHandlerInfo>)newInput;
            elements = input.toArray();

            CheckboxTableViewer checkboxViewer = (CheckboxTableViewer)viewer;
            for (ObjectNameHandlerInfo info : input) checkboxViewer.setChecked(info, info.isEnabled());
        }
    }
    
    private static class ObjectNameHandlerLabelProvider extends LabelProvider implements ITableLabelProvider
    {
        public Image getColumnImage(Object element, int index)
        {
            return null;
        }

        public String getColumnText(Object element, int index)
        {
            return ((ObjectNameHandlerInfo)element).getName();
        }
    }
}
