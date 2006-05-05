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
package org.livetribe.forma.ui.frame.spi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.livetribe.forma.ShutDownManager;
import org.livetribe.forma.ui.frame.Frame;
import org.livetribe.forma.ui.frame.FrameListener;
import org.livetribe.forma.ui.frame.FrameException;
import org.livetribe.forma.ui.frame.FrameEvent;
import org.livetribe.ioc.Container;
import org.livetribe.ioc.Inject;

/**
 * @version $Rev$ $Date$
 */
public class DefaultFrameManager implements FrameManagerSpi
{
    @Inject private Container containerManager;
    @Inject private ShutDownManager shutDownManager;
    private final Map<String, FrameInfo> frameInfos = new HashMap<String, FrameInfo>();
    private final List<FrameListener> listeners = new ArrayList<FrameListener>();
    private final List<FrameSpi> frames = new ArrayList<FrameSpi>();
    private FrameSpi currentFrame;

    public void addFrameInfo(FrameInfo frameInfo)
    {
        String frameId = frameInfo.getFrameId();
        if (frameInfos.containsKey(frameId)) throw new FrameException("Duplicate frame id " + frameId);
        frameInfos.put(frameId, frameInfo);
    }

    public Frame displayNewFrame(String frameId)
    {
        FrameInfo frameInfo = frameInfos.get(frameId);
        if (frameInfo == null)
            throw new FrameException("No frame with id " + frameId + " is registered in the frame extensions");
        String frameClassName = frameInfo.getFrameClassName();

        // The steps involved in creating a frame: 1) new 2) display 3) notify.
        // In future, we may want to expand these steps, for example adding a "restore" phase
        // to restore window geometry, and this additional step will only impact FrameSpi
        FrameSpi frame = createFrame(frameClassName);
        frames.add(frame);
        frame.display();
        notifyFrameOpened(frame);
        return frame;
    }

    private FrameSpi createFrame(String frameClassName)
    {
        try
        {
            FrameSpi frame = (FrameSpi)Thread.currentThread().getContextClassLoader().loadClass(frameClassName).newInstance();
            containerManager.resolve(frame);
            return frame;
        }
        catch (Exception x)
        {
            throw new FrameException(x);
        }
    }

    public void addFrameListener(FrameListener listener)
    {
        listeners.add(listener);
    }

    public void removeFrameListener(FrameListener listener)
    {
        listeners.remove(listener);
    }

    private void notifyFrameOpened(FrameSpi frame)
    {
        FrameEvent event = new FrameEvent(frame);
        for (FrameListener listener : listeners) listener.frameOpened(event);
    }

    private void notifyFrameClosed(FrameSpi frame)
    {
        FrameEvent event = new FrameEvent(frame);
        for (FrameListener listener : listeners) listener.frameClosed(event);
    }

    public Frame getCurrentFrame()
    {
        return currentFrame;
    }

    public void setCurrentFrame(Frame frame)
    {
        this.currentFrame = (FrameSpi)frame;
    }

    public void closeFrame(Frame f)
    {
        FrameSpi frame = (FrameSpi)f;
        assert frames.contains(frame);
        close(frame);
        if (frames.isEmpty()) shutdown();
    }

    public void shutdownApplication()
    {
        boolean confirmed = currentFrame.confirmClose();
        if (!confirmed) return;

        // Copy frames to avoid ConcurrentModificationException
        List<FrameSpi> frameCopies = new ArrayList<FrameSpi>(frames);
        frames.clear();

        for (FrameSpi frame : frameCopies) close(frame);
        shutdown();
    }

    private void close(FrameSpi frame)
    {
        if (frames.size() == 1)
        {
            // The last frame, ask the user if really wants to close
            boolean confirmed = frame.confirmClose();
            if (!confirmed) return;
        }

        frame.undisplay();
        frames.remove(frame);
        notifyFrameClosed(frame);
    }

    private void shutdown()
    {
        shutDownManager.shutdown();
    }
}
