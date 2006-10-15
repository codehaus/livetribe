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
package org.livetribe.arm40.connection.messages;

import java.util.Properties;

import junit.framework.TestCase;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


/**
 * @version $Revision$ $Date$
 */
public class AbstractHibernateContextTestCase extends TestCase
{
    protected SessionFactory sessionFactory;

    public void testNothing()
    {
    }

    public void XsetUp()
    {
        Properties props = new Properties();

        props.put("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
        props.put("hibernate.connection.driver_class", "org.hsqldb.jdbcDriver");
        props.put("hibernate.connection.url", "jdbc:hsqldb:mem:arm40");
        props.put("hibernate.connection.username", "sa");
        props.put("hibernate.connection.password", "");
        props.put("hibernate.connection.pool_size", "1");
        props.put("hibernate.connection.autocommit", "true");
        props.put("hibernate.cache.provider_class", "c");
        props.put("hibernate.hbm2ddl.auto", "create-drop");
        props.put("hibernate.show_sql", "true");

        try
        {
            sessionFactory = new Configuration()
                .addProperties(props)
                .addResource("ARMMessages.hbm.xml")
                .buildSessionFactory();
        }
        catch (Throwable ex)
        {
            throw new ExceptionInInitializerError(ex);
        }

    }
}
