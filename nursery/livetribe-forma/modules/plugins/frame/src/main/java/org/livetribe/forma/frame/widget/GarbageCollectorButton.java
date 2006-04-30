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
package org.livetribe.forma.frame.widget;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import org.livetribe.forma.platform.i18n.InternationalizationService;
import org.livetribe.forma.platform.i18n.Bundle;
import org.livetribe.ioc.Inject;
import org.livetribe.ioc.PostConstruct;

/**
 * @version $Rev$ $Date$
 */
public class GarbageCollectorButton extends JButton
{
    private Bundle bundle;

    @Inject
    public void setI18NService(InternationalizationService i18nService)
    {
        bundle = i18nService.getBundle(getClass());
    }

    @PostConstruct
    private void initComponents()
    {
        URL url = getClass().getClassLoader().getResource(bundle.get("garbage.collector.button.icon"));
        if (url != null)
        {
            ImageIcon icon = new ImageIcon(url);
            setIcon(icon);
        }

        setFocusPainted(false);
        setDefaultCapable(false);
        // TODO: fix borders

        addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                gc();
            }
        });

        setToolTipText(bundle.get("garbage.collector.button.tooltip.text"));

        // TODO: add status bar message
        // TODO: add mouseEntered/mouseExited to highlight the button (like toolbar buttons)
    }

    private void gc()
    {
        // TODO: use threadService.syncExecute() ?
        System.gc();
    }
}
