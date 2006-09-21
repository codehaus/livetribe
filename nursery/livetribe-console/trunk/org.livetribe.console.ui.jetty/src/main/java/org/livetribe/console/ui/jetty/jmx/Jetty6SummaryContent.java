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
package org.livetribe.console.ui.jetty.jmx;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.livetribe.console.ui.jetty.Messages;
import org.livetribe.console.ui.jmx.JMXConnectorManager;
import org.livetribe.console.ui.jmx.ObjectNameInfo;
import org.livetribe.ioc.Inject;

/**
 * @version $Revision$ $Date$
 */
public class Jetty6SummaryContent
{
    @Inject
    private JMXConnectorManager jmxConnectorManager;
    private FormToolkit toolkit;
    private Form form;
    
    public void init(Composite parent)
    {
        toolkit = new FormToolkit(parent.getDisplay());
        form = toolkit.createForm(parent);
        form.setText("JETTY 6"); // TODO
        
        Section status = toolkit.createSection(form.getBody(), Section.DESCRIPTION);
        status.setText(Messages.FORM_SECTION_SUMMARY_STATUS_TEXT);
        toolkit.createCompositeSeparator(status);
        status.setDescription("description");
        Composite statusComposite = toolkit.createComposite(status);
        statusComposite.setLayout(new FillLayout());
        Label label = toolkit.createLabel(statusComposite, "Status:");
        
        
    }
    
    public void show(ObjectNameInfo objectNameInfo)
    {
        
    }
    
    public Composite getComposite()
    {
        return form;
    }
}
