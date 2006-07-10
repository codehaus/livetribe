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
package org.livetribe.forma.ui.view.spi;

import java.util.HashMap;
import java.util.Map;

import org.livetribe.forma.ui.view.ViewException;
import org.livetribe.forma.ui.view.ViewManager;
import org.livetribe.ioc.Container;
import org.livetribe.ioc.Inject;

/**
 * @version $Rev$ $Date$
 */
public class DefaultViewManager implements ViewManager
{
    @Inject
    private Container containerManager;
    private final Map<String, ViewInfo> viewInfos = new HashMap<String, ViewInfo>();

    public void spiAddViewInfo(ViewInfo viewInfo)
    {
        String viewId = viewInfo.getViewId();
        if (viewInfos.containsKey(viewId)) throw new ViewException("Duplicate view id " + viewId);
        viewInfos.put(viewId, viewInfo);
    }
}
