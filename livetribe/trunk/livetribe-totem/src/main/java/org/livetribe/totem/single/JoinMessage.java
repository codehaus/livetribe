/**
 *
 * Copyright 2005 (C) The original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.livetribe.totem.single;

import java.util.HashSet;
import java.util.Set;

import org.livetribe.totem.Identifier;
import org.livetribe.totem.Message;


/**
 * @version $Revision: $ $Date: $
 */
public class JoinMessage implements Message
{
    private Identifier senderId;
    private final Set processorSet = new HashSet();
    private final Set failedSet = new HashSet();
    private int incarnation;

    public Identifier getSenderId()
    {
        return senderId;
    }

    public void setSenderId(Identifier senderId)
    {
        this.senderId = senderId;
    }

    public Set getProcessorSet()
    {
        return processorSet;
    }

    public Set getFailedSet()
    {
        return failedSet;
    }

    public int getIncarnation()
    {
        return incarnation;
    }

    public void setIncarnation(int incarnation)
    {
        this.incarnation = incarnation;
    }
}
