/**
 *
 * Copyright 2010 (C) The original author or authors
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
package org.livetribe.boot.server;

import java.io.ByteArrayInputStream;
import java.util.Collections;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

import org.livetribe.boot.protocol.DoNothing;
import org.livetribe.boot.protocol.ProvisionEntry;
import org.livetribe.boot.protocol.YouShould;


/**
 * @version $Revision: $ $Date: $
 */
public class OverrideProviderTest
{
    @Test
    public void test() throws Exception
    {
        YouShould base = new YouShould(0, "base", Collections.<ProvisionEntry>emptySet());
        YouShould override = new YouShould(0, "override", Collections.<ProvisionEntry>emptySet());
        MockProvisionProvider baseProvisionProvider = new MockProvisionProvider(base);
        MockContentProvider baseContentProvider = new MockContentProvider(new ByteArrayInputStream(new byte[]{'b', 'a', 's', 'e'}));
        MockProvisionProvider overrideProvisionProvider = new MockProvisionProvider();
        MockContentProvider overrideContentProvider = new MockContentProvider(new ByteArrayInputStream(new byte[]{'o', 'v', 'e', 'r'}));

        OverrideProvider overrideProvider = new OverrideProvider(baseProvisionProvider, baseContentProvider,
                                                                 overrideProvisionProvider, overrideContentProvider);

        assertTrue(overrideProvider.hello("", 0) == base);
        assertTrue(overrideProvider.pleaseProvide("", 0).read() == 'b');
        assertTrue(overrideProvider.hello("", 0) == base);
        assertTrue(overrideProvider.pleaseProvide("", 0).read() == 'a');

        overrideProvisionProvider.setDirective(override);

        assertTrue(overrideProvider.hello("", 0) == override);
        assertTrue(overrideProvider.pleaseProvide("", 0).read() == 'o');
        assertTrue(overrideProvider.hello("", 0) == override);
        assertTrue(overrideProvider.pleaseProvide("", 0).read() == 'v');

        overrideProvisionProvider.setDirective(new DoNothing());

        assertTrue(overrideProvider.hello("", 0) == base);
        assertTrue(overrideProvider.pleaseProvide("", 0).read() == 's');
        assertTrue(overrideProvider.hello("", 0) == base);
        assertTrue(overrideProvider.pleaseProvide("", 0).read() == 'e');
    }
}
