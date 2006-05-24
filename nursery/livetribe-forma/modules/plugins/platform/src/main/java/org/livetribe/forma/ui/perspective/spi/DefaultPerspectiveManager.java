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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.livetribe.forma.ui.PartContainer;
import org.livetribe.forma.ui.action.ActionContext;
import org.livetribe.forma.ui.perspective.Perspective;
import org.livetribe.forma.ui.perspective.PerspectiveContainer;
import org.livetribe.forma.ui.perspective.PerspectiveEvent;
import org.livetribe.forma.ui.perspective.PerspectiveException;
import org.livetribe.forma.ui.perspective.PerspectiveListener;
import org.livetribe.forma.ui.perspective.PerspectiveManager;
import org.livetribe.ioc.Container;
import org.livetribe.ioc.Inject;

/**
 * @version $Rev$ $Date$
 */
public class DefaultPerspectiveManager implements PerspectiveManager
{
    @Inject private Container containerManager;
    private final Map<String, PerspectiveInfo> perspectiveInfos = new HashMap<String, PerspectiveInfo>();
    private String defaultPerspectiveId;
    private final List<PerspectiveListener> listeners = new ArrayList<PerspectiveListener>();

    public void spiAddPerspectiveInfo(PerspectiveInfo perspectiveInfo)
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

    public Perspective displayPerspective(String perspectiveId, PartContainer container, ActionContext context)
    {
        if (perspectiveId == null) return null;
        PerspectiveInfo perspectiveInfo = perspectiveInfos.get(perspectiveId);
        if (perspectiveInfo == null) throw new PerspectiveException("No perspective with id '" + perspectiveId + "' is registered in the perspective extensions");

        Perspective currentPerspective = getPerspective(container);
        if (currentPerspective != null)
        {
            if (perspectiveId.equals(currentPerspective.getPerspectiveId())) return currentPerspective;
            closePerspective(currentPerspective);
        }

        String perspectiveClassName = perspectiveInfo.getPerspectiveClassName();
        Perspective perspective = createPerspective(perspectiveClassName);
        perspective.spiDisplayIn(container);
//        perspective.spiOpen(context);
//        perspective.spiLoad();
        notifyPerspectiveOpened(perspective);
        return perspective;
    }

    private Perspective createPerspective(String perspectiveClassName)
    {
        try
        {
            Perspective perspective = (Perspective)Thread.currentThread().getContextClassLoader().loadClass(perspectiveClassName).newInstance();
            containerManager.resolve(perspective);
            return perspective;
        }
        catch (Exception x)
        {
            throw new PerspectiveException(x);
        }
    }

    public void closePerspective(Perspective perspective)
    {
        if (perspective == null) return;
//        perspective.spiSave();
//        perspective.spiClose();
        perspective.spiUndisplay();
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

    public Perspective getPerspective(PartContainer container)
    {
        if (container instanceof PerspectiveContainer) return ((PerspectiveContainer)container).spiGetPerspective();
        return null;
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
