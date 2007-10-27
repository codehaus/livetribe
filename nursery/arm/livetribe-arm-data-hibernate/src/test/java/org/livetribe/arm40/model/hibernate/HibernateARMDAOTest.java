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
package org.livetribe.arm40.model.hibernate;

import java.util.List;

import org.livetribe.arm40.model.ARMDAO;
import org.livetribe.arm40.model.IdentityProperties;
import org.livetribe.arm40.model.IdentityPropertiesTransaction;
import org.livetribe.test.AbstractHibernateTestCase;
import org.livetribe.util.HexSupport;
import org.livetribe.util.uuid.UUIDGen;
import org.livetribe.util.uuid.UUIDGenDefault;


/**
 * @version $Revision$ $Date$
 */
public class HibernateARMDAOTest extends AbstractHibernateTestCase
{
    private final UUIDGen guidGenerator = new UUIDGenDefault();

    public void testIdentityProperties()
    {
        ARMDAO dao = (ARMDAO) getApplicationContext().getBean("testDAO");

        IdentityProperties original = new IdentityProperties();

        original.setOid(HexSupport.toHexFromBytes(guidGenerator.uuidgen()));
        original.setIdName01("FOO");
        original.setIdValue01("BAR");
        original.setIdName02("DOG");
        original.setIdValue02("CAT");

        dao.saveIdentityProperties(original);

        IdentityProperties copy = dao.getIdentityPropertiesByOID(original.getOid());

        assertTrue("FOO".equals(copy.getIdName01()));

        List list = dao.getIdentityPropertiesByName(new String[]{"FOO", "DOG"}, new String[]{"BAR", "CAT"}, null);

        assertTrue(list.size() == 1);

        IdentityProperties found = (IdentityProperties) list.get(0);

        assertNotNull(found);
        assertTrue("DOG".equals(found.getIdName02()));

        dao.deleteIdentityProperties(copy);

        try
        {
            dao.getIdentityPropertiesByOID(original.getOid());
            fail("Should not find this deleted object");
        }
        catch (Exception e)
        {
        }
    }

    public void testIdentityPropertiesTransaction()
    {
        ARMDAO dao = (ARMDAO) getApplicationContext().getBean("testDAO");

        IdentityPropertiesTransaction original = new IdentityPropertiesTransaction();

        original.setOid(HexSupport.toHexFromBytes(guidGenerator.uuidgen()));
        original.setIdName01("FOO");
        original.setIdValue01("BAR");
        original.setIdName02("DOG");
        original.setIdValue02("CAT");
        original.setUriValue("http://livetribe.codehaus.org");

        dao.saveIdentityProperties(original);

        IdentityPropertiesTransaction copy = dao.getIdentityPropertiesTransactionByOID(original.getOid());

        assertTrue("FOO".equals(copy.getIdName01()));
        assertTrue("http://livetribe.codehaus.org".equals(copy.getUriValue()));

        List list = dao.getIdentityPropertiesTransactionByName(new String[]{"FOO", "DOG"}, new String[]{"BAR", "CAT"}, null, "http://livetribe.codehaus.org");

        assertTrue(list.size() == 1);

        IdentityPropertiesTransaction found = (IdentityPropertiesTransaction) list.get(0);

        assertNotNull(found);
        assertTrue("DOG".equals(found.getIdName02()));

        dao.deleteIdentityPropertiesTransaction(copy);

        try
        {
            dao.getIdentityPropertiesByOID(original.getOid());
            fail("Should not find this deleted object");
        }
        catch (Exception e)
        {
        }
    }

    protected String[] getApplicationContextResources()
    {
        return new String[]{"org/livetribe/arm40/model/hibernate/HibernateARMDAOTest.xml"};
    }
}
