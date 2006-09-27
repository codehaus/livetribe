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
package org.livetribe.console.ui.jetty.jmx.view;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.livetribe.console.ui.jetty.Messages;
import org.livetribe.console.ui.jetty.jmx.ContextInfo;
import org.livetribe.console.ui.jetty.jmx.Jetty6Manager;
import org.livetribe.console.ui.jetty.jmx.ServerInfo;
import org.livetribe.console.ui.jmx.IMBeanViewContent;
import org.livetribe.console.ui.jmx.ObjectNameInfo;
import org.livetribe.ioc.Container;
import org.livetribe.ioc.Inject;

/**
 * @version $Revision: 157 $ $Date$
 */
public class Jetty6MBeanViewContent implements IMBeanViewContent
{
    @Inject
    private Container container;
    private Jetty6Manager jettyManager;
    private boolean initialized;
    private FormToolkit toolkit;
    private Form form;
    private Section summarySection;
    private Section contextsSection;

    public void init(Composite parent)
    {
        jettyManager = new Jetty6Manager();
        container.resolve(jettyManager);
        
        toolkit = new FormToolkit(parent.getDisplay());
        form = toolkit.createForm(parent);
        form.setText("JETTY 6"); // TODO
        FillLayout bodyLayout = new FillLayout(SWT.VERTICAL);
        bodyLayout.marginHeight = bodyLayout.marginWidth = 5;
        bodyLayout.spacing = 10;
        form.getBody().setLayout(bodyLayout);

        createSummarySection(form);
        createContextsSection(form);
        
        initialized = true;
    }

    public void dispose()
    {
        toolkit.dispose();
        form.dispose();
    }

    public boolean isInitialized()
    {
        return initialized;
    }

    public void show(ObjectNameInfo objectNameInfo)
    {
        if (summarySection.getClient() != null) summarySection.getClient().dispose();
        Composite summary = toolkit.createComposite(summarySection);
        summarySection.setClient(summary);
        GridLayout summaryLayout = new GridLayout(2, false);
        summary.setLayout(summaryLayout);
        toolkit.createLabel(summary, Messages.FORM_LABEL_SERVER_TEXT);
        Label versionLabel = toolkit.createLabel(summary, "");
        toolkit.createLabel(summary, Messages.FORM_LABEL_STARTUP_TEXT);
        Label startupLabel = toolkit.createLabel(summary, "");

        if (contextsSection.getClient() != null) contextsSection.getClient().dispose();
        Composite contexts = toolkit.createComposite(contextsSection);
        contextsSection.setClient(contexts);
        GridLayout contextsLayout = new GridLayout(1, false);
        contexts.setLayout(contextsLayout);
        
        try
        {
            ServerInfo serverInfo = jettyManager.getServerInfo(objectNameInfo);
            versionLabel.setText(serverInfo.getVersion());
            DateFormat formatter = new SimpleDateFormat(Messages.DATE_FORMAT);
            String date = formatter.format(new Date(serverInfo.getStartupTime()));
            startupLabel.setText(date);
            
            List<ContextInfo> contextInfos = jettyManager.getContextInfos(serverInfo);
            for (ContextInfo contextInfo : contextInfos)
            {
                Label contextLabel = toolkit.createLabel(contexts, contextInfo.getContextPath());
                contextLabel.setToolTipText(contextInfo.getDisplayName());
            }
        }
        catch (IOException x)
        {
            // TODO: mark the JMXConnectorInfo as not connected ?
            x.printStackTrace();
        }
    }
    
    private void createSummarySection(Form form)
    {
        summarySection = toolkit.createSection(form.getBody(), Section.TITLE_BAR | Section.TWISTIE | Section.EXPANDED);
        summarySection.setText(Messages.FORM_SECTION_STATUS_TITLE_TEXT);
        summarySection.setDescription(Messages.FORM_SECTION_STATUS_DESCRIPTION_TEXT);
    }
    
    private void createContextsSection(Form form)
    {
        contextsSection = toolkit.createSection(form.getBody(), Section.TITLE_BAR | Section.TWISTIE | Section.EXPANDED);
        contextsSection.setText(Messages.FORM_SECTION_CONTEXTS_TITLE);
    }
}
