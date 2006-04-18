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

import org.springframework.aop.Advisor;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;


/**
 * @version $Revision: $ $Date: $
 */
public class ProxyPerfTest
{
    static final int ITERATIONS = 5000000;

    public static void main(String[] args)
    {
        ISimpleBean target = new SimpleBean();

        Advisor advisor = new DefaultPointcutAdvisor(new TestPointcut(),
                                                     new NoOpBeforeAdvice());

        runCglibTests(advisor, target);
        runCglibFrozenTests(advisor, target);
        runJdkTests(advisor, target);
    }

    private static void runCglibTests(Advisor advisor, ISimpleBean target)
    {
        ProxyFactory pf = new ProxyFactory();
        pf.setTarget(target);
        pf.addAdvisor(advisor);
        ISimpleBean proxy = (ISimpleBean) pf.getProxy();
        System.out.println("Running CGLIB (Standard) Tests");
        test(proxy);
    }

    private static void runCglibFrozenTests(Advisor advisor, ISimpleBean target)
    {
        ProxyFactory pf = new ProxyFactory();
        pf.setTarget(target);
        pf.addAdvisor(advisor);
        pf.setFrozen(true);
        ISimpleBean proxy = (ISimpleBean) pf.getProxy();
        System.out.println("Running CGLIB (Frozen) Tests");
        test(proxy);
    }

    private static void runJdkTests(Advisor advisor, ISimpleBean target)
    {
        ProxyFactory pf = new ProxyFactory();
        pf.setTarget(target);
        pf.addAdvisor(advisor);
        pf.setInterfaces(new Class[]{ISimpleBean.class});
        ISimpleBean proxy = (ISimpleBean) pf.getProxy();
        System.out.println("Running JDK Tests");
        test(proxy);
    }

    private static void test(ISimpleBean bean)
    {
        long before = 0;
        long after = 0;

        // test advised method
        System.out.println("Testing Advised Method");
        before = System.currentTimeMillis();
        for (int x = 0; x < ITERATIONS; x++)
        {
            bean.advised();
        }
        after = System.currentTimeMillis();

        printStats(before, after);

        // testing unadvised method
        System.out.println("Testing Unadvised Method");
        before = System.currentTimeMillis();
        for (int x = 0; x < ITERATIONS; x++)
        {
            bean.unadvised();
        }
        after = System.currentTimeMillis();

        printStats(before, after);

        // testing equals() method
        System.out.println("Testing equals() Method");
        before = System.currentTimeMillis();
        for (int x = 0; x < ITERATIONS; x++)
        {
            bean.equals(bean);
        }
        after = System.currentTimeMillis();

        printStats(before, after);

        // testing hashCode() method
        System.out.println("Testing hashCode() Method");
        before = System.currentTimeMillis();
        for (int x = 0; x < ITERATIONS; x++)
        {
            bean.hashCode();
        }
        after = System.currentTimeMillis();

        printStats(before, after);

        // testing method on Advised
        Advised advised = (Advised) bean;
        System.out.println("Testing Advised.getTargetSource() Method");
        before = System.currentTimeMillis();
        for (int x = 0; x < ITERATIONS; x++)
        {
            advised.getTargetSource();
        }
        after = System.currentTimeMillis();

        printStats(before, after);
        System.out.println(">>>\n");
    }

    static void printStats(long before, long after)
    {
        System.out.println("Took " + (after - before) / 1000.0 + "s, " + (after - before) * 1000000.0 / ITERATIONS + "ns/call");
    }
}
