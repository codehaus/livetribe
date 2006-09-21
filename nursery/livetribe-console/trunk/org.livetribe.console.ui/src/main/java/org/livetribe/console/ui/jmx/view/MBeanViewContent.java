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

import java.io.IOException;
import java.util.Map;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanInfo;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.livetribe.console.ui.Messages;
import org.livetribe.console.ui.jmx.IMBeanViewContent;
import org.livetribe.console.ui.jmx.JMXConnectorInfo;
import org.livetribe.console.ui.jmx.JMXConnectorManager;
import org.livetribe.console.ui.jmx.ObjectNameInfo;
import org.livetribe.ioc.Inject;

/**
 * Mediator that coordinates interaction and visualization of MBean information
 * @version $Rev$ $Date$
 */
public class MBeanViewContent implements IMBeanViewContent
{
    @Inject 
    private JMXConnectorManager jmxConnectorManager;
    private TabFolder tabs;
    private TableViewer attributesViewer;
    private boolean initialized;

    public void init(Composite parent)
    {
        tabs = new TabFolder(parent, SWT.TOP);
        TabItem attributesTab = new TabItem(tabs, SWT.NONE);
        attributesTab.setText(Messages.TAB_MBEAN_ATTRIBUTES_TEXT);
        attributesTab.setToolTipText(attributesTab.getText());
        
        Table attributesTable = new Table(tabs, SWT.FULL_SELECTION);
        attributesTable.setHeaderVisible(true);
        attributesTable.setLinesVisible(true);
        TableColumn nameColumn = new TableColumn(attributesTable, SWT.LEFT);
        nameColumn.setText(Messages.COLUMN_MBEAN_ATTRIBUTE_NAME_TEXT);
        nameColumn.setWidth(200);
        TableColumn valueColumn = new TableColumn(attributesTable, SWT.LEFT);
        valueColumn.setText(Messages.COLUMN_MBEAN_ATTRIBUTE_VALUE_TEXT);
        valueColumn.setWidth(200);
        
        attributesViewer = new TableViewer(attributesTable);
        attributesViewer.setContentProvider(new TableContentProvider());
        attributesViewer.setLabelProvider(new TableLabelProvider());
        attributesTab.setControl(attributesViewer.getControl());
        
        initialized = true;
    }
    
    public boolean isInitialized()
    {
        return initialized;
    }

    public void dispose()
    {
        tabs.dispose();
        attributesViewer.getControl().dispose();
    }

    public void show(ObjectNameInfo objectNameInfo)
    {
        JMXConnectorInfo connectorInfo = (JMXConnectorInfo)objectNameInfo.getRoot();
        try
        {
            MBeanInfo mbeanInfo = jmxConnectorManager.getMBeanInfo(connectorInfo, objectNameInfo);
            Map<String, Object> attributes = jmxConnectorManager.getAttributes(connectorInfo, objectNameInfo, mbeanInfo.getAttributes());
            
            attributesViewer.setInput(attributes);
        }
        catch (InstanceNotFoundException x)
        {
            x.printStackTrace();
        }
        catch (IOException x)
        {
            x.printStackTrace();
        }
    }
    
    private class TableContentProvider implements IStructuredContentProvider
    {
        private Map data;
        
        public Object[] getElements(Object input)
        {
            return data.entrySet().toArray();
        }

        public void dispose()
        {
        }

        public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
        {
            data = (Map)newInput;
        }
    }
    
    private class TableLabelProvider extends LabelProvider implements ITableLabelProvider
    {
        public Image getColumnImage(Object element, int index)
        {
            return null;
        }

        public String getColumnText(Object element, int index)
        {
            Map.Entry entry = (Map.Entry)element;
            switch (index)
            {
                case 0:
                    return getText(entry.getKey());
                case 1:
                    return getText(entry.getValue());
            }
            return null;
        }
    }
}
