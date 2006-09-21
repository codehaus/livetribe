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

import java.net.MalformedURLException;

import javax.management.remote.JMXServiceURL;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.livetribe.console.ui.jmx.JMXConnectorInfo;

/**
 * @version $Revision: 156 $ $Date$
 */
public class JMXConnectionDialog extends Dialog
{
    private Text text;
    private JMXConnectorInfo jmxConnectorInfo;
    
    public JMXConnectionDialog(Shell shell)
    {
        super(shell);
        setShellStyle(getShellStyle() | SWT.RESIZE);
    }

    @Override
    protected Control createDialogArea(Composite parent)
    {
        Composite container = (Composite)super.createDialogArea(parent);
        
        GridLayout layout = new GridLayout(2, false); 
        layout.marginTop = layout.marginRight = layout.marginBottom = layout.marginLeft = 5;
        container.setLayout(layout);
        
        Label label = new Label(container, SWT.SHADOW_ETCHED_OUT);
        label.setText("Remote JMX Connector Server");
        GridData layoutData = new GridData();
        layoutData.horizontalSpan = 2;
        label.setLayoutData(layoutData);
        
        // TODO: add validation (using regexp ?)
        text = new Text(container, SWT.SINGLE);
        text.setText("service:jmx:");
        text.setSelection(text.getText().length(), text.getText().length());
        text.setFocus();
        layoutData = new GridData();
        layoutData.grabExcessHorizontalSpace = true;
        layoutData.horizontalAlignment = GridData.FILL;
        text.setLayoutData(layoutData);
        
        return container;
    }

    @Override
    protected Button createButton(Composite parent, int buttonId, String buttonText, boolean defaultButton)
    {
        if (buttonId == IDialogConstants.OK_ID)
        {
            return super.createButton(parent, buttonId, "Connect", defaultButton);
        }
        return super.createButton(parent, buttonId, buttonText, defaultButton);
    }

    @Override
    protected void okPressed()
    {
        try
        {
            JMXServiceURL url = new JMXServiceURL(text.getText());
            jmxConnectorInfo = new JMXConnectorInfo(url, null);
            super.okPressed();
        }
        catch (MalformedURLException x)
        {
            // TODO:
            x.printStackTrace();
        }
    }

    @Override
    protected Point getInitialSize()
    {
        return new Point(400, 250);
    }

    public JMXConnectorInfo getJMXConnectorInfo()
    {
        return jmxConnectorInfo;
    }
}
