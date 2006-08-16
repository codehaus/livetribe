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
package org.livetribe.slp.demo.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import edu.emory.mathcs.backport.java.util.concurrent.Callable;

/**
 * @version $Revision$ $Date$
 */
public class StopScanAction implements ActionListener, Callable
{
    private final Application application;
    private final NetworkModel networkModel;

    public StopScanAction(Application application, NetworkModel networkModel)
    {
        this.application = application;
        this.networkModel = networkModel;
    }

    public void actionPerformed(ActionEvent e)
    {
        application.post(this);
    }

    public Object call() throws Exception
    {
        networkModel.stop();
        return null;
    }
}
