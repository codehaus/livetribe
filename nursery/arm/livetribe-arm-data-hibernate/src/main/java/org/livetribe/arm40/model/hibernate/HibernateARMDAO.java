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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import org.livetribe.arm40.model.ARMDAO;
import org.livetribe.arm40.model.ApplicationDefinition;
import org.livetribe.arm40.model.IdentityProperties;
import org.livetribe.arm40.model.IdentityPropertiesTransaction;


/**
 * @version $Revision$ $Date$
 */
public class HibernateARMDAO extends HibernateDaoSupport implements ARMDAO
{
    public List getIdentityPropertiesByName(String[] idNames, String[] idValues, String[] ctxNames)
    {
        return getIdentityPropertiesByNamesAndType(idNames, idValues, ctxNames, "IdentityProperties", null);
    }

    public IdentityProperties getIdentityPropertiesByOID(String oid)
    {
        return (IdentityProperties) getHibernateTemplate().load(IdentityProperties.class, oid);
    }

    public void saveIdentityProperties(IdentityProperties identityProperties)
    {
        if (identityProperties == null) throw new IllegalArgumentException("IdentityProperties is null");

        getHibernateTemplate().save(identityProperties);
    }

    public void deleteIdentityProperties(IdentityProperties identityProperties)
    {
        if (identityProperties == null) throw new IllegalArgumentException("IdentityProperties is null");

        getHibernateTemplate().delete(identityProperties);
    }

    public List getIdentityPropertiesTransactionByName(String[] idNames, String[] idValues, String[] ctxNames, String uriValue)
    {
        return getIdentityPropertiesByNamesAndType(idNames, idValues, ctxNames, "IdentityPropertiesTransaction", uriValue);
    }

    public IdentityPropertiesTransaction getIdentityPropertiesTransactionByOID(String oid)
    {
        return (IdentityPropertiesTransaction) getHibernateTemplate().load(IdentityPropertiesTransaction.class, oid);
    }

    public void saveIdentityPropertiesTransaction(IdentityPropertiesTransaction identityPropertiesTransaction)
    {
        if (identityPropertiesTransaction == null)
            throw new IllegalArgumentException("IdentityPropertiesTransaction is null");

        getHibernateTemplate().save(identityPropertiesTransaction);
    }

    public void deleteIdentityPropertiesTransaction(IdentityPropertiesTransaction identityPropertiesTransaction)
    {
        if (identityPropertiesTransaction == null)
            throw new IllegalArgumentException("IdentityPropertiesTransaction is null");

        getHibernateTemplate().delete(identityPropertiesTransaction);
    }

    protected List getIdentityPropertiesByNamesAndType(String[] idNames, String[] idValues, String[] ctxNames, String type, String uriValue)
    {
        idNames = (idNames == null ? new String[0] : idNames);
        idValues = (idValues == null ? new String[0] : idValues);
        ctxNames = (ctxNames == null ? new String[0] : ctxNames);

        boolean first = true;
        StringBuffer query = new StringBuffer();

        query.append("from ");
        query.append(type);
        query.append(" idp where");

        DecimalFormat dec = new DecimalFormat();
        dec.setMinimumIntegerDigits(2);

        List names = new ArrayList();
        List values = new ArrayList();
        int length = Math.min(20, Math.min(idNames.length, idValues.length));
        for (int i = 0; i < length; i++)
        {
            String index = dec.format(i + 1);

            if ((idNames[i] != null && idNames[i].trim().length() > 0)
                && (idValues[i] != null && idValues[i].trim().length() > 0))
            {
                if (!first) query.append(" and");
                else first = false;

                String idName = "IDN" + index;
                String idValue = "IDV" + index;

                names.add(idName);
                values.add(idNames[i]);
                query.append(" idp.idName").append(index).append("=:").append(idName);

                names.add(idValue);
                values.add(idValues[i]);
                query.append(" and idp.idValue").append(index).append("=:").append(idValue);
            }
        }

        for (int i = 0; i < ctxNames.length; i++)
        {
            String index = dec.format(i + 1);

            if ((ctxNames[i] != null && ctxNames[i].trim().length() > 0))
            {
                if (!first) query.append(" and");
                else first = false;

                String ctxName = "CTX" + index;

                names.add(ctxName);
                values.add(ctxNames[i]);
                query.append(" idp.ctxName").append(index).append("=:").append(ctxName);
            }
        }

        if (uriValue != null)
        {
            if (!first) query.append(" and");
            names.add("URI");
            values.add(uriValue);
            query.append(" idp.uriValue=:URI");
        }

        return getHibernateTemplate().findByNamedParam(query.toString(),
                                                       (String[]) names.toArray(new String[names.size()]),
                                                       values.toArray());
    }

    public ApplicationDefinition getApplicationDefinitionByName(String name, IdentityProperties identityProperties)
    {
        List ids = getIdentityPropertiesByName(identityProperties.getIdNames(), identityProperties.getIdValues(), identityProperties.getCtxNames());
        if (ids.size() == 0) return null;

        identityProperties = (IdentityProperties) ids.get(0);
        return null;  //todo: consider this autogenerated code
    }

    public ApplicationDefinition getApplicationDefinitionByOID(String oid)
    {
        return null;  //todo: consider this autogenerated code
    }

    public ApplicationDefinition getApplicationDefinitionById(byte[] id)
    {
        return (ApplicationDefinition) getHibernateTemplate().load(ApplicationDefinition.class, new Integer(1));
    }

    public void saveApplicaitonDefinition(ApplicationDefinition applicationDefinition)
    {
        //todo: consider this autogenerated code
    }

    public void deleteApplicaitonDefinition(ApplicationDefinition applicationDefinition)
    {
        //todo: consider this autogenerated code
    }
}
