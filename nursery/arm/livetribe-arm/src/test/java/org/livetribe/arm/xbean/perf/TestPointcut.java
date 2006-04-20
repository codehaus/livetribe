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
package org.livetribe.arm.xbean.perf;

import java.lang.reflect.Method;

import org.springframework.aop.support.StaticMethodMatcherPointcut;


/**
 * @version $Revision: $ $Date: $
 */
public class TestPointcut extends StaticMethodMatcherPointcut
{
    public boolean matches(Method method, Class cls)
    {
        return ("advised".equals(method.getName()));
    }

}
