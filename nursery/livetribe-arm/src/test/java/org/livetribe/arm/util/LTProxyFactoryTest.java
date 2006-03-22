/**
 *
 * Copyright 2006 (C) The original author or authors
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
package org.livetribe.arm.util;

import junit.framework.TestCase;
import org.opengroup.arm40.transaction.ArmErrorCallback;
import org.opengroup.arm40.transaction.ArmInterface;

import org.livetribe.arm.ArmRuntimeException;
import org.livetribe.arm.LTAbstractFactoryBase;
import org.livetribe.arm.LTAbstractObjectBase;

/**
 * @version $Revision: $ $Date: $
 */
public class LTProxyFactoryTest extends TestCase
{
    public void testObjectBaseProxy()
    {
        LTAbstractFactoryBase factory = new MockFactory();
        MockInterface object = (MockInterface) LTProxyFactory.newProxyInstance(new Mock(factory));

        assertEquals("BAR", object.getFoo());
        assertEquals(0, ((ArmInterface) object).getErrorCode());

        object.throwArmRuntimeException();
        assertEquals(123, ((ArmInterface) object).getErrorCode());

        object.getFoo();
        assertEquals(0, ((ArmInterface) object).getErrorCode());

        factory.setErrorCallback(new ArmErrorCallback()
        {
            public void errorCodeSet(ArmInterface errorObject, String interfaceName, String methodName)
            {
                // override error code
                errorObject.setErrorCode(321);
            }
        });

        object.throwArmRuntimeException();
        assertEquals(321, ((ArmInterface) object).getErrorCode());
    }

    static interface MockInterface
    {
        public String getFoo();

        public void throwArmRuntimeException();
    }

    static class Mock extends LTAbstractObjectBase implements MockInterface
    {
        Mock(LTAbstractFactoryBase factory)
        {
            super(factory);
        }

        public String getFoo()
        {
            return "BAR";
        }

        public void throwArmRuntimeException()
        {
            throw new ArmRuntimeException(123, "Test Exception");
        }
    }

    static class MockFactory extends LTAbstractFactoryBase
    {
    }

}
