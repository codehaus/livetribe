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
package org.livetribe.arm40;

import java.net.URL;
import java.util.Collections;

import junit.framework.TestCase;
import org.apache.xbean.classloader.MultiParentClassLoader;

import org.livetribe.arm40.test.TestInterface;


/**
 * @version $Revision$ $Date$
 */
public class KnitPointTest extends TestCase
{
    public void testNothing() throws Exception
    {
        ClassLoader l = Thread.currentThread().getContextClassLoader();
        MultiParentClassLoader loader = new MultiParentClassLoader("FFF", new URL[]{}, new ClassLoader[]{l}, true, Collections.EMPTY_SET, Collections.EMPTY_SET);
        Class clazz = loader.loadClass(TestInterface.class.getName());
        
        if (clazz != null)
        {
            String name = clazz.getName();

            ClassLoader save = Thread.currentThread().getContextClassLoader();
            Thread.currentThread().setContextClassLoader(loader);
            try
            {
                Object obj = loader.loadClass("org.livetribe.arm40.test.TestImpl").newInstance();
                ClassLoader obtained = obj.getClass().getClassLoader();
                String test = obj.toString();
            }
            finally
            {
                Thread.currentThread().setContextClassLoader(save);
            }
        }
    }
}
