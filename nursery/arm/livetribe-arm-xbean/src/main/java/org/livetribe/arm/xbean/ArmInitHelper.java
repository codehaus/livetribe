/**
 *
 * Copyright 2006 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.livetribe.arm.xbean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;

import org.livetribe.arm.util.ARMException;
import org.livetribe.util.ByteArrayPropertyEditor;


/**
 * @version $Rev: $ $Date$
 * @org.apache.xbean.XBean
 */
public class ArmInitHelper implements BeanFactoryPostProcessor
{
    private boolean autowireFactories = true;
    private boolean registerPropertyEditors = true;

    public boolean isAutowireFactories()
    {
        return autowireFactories;
    }

    public void setAutowireFactories(boolean autowireFactories)
    {
        this.autowireFactories = autowireFactories;
    }

    public boolean isRegisterPropertyEditors()
    {
        return registerPropertyEditors;
    }

    public void setRegisterPropertyEditors(boolean registerPropertyEditors)
    {
        this.registerPropertyEditors = registerPropertyEditors;
    }

    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException
    {
        if (autowireFactories)
        {
            try
            {
                TransactionFactory transactionFactory = new TransactionFactory();
                beanFactory.registerSingleton("org.livetribe.arm.TransactionFactory", transactionFactory);

                MetricFactory metricFactory = new MetricFactory();
                beanFactory.registerSingleton("org.livetribe.arm.MetricFactory", metricFactory);

                String[] names = beanFactory.getBeanDefinitionNames();
                for (int i = 0; i < names.length; i++)
                {
                    AbstractBeanDefinition def = (AbstractBeanDefinition) beanFactory.getBeanDefinition(names[i]);

                    if (Holder.class.isAssignableFrom(def.getBeanClass())
                        && def.getAutowireMode() == AbstractBeanDefinition.AUTOWIRE_NO)
                    {
                        def.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
                    }
                }
            }
            catch (ARMException arme)
            {
                throw new BeanInitializationException("Creating ARM factories", arme);
            }
        }

        if (registerPropertyEditors)
        {
            beanFactory.registerCustomEditor((new byte[]{}).getClass(), new ByteArrayPropertyEditor());
            beanFactory.registerCustomEditor(SystemAddress.class, new SystemAddressIPPropertyEditor());
        }
    }
}
