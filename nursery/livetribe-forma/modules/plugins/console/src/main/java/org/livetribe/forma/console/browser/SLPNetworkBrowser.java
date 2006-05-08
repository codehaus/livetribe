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

import java.util.List;
import java.util.concurrent.Callable;

import org.livetribe.forma.threading.ThreadingManager;
import org.livetribe.forma.ui.browser.AbstractBrowser;
import org.livetribe.forma.ui.feedback.Feedback;
import org.livetribe.forma.ui.feedback.FeedbackManager;
import org.livetribe.ioc.Container;
import org.livetribe.ioc.Inject;
import org.livetribe.ioc.PostConstruct;
import org.livetribe.slp.ServiceURL;

/**
 * @version $Rev$ $Date$
 */
public class SLPNetworkBrowser extends AbstractBrowser
{
    public static final String ID = "org.livetribe.forma.browser.network.slp";

    @Inject private Container containerManager;
    @Inject private FeedbackManager feedbackManager;
    @Inject private ThreadingManager threadingManager;
    private SLPNetworkController controller;

    @PostConstruct
    private void initComponents()
    {
        controller = new SLPNetworkController();
        containerManager.resolve(controller);
    }

    public void spiOpen()
    {
        Feedback feedback = feedbackManager.showWaitCursor(this);
        try
        {
            Object result = threadingManager.executeSync(new Callable<List<ServiceURL>>()
            {
                public List<ServiceURL> call() throws Exception
                {
                    return controller.findServices("service:jmx");
                }
            });
            System.out.println("result = " + result);
        }
        finally
        {
            feedback.stop();
        }
    }
}
