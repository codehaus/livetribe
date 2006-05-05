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
import javax.swing.JPanel;

import org.livetribe.forma.ui.browser.spi.BrowserSpi;
import org.livetribe.forma.ui.PartContainer;
import org.livetribe.ioc.PostConstruct;

/**
 * @version $Rev$ $Date$
 */
public class SLPNetworkBrowser extends JPanel implements BrowserSpi
{
    @PostConstruct
    private void initComponents()
    {
//        SLPNetworkController controller = new SLPNetworkController();
    }

    public Component getComponent()
    {
        return this;
    }

    public void displayIn(PartContainer partContainer)
    {
    }

    public void open()
    {
//        controller.findServices("service:jmx");
    }
}
