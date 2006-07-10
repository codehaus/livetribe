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
package org.livetribe.forma.console.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import org.livetribe.forma.console.model.service.ServiceNode;
import org.livetribe.forma.console.model.service.slp.SLPModelManager;
import org.livetribe.forma.ui.Context;
import org.livetribe.forma.ui.ContextAware;
import org.livetribe.ioc.Inject;

/**
 * @version $Rev$ $Date$
 */
public class ScanNetworkWithSLPAction implements ActionListener, ContextAware
{
    public static final String ID = ScanNetworkWithSLPAction.class.getName();
    public static final String RESULT = ID + ".result";

    @Inject
    private SLPModelManager slpModelManager;
    private Context context;

    public void spiSetContext(Context context)
    {
        this.context = context;
    }

    public void actionPerformed(ActionEvent e)
    {
        try
        {
            Thread.sleep(5000);
        }
        catch (InterruptedException e1)
        {
            e1.printStackTrace();
        }

        List<ServiceNode> nodes = slpModelManager.findJMXServices();
        context.put(RESULT, nodes);
    }
}
