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
package org.livetribe.forma.frame;

import java.awt.EventQueue;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JMenuBar;

import org.livetribe.forma.frame.widget.Frame;
import org.livetribe.forma.platform.i18n.InternationalizationService;
import org.livetribe.ioc.IOCService;

/**
 * @version $Rev$ $Date$
 */
public class FrameService implements IFrameServiceSpi
{
    private final Logger logger = Logger.getLogger(getClass().getName());
    private InternationalizationService translator;
    private IOCService iocService;
    private Object terminator;
    private Set<IFrameSpi> frames = new HashSet<IFrameSpi>();
    private IFrameSpi currentFrame;
    private List<FrameListener> listeners = new ArrayList<FrameListener>();

    public void setTranslator(InternationalizationService i18nService)
    {
        this.translator = i18nService;
    }

    public void setIOCService(IOCService iocService)
    {
        this.iocService = iocService;
    }

    // Do no specify the actual class of the terminator, to avoid compile time dependencies
    public void setTerminator(Object terminator)
    {
        this.terminator = terminator;
    }

    public void addFrameListener(FrameListener listener)
    {
        listeners.add(listener);
    }

    public void removeFrameListener(FrameListener listener)
    {
        listeners.remove(listener);
    }

    private IFrameSpi newFrame()
    {
        assert EventQueue.isDispatchThread();
        String title = translator.getBundle(getClass()).get("application.title", frames.size() + 1);
        Frame frame = new Frame(title);
        frame.setJMenuBar(new JMenuBar());
        frames.add(frame);
        iocService.resolve(frame);
        return frame;
    }

    public IFrame openNewFrame()
    {
        assert EventQueue.isDispatchThread();
        IFrameSpi frame = newFrame();
        frame.display();
        notifyFrameOpened(frame);
        return frame;
    }

    private void notifyFrameOpened(IFrameSpi frame)
    {
        FrameEvent event = new FrameEvent(frame);
        for (FrameListener listener : listeners) listener.frameOpened(event);
    }

    private void notifyFrameClosed(IFrameSpi frame)
    {
        FrameEvent event = new FrameEvent(frame);
        for (FrameListener listener : listeners) listener.frameClosed(event);
    }

    public void closeFrame(IFrame frame)
    {
        assert EventQueue.isDispatchThread();
        // TODO: Nothing better than downcasting ?
        assert frame instanceof IFrameSpi;
        IFrameSpi f = (IFrameSpi)frame;
        assert frames.contains(f);
        close(f);
        if (frames.isEmpty()) shutdown();
    }

    private void close(IFrameSpi frame)
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

    public void shutdownApplication()
    {
        assert EventQueue.isDispatchThread();
        boolean confirmed = currentFrame.confirmClose();
        if (!confirmed) return;

        // Copy frames to avoid ConcurrentModificationException
        Set<IFrameSpi> frameCopies = new HashSet<IFrameSpi>(frames);
        frames.clear();

        for (IFrameSpi frame : frameCopies) close(frame);
        shutdown();
    }

    private void shutdown()
    {
        try
        {
            // Use reflection to call the terminator so that this module
            // has no compile time dependencies with the boot module
            Class<? extends Object> terminatorClass = terminator.getClass();
            Method stop = terminatorClass.getMethod("stop");
            stop.invoke(terminator);
        }
        catch (Exception x)
        {
            if (logger.isLoggable(Level.INFO))
                logger.log(Level.INFO, "Could not invoke method 'stop' on terminator object " + terminator, x);
        }
    }

    public void setCurrentFrame(IFrameSpi frame)
    {
        assert EventQueue.isDispatchThread();
        this.currentFrame = frame;
    }

    public IFrame getCurrentFrame()
    {
        assert EventQueue.isDispatchThread();
        return currentFrame;
    }

    public Set<IFrameSpi> getFrames()
    {
        assert EventQueue.isDispatchThread();
        return frames;
    }
}
