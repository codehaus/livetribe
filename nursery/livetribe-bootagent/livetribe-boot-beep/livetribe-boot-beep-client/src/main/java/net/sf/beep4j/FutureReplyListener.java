/**
 *
 * Copyright 2007 (C) The original author or authors
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
package net.sf.beep4j;

/**
 * @version $Revision$ $Date$
 */
public abstract class FutureReplyListener<V> implements ReplyListener
{
    private FutureReply<V> owner;

    final void setOwner(FutureReply<V> owner)
    {
        this.owner = owner;
    }

    final protected void setValue(V value)
    {
        owner.setValue(value);
    }

    public void receiveANS(Message message)
    {
        throw new UnsupportedOperationException("Should not receive an ANS");
    }

    public void receiveNUL()
    {
        throw new UnsupportedOperationException("Should not receive an NUL");
    }

    public void receiveERR(Message message)
    {
        throw new UnsupportedOperationException("Should not receive a ERR");
    }

    public void receiveRPY(Message message)
    {
        throw new UnsupportedOperationException("Should not receive an RPY");
    }
}
