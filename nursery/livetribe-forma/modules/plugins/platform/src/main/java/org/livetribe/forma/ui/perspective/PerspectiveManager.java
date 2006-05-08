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
package org.livetribe.forma.ui.perspective;

import org.livetribe.forma.ui.PartContainer;
import org.livetribe.forma.ui.perspective.spi.PerspectiveInfo;

/**
 * @version $Rev$ $Date$
 */
public interface PerspectiveManager
{
    public static final String ID = "org.livetribe.forma.perspective.perspectiveManager";

    public void spiAddPerspectiveInfo(PerspectiveInfo perspectiveInfo);

    public void setDefaultPerspectiveId(String perspectiveId);

    public String getDefaultPerspectiveId();

    public Perspective displayNewPerspective(PartContainer container, String perspectiveId);

    public void closePerspective(Perspective perspective);

    public void addPerspectiveListener(PerspectiveListener listener);

    public void removePerspectiveListener(PerspectiveListener listener);

    public Perspective getPerspective(PartContainer container);
}
