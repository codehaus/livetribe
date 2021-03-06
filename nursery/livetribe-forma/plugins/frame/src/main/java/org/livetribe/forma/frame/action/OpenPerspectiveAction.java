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
package org.livetribe.forma.frame.action;

import org.livetribe.forma.ui.Context;
import org.livetribe.forma.ui.action.Action;
import org.livetribe.forma.ui.frame.Frame;
import org.livetribe.forma.ui.frame.FrameManager;
import org.livetribe.forma.ui.perspective.PerspectiveManager;
import org.livetribe.ioc.Inject;

/**
 * @version $Rev$ $Date$
 */
public class OpenPerspectiveAction implements Action
{
    @Inject
    private FrameManager frameManager;
    @Inject
    private PerspectiveManager perspectiveManager;
    private String perspectiveId;

    public void setPerspectiveId(String perspectiveId)
    {
        this.perspectiveId = perspectiveId;
    }

    public void execute(Context context)
    {
        Frame frame = frameManager.getCurrentFrame();
        perspectiveManager.openPerspective(perspectiveId, frame, null);
    }
}
