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
package org.livetribe.arm.model.hibernate;

import java.util.List;

import junit.framework.TestCase;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import org.livetribe.arm.connection.model.ARMDAO;
import org.livetribe.arm.connection.model.IdentityProperties;


/**
 * @version $Revision$ $Date$
 */
public class HibernateARMDAOTest extends TestCase
{
    private ApplicationContext context;

    public void testRun()
    {
        ARMDAO dao = (ARMDAO) context.getBean("testDAO");

        IdentityProperties original = new IdentityProperties();

        original.setIdName01("FOO");
        original.setIdValue01("BAR");

        dao.saveIdentityProperties(original);

        IdentityProperties copy = dao.getIdentityPropertiesByOID(original.getOid());

        assertTrue("FOO".equals(copy.getIdName01()));

        copy.setIdName02("DOG");
        copy.setIdValue02("CAT");

        dao.saveIdentityProperties(copy);

        IdentityProperties savedCopy = dao.getIdentityPropertiesByOID(original.getOid());

        assertTrue("DOG".equals(savedCopy.getIdName02()));

        List list = dao.getIdentityPropertiesByName(new String[]{"FOO", "DOG"}, new String[]{"BAR", "CAT"}, null);

        assertTrue(list.size() == 1);

        IdentityProperties found = (IdentityProperties) list.get(0);

        assertNotNull(found);
        assertTrue("DOG".equals(found.getIdName02()));

        dao.deleteIdentityProperties(copy);

        IdentityProperties shouldBeNull = dao.getIdentityPropertiesByOID(original.getOid());

        assertTrue(null == shouldBeNull);
    }

    public void setUp()
    {
        context = new ClassPathXmlApplicationContext("org/livetribe/arm/model/hibernate/TestContext.xml");
    }

    public void tearDown()
    {
        context = null;
    }
}
