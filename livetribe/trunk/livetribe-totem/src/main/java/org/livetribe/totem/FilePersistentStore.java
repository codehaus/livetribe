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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * @version $Revision: $ $Date: $
 */
public class FilePersistentStore implements PersistentStore
{
    private final File store;

    public FilePersistentStore(File store)
    {
        this.store = store;
    }

    public short getRingIncarnation() throws IOException
    {
        if (!store.exists()) return (short) 0;

        return (new DataInputStream(new FileInputStream(store))).readShort();
    }

    public void setRingIncarnation(short incarnation) throws IOException
    {
//        if (!store.exists()) {
//            store.createNewFile();
//        }
        (new DataOutputStream(new FileOutputStream(store))).writeShort(incarnation);
    }
}
