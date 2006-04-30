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

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.concurrent.TimeUnit;
import javax.swing.JProgressBar;

import org.livetribe.forma.platform.i18n.Bundle;
import org.livetribe.forma.platform.i18n.InternationalizationService;
import org.livetribe.forma.platform.threading.IThreadingService;
import org.livetribe.ioc.Inject;
import org.livetribe.ioc.PostConstruct;

/**
 * @version $Rev$ $Date$
 */
public class MemoryBar extends JProgressBar implements Runnable, ActionListener
{
    private static final int SHIFT_FOR_MEGA = 20;
    private Bundle bundle;
    private IThreadingService threadService;

    @Inject
    public void setI18NService(InternationalizationService i18nService)
    {
        bundle = i18nService.getBundle(getClass());
    }

    @Inject
    public void setThreadingService(IThreadingService threadService)
    {
        this.threadService = threadService;
    }

    @PostConstruct
    private void initComponents()
    {
        setStringPainted(true);
        setBorderPainted(false);
        setMaximumSize(new Dimension(100, 20));
        threadService.invokeWithFixedDelay(this, 0, 2, TimeUnit.SECONDS);
    }

    public void run()
    {
        int total = getTotalMemory();
        int free = getFreeMemory();
        int value = total - free;
        String text = bundle.get("memory.bar.text", value, total);

        setMaximum(total);
        setValue(value);
        setString(text);
    }

    public void actionPerformed(ActionEvent e)
    {
        run();
    }

    private int getTotalMemory()
    {
        long value = Runtime.getRuntime().totalMemory();
        return (int)(value >> SHIFT_FOR_MEGA);
    }

    private int getFreeMemory()
    {
        long memory = Runtime.getRuntime().freeMemory();
        return (int)(memory >> SHIFT_FOR_MEGA);
    }
}
