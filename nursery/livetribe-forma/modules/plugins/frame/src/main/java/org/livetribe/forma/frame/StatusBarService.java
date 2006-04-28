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
 * @version $Rev$ $Date$
 */
public class StatusBarService
{
    private FrameService frameService;
    private ThreadService threadService;

    public void setFrameService(FrameService frameService)
    {
        this.frameService = frameService;
    }

    public void setThreadService(ThreadService threadService)
    {
        this.threadService = threadService;
    }

    public void setText(final String text)
    {
        threadService.postToEventQueue(new Runnable()
        {
            public void run()
            {
                frameService.getCurrentFrame().setStatusText(text);
            }
        });
    }
}
