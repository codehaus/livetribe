/**
 *
 * Copyright 2008 (C) The original author or authors
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
package org.livetribe.ec2.model;

/**
 * @version $Revision$ $Date$
 */
public class InstanceState
{
    public enum State
    {
        pending, running, shutting_down, terminated;

        @Override
        public String toString()
        {
            return name().replaceAll("_", "-").toLowerCase();
        }

        public static State getValue(String value)
        {
            return valueOf(value.replaceAll("-", "_"));
        }
    }

    private final short code;
    private final State state;

    public InstanceState(short code, State state)
    {
        this.code = code;
        this.state = state;
    }

    public short getCode()
    {
        return code;
    }

    public State getState()
    {
        return state;
    }
}
