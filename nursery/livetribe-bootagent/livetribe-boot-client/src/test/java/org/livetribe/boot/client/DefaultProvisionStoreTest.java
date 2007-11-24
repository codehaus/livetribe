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
package org.livetribe.boot.client;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

import org.livetribe.boot.protocol.ProvisionEntry;


/**
 * @version $Revision$ $Date$
 */
public class DefaultProvisionStoreTest extends TestCase
{
    private final static String STORE_LOCATION = "./target/store";

    public void test() throws Exception
    {
        DefaultProvisionStore store = new DefaultProvisionStore(new File(STORE_LOCATION));

        store.setUuid("1452-a8e6b-2df7-2ed9");

        assertEquals("1452-a8e6b-2df7-2ed9", store.getUuid());

        try
        {
            store.prepareNext();
            fail("Should have rejected an un-initialized provision directive");
        }
        catch (ProvisionStoreException e)
        {
        }

        store.setNextProvisionDirective(new ProvisionDirective(0, "good", Collections.<ProvisionEntry>emptySet()));

        try
        {
            store.prepareNext();
            fail("Should have rejected a bad provision directive");
        }
        catch (ProvisionStoreException e)
        {
        }

        store.setNextProvisionDirective(new ProvisionDirective(1234, "", Collections.<ProvisionEntry>emptySet()));

        try
        {
            store.prepareNext();
            fail("Should have rejected a bad provision directive");
        }
        catch (ProvisionStoreException e)
        {
        }

        store.setNextProvisionDirective(new ProvisionDirective(1234, "good", Collections.<ProvisionEntry>emptySet()));

        store.prepareNext();

        Set<ProvisionEntry> entries = new HashSet<ProvisionEntry>();
        entries.add(new ProvisionEntry("missing", 456));

        store.setNextProvisionDirective(new ProvisionDirective(1234, "good", entries));

        try
        {
            store.prepareNext();
            fail("Should have rejected a bad provision directive which has unsaved resources");
        }
        catch (ProvisionStoreException e)
        {
        }

        store.store(new ProvisionEntry("missing", 456), new ByteArrayInputStream(new byte[]{(byte) 0xca, (byte) 0xfe, (byte) 0xba, (byte) 0xbe}));

        store.prepareNext();
    }

    public void setUp()
    {
        delete(new File(STORE_LOCATION));
    }

    public void tearDown()
    {
        delete(new File(STORE_LOCATION));
    }

    private void delete(File file)
    {
        if (file.isDirectory())
        {
            for (String name : file.list()) delete(new File(file, name));
        }
        else
        {
            file.delete();
        }
    }
}
