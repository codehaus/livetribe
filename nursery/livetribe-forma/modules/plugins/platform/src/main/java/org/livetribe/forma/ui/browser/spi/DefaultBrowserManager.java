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
package org.livetribe.forma.ui.browser.spi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.livetribe.forma.ui.PartContainer;
import org.livetribe.forma.ui.browser.Browser;
import org.livetribe.forma.ui.browser.BrowserEvent;
import org.livetribe.forma.ui.browser.BrowserException;
import org.livetribe.forma.ui.browser.BrowserListener;
import org.livetribe.forma.ui.browser.BrowserManager;
import org.livetribe.forma.ui.perspective.PerspectiveException;
import org.livetribe.ioc.Container;
import org.livetribe.ioc.Inject;


/**
 * @version $Rev$ $Date$
 */
public class DefaultBrowserManager implements BrowserManager
{
    @Inject private Container containerManager;
    private Map<String, BrowserInfo> browserInfos = new HashMap<String, BrowserInfo>();
    private final List<BrowserListener> listeners = new ArrayList<BrowserListener>();

    public void spiAddBrowserInfo(BrowserInfo browserInfo)
    {
        String browserId = browserInfo.getBrowserId();
        if (browserInfos.containsKey(browserId)) throw new PerspectiveException("Duplicate browser id " + browserId);
        browserInfos.put(browserId, browserInfo);
    }

    public void addBrowserListener(BrowserListener listener)
    {
        listeners.add(listener);
    }

    public void removeBrowserListener(BrowserListener listener)
    {
        listeners.remove(listener);
    }

    public Browser displayBrowser(String browserId, PartContainer container)
    {
        if (browserId == null) return null;
        BrowserInfo browserInfo = browserInfos.get(browserId);
        if (browserInfo == null) throw new BrowserException("No browser with id " + browserId + " is registered in the browser extensions");
        String browserClassName = browserInfo.getBrowserClassName();
        Browser browser = createBrowser(browserClassName);
        browser.spiDisplayIn(container);
        browser.spiOpen();
        notifyBrowserOpened(browser);
        return browser;
    }

    private Browser createBrowser(String browserClassName)
    {
        try
        {
            Browser browser = (Browser)Thread.currentThread().getContextClassLoader().loadClass(browserClassName).newInstance();
            containerManager.resolve(browser);
            return browser;
        }
        catch (Exception x)
        {
            throw new BrowserException(x);
        }
    }

    private void notifyBrowserOpened(Browser browser)
    {
        BrowserEvent event = new BrowserEvent(browser);
        for (BrowserListener listener : listeners) listener.browserOpened(event);
    }
}
