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

import javax.resource.spi.ConnectionRequestInfo;
import javax.sql.DataSource;
import java.io.Serializable;


/**
 * @version $Revision$ $Date$
 */
public class HibernateConnectionRequestInfo implements ConnectionRequestInfo, Serializable, Cloneable
{
    private DataSource dataSource;

    public HibernateConnectionRequestInfo()
    {
        this((DataSource) null);
    }

    public HibernateConnectionRequestInfo(HibernateConnectionRequestInfo that)
    {
        this(that.dataSource);
    }

    public HibernateConnectionRequestInfo(DataSource dataSource)
    {
        this.dataSource = dataSource;
    }

    public DataSource getDataSource()
    {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource)
    {
        this.dataSource = dataSource;
    }

    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }

    /**
     * @see javax.resource.spi.ConnectionRequestInfo#hashCode()
     */
    public int hashCode()
    {
        return (dataSource != null ? dataSource.hashCode() : 0);
    }

    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final HibernateConnectionRequestInfo that = (HibernateConnectionRequestInfo) o;

        return !(dataSource != null ? !dataSource.equals(that.dataSource) : that.dataSource != null);
    }
}
