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
package org.livetribe.forma.ui.statusbar.spi;

import java.util.HashMap;
import java.util.Map;

import org.livetribe.forma.ui.statusbar.StatusbarException;
import org.livetribe.forma.ui.statusbar.StatusbarContainer;

/**
 * @version $Rev$ $Date$
 */
public class DefaultStatusbarManager implements StatusbarManagerSpi
{
    private Map<String, StatusbarInfo> statusbarInfos = new HashMap<String, StatusbarInfo>();

    public void addStatusbarInfo(StatusbarInfo statusbarInfo)
    {
        String statusbarId = statusbarInfo.getStatusbarId();
        if (statusbarInfos.containsKey(statusbarId)) throw new StatusbarException("Duplicate statusbar id " + statusbarId);
        statusbarInfos.put(statusbarId, statusbarInfo);
    }

    public void installStatusBar(StatusbarContainer frame, String statusbarId)
    {
        // TODO
    }
}
