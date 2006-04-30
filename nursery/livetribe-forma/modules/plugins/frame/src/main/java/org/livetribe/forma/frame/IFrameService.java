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

/**
 * The API for plugins that want to operate on frames.
 * @version $Rev$ $Date$
 */
public interface IFrameService
{
    /**
     * Creates, initializes and displays a new frame,
     * a top level window extending {@link java.awt.Frame}.
     * @return The frame just created
     */
    public IFrame openNewFrame();

    /**
     * Returns the currently active frame.
     */
    public IFrame getCurrentFrame();

    /**
     * Closes the given frame, and if that frame is the last open,
     * shuts down the application.
     * @param frame The frame to be closed
     * @see #shutdownApplication()
     */
    public void closeFrame(IFrame frame);

    /**
     * Shuts down the application by closing all open frames and
     * stopping all services.
     * @see #closeFrame(IFrame)
     */
    public void shutdownApplication();
}
