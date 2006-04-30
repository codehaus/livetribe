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
package org.livetribe.forma.console;

import org.livetribe.forma.frame.FrameEvent;
import org.livetribe.forma.frame.FrameListener;
import org.livetribe.forma.frame.FrameService;
import org.livetribe.forma.frame.IFrame;

/**
 * @version $Rev$ $Date$
 */
public class ConsolePlugin
{
    private FrameService frameService;
    private final FrameListener frameListener = new FrameEventListener();

    public void setFrameService(FrameService frameService)
    {
        this.frameService = frameService;
    }

    public void start()
    {
        frameService.addFrameListener(frameListener);
    }

    public void stop()
    {
        frameService.removeFrameListener(frameListener);
    }

    private void frameOpened(IFrame frame)
    {
//        DashBoard dashBoard = frame.getDashBoard();
//        dashBoard.add(new JLabel("SIMON"));
    }

    private class FrameEventListener implements FrameListener
    {
        public void frameOpened(FrameEvent event)
        {
            ConsolePlugin.this.frameOpened(event.getFrame());
        }

        public void frameClosed(FrameEvent event)
        {
        }
    }
}
