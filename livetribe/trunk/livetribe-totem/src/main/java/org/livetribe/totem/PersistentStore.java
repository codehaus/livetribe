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
package org.livetribe.totem;

import java.io.IOException;


/**
 * Interface class that provides access to some form of persistent store that
 * will provide stable storage for the ring incarnation number.  This number is
 * used for ring recovery.
 *
 * @version $Revision: $ $Date: $
 */
public interface PersistentStore
{
    /**
     * Retrieve the ring incarnation number from stable storage.
     *
     * @return the ring incarnation number
     * @throws IOException if there is a problem retrieving the ring
     *                     incarnation number
     */
    public short getRingIncarnation() throws IOException;

    /**
     * Store the ring incarnation number to stable storage.
     *
     * @param incarnation the ring incarnation number to be stored
     * @throws IOException if there is a problem storing the ring
     *                     incarnation number
     */
    public void setRingIncarnation(short incarnation) throws IOException;
}
