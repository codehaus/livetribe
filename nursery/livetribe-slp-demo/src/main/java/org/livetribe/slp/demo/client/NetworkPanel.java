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

import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import org.livetribe.slp.Attributes;
import org.livetribe.slp.ServiceInfo;

/**
 * @version $Revision$ $Date$
 */
public class NetworkPanel extends JPanel implements Observer
{
    private final Application application;
    private final NetworkModel model;

    public NetworkPanel(Application application, NetworkModel model)
    {
        this.application = application;
        this.model = model;
    }

    public void init()
    {
        setLayout(new FlowLayout(FlowLayout.LEFT, 25, 25));
    }

    public void update(Observable source, Object event)
    {
        removeAll();

        List serviceInfos = new ArrayList(model.getServiceInfos());
        Collections.sort(serviceInfos, new ServiceInfoComparator());
        for (Iterator iterator = serviceInfos.iterator(); iterator.hasNext();)
        {
            ServiceInfo serviceInfo = (ServiceInfo)iterator.next();
            ServiceInfoPanel serviceInfoPanel = new ServiceInfoPanel(application, serviceInfo);
            serviceInfoPanel.init();
            add(serviceInfoPanel);
        }

        revalidate();
        repaint();
    }

    private class ServiceInfoComparator implements Comparator
    {
        public int compare(Object o1, Object o2)
        {
            ServiceInfo s1 = (ServiceInfo)o1;
            ServiceInfo s2 = (ServiceInfo)o2;
            Attributes a1 = s1.getAttributes();
            Attributes a2 = s2.getAttributes();
            String n1 = (String)a1.getValue("name");
            String n2 = (String)a2.getValue("name");
            return n1.compareTo(n2);
        }
    }
}
