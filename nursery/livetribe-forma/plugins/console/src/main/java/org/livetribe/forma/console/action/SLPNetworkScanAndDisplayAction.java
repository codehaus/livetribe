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

import org.livetribe.forma.console.perspective.ServicesSummaryPerspective;
import org.livetribe.forma.threading.ThreadingManager;
import org.livetribe.forma.ui.Context;
import org.livetribe.forma.ui.action.Action;
import org.livetribe.forma.ui.action.ActionManager;
import org.livetribe.forma.ui.feedback.FeedbackManager;
import org.livetribe.forma.ui.frame.Frame;
import org.livetribe.forma.ui.frame.FrameManager;
import org.livetribe.forma.ui.perspective.Perspective;
import org.livetribe.forma.ui.perspective.PerspectiveManager;
import org.livetribe.ioc.Inject;

/**
 * @version $Rev$ $Date$
 */
public class SLPNetworkScanAndDisplayAction implements Action
{
    public static final String ID = SLPNetworkScanAndDisplayAction.class.getName();

    @Inject
    private FrameManager frameManager;
    @Inject
    private PerspectiveManager perspectiveManager;
    @Inject
    private ThreadingManager threadingManager;
    @Inject
    private ActionManager actionManager;
    @Inject
    private FeedbackManager feedbackManager;

    public void execute(Context context)
    {
        Frame currentFrame = frameManager.getCurrentFrame();
        Perspective perspective = perspectiveManager.loadPerspective(ServicesSummaryPerspective.ID, currentFrame, context);
        threadingManager.flushEvents();
        perspectiveManager.openPerspective(perspective, context);
    }
}
