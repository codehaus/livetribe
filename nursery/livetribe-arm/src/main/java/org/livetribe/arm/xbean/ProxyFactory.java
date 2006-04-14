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
package org.livetribe.arm.xbean;

import java.util.Iterator;
import java.util.Set;

import org.springframework.aop.Advisor;


/**
 * @version $Revision: $ $Date: $
 */
public class ProxyFactory extends org.springframework.aop.framework.ProxyFactory
{
    public void setAdvisor(Advisor advisor)
    {
        addAdvisor(advisor);
    }

    public void addInterfaces(Set interfaces)
    {
        Iterator iter = interfaces.iterator();
        while (iter.hasNext())
        {
            addInterface((Class) iter.next());
        }
    }

    public void removeInterfaces(Set interfaces)
    {
        Iterator iter = interfaces.iterator();
        while (iter.hasNext())
        {
            removeInterface((Class) iter.next());
        }
    }
}
