/**
 *
 * Copyright 2006 (C) The original author or authors.
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
package org.livetribe.arm;

import java.util.Locale;

import junit.framework.TestCase;


/**
 * @version $Revision: $ $Date: $
 */
public class LTAbstractBaseTest extends TestCase
{
    private Locale saved;

    public void testI18N()
    {
        Mock object = new Mock();

        Locale.setDefault(new Locale("en", "US", "test"));

        object.setErrorCode(I18NConstants.TEST_CODES);

        assertEquals("Test Message", object.getErrorMessage(object.getErrorCode()));
    }

    public void setUp()
    {
        saved = Locale.getDefault();
    }

    public void tearDown()
    {
        Locale.setDefault(saved);
    }

    static class Mock extends LTAbstractObject
    {
    }

    static class MockFactory extends LTAbstractFactoryBase
    {
    }
}
