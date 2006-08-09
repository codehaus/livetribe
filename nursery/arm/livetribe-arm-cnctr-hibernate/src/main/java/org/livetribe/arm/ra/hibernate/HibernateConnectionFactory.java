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
package org.livetribe.arm.ra.hibernate;

import javax.resource.Referenceable;
import javax.naming.Reference;
import javax.naming.NamingException;
import java.io.Serializable;

import org.livetribe.arm.connection.Connection;
import org.livetribe.arm.connection.ConnectionException;
import org.livetribe.arm.connection.ConnectionFactory;


/**
 * @version $Revision$ $Date$
 */
public class HibernateConnectionFactory implements ConnectionFactory, Referenceable, Serializable
{
    private final HibernateConnectionRequestInfo info;
    private Reference reference;

    public HibernateConnectionFactory(HibernateConnectionRequestInfo info)
    {
        this.info = info;
    }

    public Connection createConnection() throws ConnectionException
    {
        return null;  //todo: consider this autogenerated code
    }

    public Connection createConnection(String userName, String password) throws ConnectionException
    {
        return null;  //todo: consider this autogenerated code
    }

    public void setReference(Reference reference)
    {
        this.reference = reference;
    }

    public Reference getReference() throws NamingException
    {
        if (reference == null) throw new NamingException("Reference is null");
        return reference;
    }
}