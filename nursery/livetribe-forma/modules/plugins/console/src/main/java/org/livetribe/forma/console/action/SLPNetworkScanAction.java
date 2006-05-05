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
package org.livetribe.forma.console.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.livetribe.forma.ui.frame.Frame;
import org.livetribe.forma.ui.frame.FrameManager;
import org.livetribe.forma.ui.perspective.PerspectiveManager;
import org.livetribe.ioc.Inject;

/**
 * @version $Rev$ $Date$
 */
public class SLPNetworkScanAction implements ActionListener
{
    @Inject private FrameManager frameManager;
    @Inject private PerspectiveManager perspectiveManager;
//    @Inject private BrowserManager browserManager;

    public void actionPerformed(ActionEvent e)
    {
        Frame frame = frameManager.getCurrentFrame();
//        Perspective perspective = perspectiveManager.getPerspective(frame);
//        if (perspective == null) perspective = perspectiveManager.displayNewPerspective(frame, BrowserPerspective.ID);
//        if (!BrowserPerspective.ID.equals(perspective.getPerspectiveId()))
//        {
//            perspectiveManager.closePerspective(perspective);
//            perspective = perspectiveManager.displayNewPerspective(frame, BrowserPerspective.ID);
//        }
//
//        browserManager.displayNewBrowser(perspective, SLPNetworkBrowser.ID);
    }
}
