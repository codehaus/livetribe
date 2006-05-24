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
package org.livetribe.forma.console.browser;

import java.awt.Component;
import java.util.List;
import java.util.concurrent.Callable;

import org.livetribe.forma.threading.ThreadingManager;
import org.livetribe.forma.ui.PartContainer;
import org.livetribe.forma.ui.browser.Browser;
import org.livetribe.forma.ui.feedback.Feedback;
import org.livetribe.forma.ui.feedback.FeedbackManager;
import org.livetribe.ioc.Container;
import org.livetribe.ioc.Inject;
import org.livetribe.ioc.PostConstruct;
import org.livetribe.slp.api.sa.ServiceInfo;

/**
 * @version $Rev$ $Date$
 */
public class SLPNetworkBrowser implements Browser
{
    public static final String ID = SLPNetworkBrowser.class.getName();

    @Inject private Container containerManager;
    @Inject private FeedbackManager feedbackManager;
    @Inject private ThreadingManager threadingManager;
    private final SLPNetworkModel model = new SLPNetworkModel();
    private final SLPNetworkPanel panel = new SLPNetworkPanel();
    private PartContainer container;

    @PostConstruct
    private void initComponents()
    {
        containerManager.resolve(panel);
        containerManager.resolve(model);
    }

    public Component spiGetComponent()
    {
        return panel;
    }

    public void spiDisplayIn(PartContainer container)
    {
        this.container = container;
        container.spiDisplay(this);
    }

    public void spiUndisplay()
    {
        if (container == null) return;
        container.spiDisplay(null);
        container = null;
    }

    public void spiOpen()
    {
        Feedback feedback = feedbackManager.showWaitCursor(panel);
        try
        {
            List<ServiceInfo> result = threadingManager.syncExecute(new Callable<List<ServiceInfo>>()
            {
                public List<ServiceInfo> call() throws Exception
                {
                    return model.findServices("service:jmx");
                }
            });
            panel.display(result);
        }
        finally
        {
            feedback.stop();
        }
    }
}
