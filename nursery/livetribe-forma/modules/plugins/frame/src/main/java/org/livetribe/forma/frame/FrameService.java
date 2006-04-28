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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JMenuBar;

import org.livetribe.forma.frame.i18n.I18NService;
import org.livetribe.forma.frame.widget.Frame;
import org.livetribe.ioc.IOCService;

/**
 * @version $Rev$ $Date$
 */
public class FrameService
{
    private final Logger logger = Logger.getLogger(getClass().getName());
    private I18NService translator;
    private IOCService iocService;
    private Object terminator;
    private List<Frame> frames = new ArrayList<Frame>();
    private Frame currentFrame;
    private List<FrameListener> listeners = new ArrayList<FrameListener>();

    public void setTranslator(I18NService translator)
    {
        this.translator = translator;
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

    private Frame newFrame()
    {
        assert EventQueue.isDispatchThread();
        String title = translator.getI18N(Frame.class).get("application.title", frames.size() + 1);
        Frame frame = new Frame(title);
        frame.setJMenuBar(new JMenuBar());
        frames.add(frame);
        iocService.resolve(frame);
        return frame;
    }

    public void openNewFrame()
    {
        assert EventQueue.isDispatchThread();

        Frame frame = newFrame();
        frame.display();

        notifyFrameOpened(frame);
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

    public void closeFrame(Frame frame)
    {
        assert EventQueue.isDispatchThread();
        assert frames.contains(frame);
        close(frame);
        if (frames.isEmpty()) shutdown();
    }

    private void close(Frame frame)
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
        boolean confirmed = getCurrentFrame().confirmClose();
        if (!confirmed) return;

        // Copy frames to avoid ConcurrentModificationException
        List<Frame> frameCopies = new ArrayList<Frame>(frames);
        frames.clear();

        for (Frame frame : frameCopies) close(frame);
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

    public void setCurrentFrame(Frame frame)
    {
        assert EventQueue.isDispatchThread();
        this.currentFrame = frame;
    }

    public Frame getCurrentFrame()
    {
        assert EventQueue.isDispatchThread();
        return currentFrame;
    }

    public List<Frame> getFrames()
    {
        assert EventQueue.isDispatchThread();
        return frames;
    }
}
