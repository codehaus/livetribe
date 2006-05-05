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
package org.livetribe.forma.ui.perspective.spi;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import org.livetribe.forma.ui.perspective.PerspectiveException;
import org.livetribe.forma.ui.perspective.PerspectiveListener;
import org.livetribe.forma.ui.perspective.Perspective;
import org.livetribe.forma.ui.perspective.PerspectiveEvent;
import org.livetribe.forma.ui.PartContainer;
import org.livetribe.ioc.Inject;
import org.livetribe.ioc.Container;

/**
 * @version $Rev$ $Date$
 */
public class DefaultPerspectiveManager implements PerspectiveManagerSpi
{
    @Inject private Container containerManager;
    private final Map<String, PerspectiveInfo> perspectiveInfos = new HashMap<String, PerspectiveInfo>();
    private String defaultPerspectiveId;
    private final List<PerspectiveListener> listeners = new ArrayList<PerspectiveListener>();

    public void addPerspectiveInfo(PerspectiveInfo perspectiveInfo)
    {
        String perspectiveId = perspectiveInfo.getPerspectiveId();
        if (perspectiveInfos.containsKey(perspectiveId)) throw new PerspectiveException("Duplicate perspective id " + perspectiveId);
        perspectiveInfos.put(perspectiveId, perspectiveInfo);
    }

    public void setDefaultPerspectiveId(String perspectiveId)
    {
        this.defaultPerspectiveId = perspectiveId;
    }

    public String getDefaultPerspectiveId()
    {
        return defaultPerspectiveId;
    }

    public Perspective displayNewPerspective(PartContainer frame, String perspectiveId)
    {
        if (perspectiveId == null) return null;
        PerspectiveInfo perspectiveInfo = perspectiveInfos.get(perspectiveId);
        if (perspectiveInfo == null) throw new PerspectiveException("No perspective with id " + perspectiveId + " is registered in the perspective extensions");
        String perspectiveClassName = perspectiveInfo.getPerspectiveClassName();
        PerspectiveSpi perspective = createPerspective(perspectiveClassName);
        perspective.displayIn(frame);
//        perspective.open();
//        perspective.load();
        notifyPerspectiveOpened(perspective);
        return perspective;
    }

    private PerspectiveSpi createPerspective(String perspectiveClassName)
    {
        try
        {
            PerspectiveSpi perspective = (PerspectiveSpi)Thread.currentThread().getContextClassLoader().loadClass(perspectiveClassName).newInstance();
            containerManager.resolve(perspective);
            return perspective;
        }
        catch (Exception x)
        {
            throw new PerspectiveException(x);
        }
    }

    public void closePerspective(Perspective prspctv)
    {
        if (prspctv == null) return;
        PerspectiveSpi perspective = (PerspectiveSpi)prspctv;
//        perspective.save();
//        perspective.close();
        perspective.undisplay();
        notifyPerspectiveClosed(perspective);
    }

    public void addPerspectiveListener(PerspectiveListener listener)
    {
        listeners.add(listener);
    }

    public void removePerspectiveListener(PerspectiveListener listener)
    {
        listeners.remove(listener);
    }

    private void notifyPerspectiveOpened(Perspective perspective)
    {
        PerspectiveEvent event = new PerspectiveEvent(perspective);
        for (PerspectiveListener listener : listeners) listener.perspectiveOpened(event);
    }

    private void notifyPerspectiveClosed(Perspective perspective)
    {
        PerspectiveEvent event = new PerspectiveEvent(perspective);
        for (PerspectiveListener listener : listeners) listener.perspectiveClosed(event);
    }
}
