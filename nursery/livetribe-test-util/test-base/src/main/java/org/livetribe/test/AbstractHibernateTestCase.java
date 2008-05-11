/**
 *
 * Copyright 2007 (C) The original author or authors
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
package org.livetribe.test;

import junit.framework.TestCase;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import org.hibernate.context.ManagedSessionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.junit.Before;
import org.junit.After;


/**
 * @version $Revision$ $Date$
 */
public abstract class AbstractHibernateTestCase
{
    private ClassPathXmlApplicationContext applicationContext;
    private SessionFactory sessionFactory;

    @Before
    protected void setUp()
    {
        applicationContext = new ClassPathXmlApplicationContext(getApplicationContextResources());
        sessionFactory = (SessionFactory) applicationContext.getBean("sessionFactory");
        open();
    }

    protected void open()
    {
        Session session = openSession(sessionFactory);
        ManagedSessionContext.bind(session);
        session.beginTransaction();
    }

    protected Session openSession(SessionFactory sessionFactory)
    {
        return sessionFactory.openSession();
    }

    protected void flush()
    {
        getCurrentSession().flush();
    }

    protected void close()
    {
        Session session = ManagedSessionContext.unbind(sessionFactory);
        if (session != null)
        {
            try
            {
                Transaction transaction = session.getTransaction();
                if (transaction != null)
                {
                    try
                    {
                        transaction.commit();
                    }
                    catch (RuntimeException x)
                    {
                        transaction.rollback();
                        throw x;
                    }
                }
            }
            finally
            {
                session.close();
            }
        }
    }

    @After
    protected void tearDown()
    {
        close();
        applicationContext.close();
    }

    protected abstract String[] getApplicationContextResources();

    protected Object getBean(String beanName)
    {
        return applicationContext.getBean(beanName);
    }

    public ApplicationContext getApplicationContext()
    {
        return applicationContext;
    }

    protected org.hibernate.Session getCurrentSession()
    {
        return sessionFactory.getCurrentSession();
    }

    protected Long getIdentifier(Object entity)
    {
        return (Long) getCurrentSession().getIdentifier(entity);
    }
}
