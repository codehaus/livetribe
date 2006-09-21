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
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.livetribe.console.ui.jetty.Activator;
import org.livetribe.console.ui.jetty.Messages;
import org.livetribe.console.ui.jmx.IMBeanViewContent;
import org.livetribe.console.ui.jmx.ObjectNameInfo;
import org.livetribe.console.ui.widgets.OutlookBar;
import org.livetribe.ioc.Container;
import org.livetribe.ioc.Inject;

/**
 * @version $Revision: 157 $ $Date$
 */
public class Jetty6MBeanViewContent implements IMBeanViewContent
{
    @Inject
    private Container container;
    private boolean initialized;
    private Composite composite;
    private OutlookBar outlookBar;
    private Composite content;
    private ObjectNameInfo objectNameInfo;

    public void init(Composite parent)
    {
        this.composite = new Composite(parent, SWT.NONE);
        composite.setLayout(new FormLayout());
        
        outlookBar = new OutlookBar(composite);
        outlookBar.setBackground(composite.getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
        FormData layoutData = new FormData();
        layoutData.top = new FormAttachment(0, 5);
        layoutData.left = new FormAttachment(0, 5);
        layoutData.bottom = new FormAttachment(100, -5);
        outlookBar.setLayoutData(layoutData);
        
        OutlookBar.Item item = outlookBar.newItem();
        item.setImage(Activator.imageDescriptorFromPlugin("org.livetribe.console.ui", "icons/exit.png").createImage());
        item.setText(Messages.ITEM_SUMMARY_TEXT);
        item.addMouseListener(new MouseAdapter() 
        {
            @Override
            public void mouseUp(MouseEvent event)
            {
                if (event.button == 1)
                {
                    showSummaryContent();
                }
            }
        });

        initialized = true;
    }

    public void dispose()
    {
    }

    public boolean isInitialized()
    {
        return initialized;
    }

    public void show(ObjectNameInfo objectNameInfo)
    {
        this.objectNameInfo = objectNameInfo;
    }
    
    public void showSummaryContent()
    {
        if (content != null) content.dispose();
        Jetty6SummaryContent summary = new Jetty6SummaryContent();
        container.resolve(summary);
        summary.init(composite);
        content = summary.getComposite();
        FormData layoutData = new FormData();
        layoutData.top = new FormAttachment(0, 5);
        layoutData.right = new FormAttachment(100, -5);
        layoutData.bottom = new FormAttachment(100, -5);
        layoutData.left = new FormAttachment(outlookBar, 5);
        content.setLayoutData(layoutData);

        composite.layout();
    }
}
