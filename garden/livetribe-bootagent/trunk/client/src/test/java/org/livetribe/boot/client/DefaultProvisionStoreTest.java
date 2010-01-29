/**
 *
 * Copyright 2007-2010 (C) The original author or authors
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
import java.io.InputStream;
import java.net.URL;
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

        store.setNextProvisionDirective(new ProvisionConfiguration(0, "com.acme.boot.LifeCycleImpl", Collections.<ProvisionEntry>emptySet()));

        try
        {
            store.prepareNext();
            fail("Should have rejected a bad provision directive");
        }
        catch (ProvisionStoreException e)
        {
        }

        store.setNextProvisionDirective(new ProvisionConfiguration(454, "", Collections.<ProvisionEntry>emptySet()));

        try
        {
            store.prepareNext();
            fail("Should have rejected a bad provision directive");
        }
        catch (ProvisionStoreException e)
        {
        }

        store.setNextProvisionDirective(new ProvisionConfiguration(454, "com.acme.boot.LifeCycleImpl", Collections.<ProvisionEntry>emptySet()));

        store.prepareNext();

        Set<ProvisionEntry> entries = new HashSet<ProvisionEntry>();
        entries.add(new ProvisionEntry("missing", 32));

        store.setNextProvisionDirective(new ProvisionConfiguration(455, "com.acme.boot.LifeCycleImpl", entries));

        try
        {
            store.prepareNext();
            fail("Should have rejected a bad provision directive which has unsaved resources");
        }
        catch (ProvisionStoreException e)
        {
        }

        store.store(new ProvisionEntry("missing", 32), new ByteArrayInputStream(new byte[]{(byte)0xca, (byte)0xfe, (byte)0xba, (byte)0xbe}));

        store.prepareNext();
        store.commitNext();

        URL[] classpath = store.getClasspath();

        assertEquals(1, classpath.length);

        InputStream inputStream = classpath[0].openConnection().getInputStream();
        byte[] buffer = new byte[1024];

        assertEquals(4, inputStream.read(buffer));
        assertEquals((byte)0xca, buffer[0]);
        assertEquals((byte)0xfe, buffer[1]);
        assertEquals((byte)0xba, buffer[2]);
        assertEquals((byte)0xbe, buffer[3]);

        entries = new HashSet<ProvisionEntry>();
        entries.add(new ProvisionEntry("nextgen", 21));

        store.setNextProvisionDirective(new ProvisionConfiguration(456, "com.acme.boot.ServiceLifeCycle", entries));

        store.store(new ProvisionEntry("nextgen", 21), new ByteArrayInputStream(new byte[]{(byte)0xba, (byte)0xbe, (byte)0xca, (byte)0xfe}));

        store.prepareNext();

        classpath = store.getClasspath();

        assertEquals(1, classpath.length);

        inputStream = classpath[0].openConnection().getInputStream();

        assertEquals(4, inputStream.read(buffer));
        assertEquals((byte)0xba, buffer[0]);
        assertEquals((byte)0xbe, buffer[1]);
        assertEquals((byte)0xca, buffer[2]);
        assertEquals((byte)0xfe, buffer[3]);
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
            //noinspection ResultOfMethodCallIgnored
            file.delete();
        }
    }
}
