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
import org.livetribe.forma.ui.frame.FrameManager;
import org.livetribe.ioc.Container;
import org.livetribe.ioc.Inject;

/**
 * @version $Rev$ $Date$
 */
public class DefaultFrameManager implements FrameManager
{
    @Inject private Container containerManager;
    @Inject private ShutDownManager shutDownManager;
    private final Map<String, FrameInfo> frameInfos = new HashMap<String, FrameInfo>();
    private final List<FrameListener> listeners = new ArrayList<FrameListener>();
    private final List<Frame> frames = new ArrayList<Frame>();
    private Frame currentFrame;

    public void spiAddFrameInfo(FrameInfo frameInfo)
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

        Frame frame = createFrame(frameClassName);
        frames.add(frame);
        frame.spiDisplay();
        notifyFrameOpened(frame);
        return frame;
    }

    private Frame createFrame(String frameClassName)
    {
        try
        {
            Frame frame = (Frame)Thread.currentThread().getContextClassLoader().loadClass(frameClassName).newInstance();
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

    private void notifyFrameOpened(Frame frame)
    {
        FrameEvent event = new FrameEvent(frame);
        for (FrameListener listener : listeners) listener.frameOpened(event);
    }

    private void notifyFrameClosed(Frame frame)
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
        this.currentFrame = frame;
    }

    public void closeFrame(Frame frame)
    {
        assert frames.contains(frame);
        close(frame);
        if (frames.isEmpty()) shutdown();
    }

    public void shutdownApplication()
    {
        boolean confirmed = currentFrame.spiConfirmClose();
        if (!confirmed) return;

        // Copy frames to avoid ConcurrentModificationException
        List<Frame> frameCopies = new ArrayList<Frame>(frames);
        frames.clear();

        for (Frame frame : frameCopies) close(frame);
        shutdown();
    }

    private void close(Frame frame)
    {
        if (frames.size() == 1)
        {
            // The last frame, ask the user if really wants to close
            boolean confirmed = frame.spiConfirmClose();
            if (!confirmed) return;
        }

        frame.spiUndisplay();
        frames.remove(frame);
        notifyFrameClosed(frame);
    }

    private void shutdown()
    {
        shutDownManager.shutdown();
    }
}
