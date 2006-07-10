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

/**
 * @version $Rev$ $Date$
 */
public class FrameInfo
{
    private String frameId;
    private String frameClassName;

    public String getFrameId()
    {
        return frameId;
    }

    public void setFrameId(String frameId)
    {
        this.frameId = frameId;
    }

    public String getFrameClassName()
    {
        return frameClassName;
    }

    public void setFrameClassName(String frameClassName)
    {
        this.frameClassName = frameClassName;
    }
}
