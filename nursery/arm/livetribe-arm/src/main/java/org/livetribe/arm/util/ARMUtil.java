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

import org.opengroup.arm40.metric.ArmMetricFactory;
import org.opengroup.arm40.tranreport.ArmTranReportFactory;
import org.opengroup.arm40.transaction.ArmTransactionFactory;


/**
 * @version $Revision: $ $Date: $
 */
public class ARMUtil
{
    public static ArmMetricFactory createArmMetricFactory() throws ARMException
    {
        return createArmMetricFactory(Thread.currentThread().getContextClassLoader());
    }

    public static ArmMetricFactory createArmMetricFactory(ClassLoader cl) throws ARMException
    {
        ArmMetricFactory factory;
        String className = System.getProperty(ArmMetricFactory.propertyKey);

        try
        {
            Class factoryClass = cl.loadClass(className);
            factory = (ArmMetricFactory) factoryClass.newInstance();
        }
        catch (ClassNotFoundException cnfe)
        {
            throw new ARMException("Creating ArmMetricFactory with class " + className, cnfe);
        }
        catch (IllegalAccessException iae)
        {
            throw new ARMException("Creating ArmMetricFactory with class " + className, iae);
        }
        catch (InstantiationException ie)
        {
            throw new ARMException("Creating ArmMetricFactory with class " + className, ie);
        }
        return factory;
    }

    public static ArmTransactionFactory createArmTransactionFactory() throws ARMException
    {
        return createArmTransactionFactory(Thread.currentThread().getContextClassLoader());
    }

    public static ArmTransactionFactory createArmTransactionFactory(ClassLoader cl) throws ARMException
    {
        ArmTransactionFactory factory;
        String className = System.getProperty(ArmTransactionFactory.propertyKey);

        try
        {
            Class factoryClass = cl.loadClass(className);
            factory = (ArmTransactionFactory) factoryClass.newInstance();
        }
        catch (ClassNotFoundException cnfe)
        {
            throw new ARMException("Creating ArmTransactionFactory with class " + className, cnfe);
        }
        catch (IllegalAccessException iae)
        {
            throw new ARMException("Creating ArmTransactionFactory with class " + className, iae);
        }
        catch (InstantiationException ie)
        {
            throw new ARMException("Creating ArmTransactionFactory with class " + className, ie);
        }
        return factory;
    }

    public static ArmTranReportFactory createArmTranReportFactory() throws ARMException
    {
        return createArmTranReportFactory(Thread.currentThread().getContextClassLoader());
    }

    public static ArmTranReportFactory createArmTranReportFactory(ClassLoader cl) throws ARMException
    {
        ArmTranReportFactory factory;
        String className = System.getProperty(ArmTranReportFactory.propertyKey);

        try
        {
            Class factoryClass = cl.loadClass(className);
            factory = (ArmTranReportFactory) factoryClass.newInstance();
        }
        catch (ClassNotFoundException cnfe)
        {
            throw new ARMException("Creating ArmTransactionFactory with class " + className, cnfe);
        }
        catch (IllegalAccessException iae)
        {
            throw new ARMException("Creating ArmTransactionFactory with class " + className, iae);
        }
        catch (InstantiationException ie)
        {
            throw new ARMException("Creating ArmTransactionFactory with class " + className, ie);
        }
        return factory;
    }
}
