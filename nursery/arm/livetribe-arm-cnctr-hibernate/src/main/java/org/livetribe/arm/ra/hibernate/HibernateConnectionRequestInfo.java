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
import java.io.Serializable;


/**
 * @version $Revision$ $Date$
 */
public class HibernateConnectionRequestInfo implements ConnectionRequestInfo, Serializable, Cloneable
{
    private String dialect;
    private String driverClass;
    private String url;
    private String username;
    private String password;
    private Integer poolSize;
    private String cacheProviderClass;


    public HibernateConnectionRequestInfo()
    {
        this(null, null, null, null, null, null, null);
    }

    public HibernateConnectionRequestInfo(HibernateConnectionRequestInfo that)
    {
        this(that.dialect, that.driverClass, that.url, that.username, that.password, that.poolSize, that.cacheProviderClass);
    }

    public HibernateConnectionRequestInfo(String dialect, String driverClass, String url, String username, String password, Integer poolSize, String cacheProviderClass)
    {
        this.dialect = dialect;
        this.driverClass = driverClass;
        this.url = url;
        this.username = username;
        this.password = password;
        this.poolSize = poolSize;
        this.cacheProviderClass = cacheProviderClass;
    }

    public String getDialect()
    {
        return dialect;
    }

    public void setDialect(String dialect)
    {
        this.dialect = dialect;
    }

    String getDriverClass()
    {
        return driverClass;
    }

    void setDriverClass(String driverClass)
    {
        this.driverClass = driverClass;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public Integer getPoolSize()
    {
        return poolSize;
    }

    public void setPoolSize(Integer poolSize)
    {
        this.poolSize = poolSize;
    }

    public String getCacheProviderClass()
    {
        return cacheProviderClass;
    }

    public void setCacheProviderClass(String cacheProviderClass)
    {
        this.cacheProviderClass = cacheProviderClass;
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
        int rc = 0;

        rc += 29 * dialect.hashCode();
        rc+= 29 *  driverClass.hashCode();
        rc += 29 *  url.hashCode();
        if (username != null) rc += 29 *  username.hashCode();
        if (password != null) rc += 29 *  password.hashCode();
        if (poolSize != null) rc ^= poolSize.hashCode();
        if (cacheProviderClass != null) rc ^= cacheProviderClass.hashCode();

        return rc;
    }

    /**
     * @see javax.resource.spi.ConnectionRequestInfo#equals(java.lang.Object)
     */
    public boolean equals(Object obj)
    {
        if (obj == null) return false;
        if (!getClass().equals(obj.getClass())) return false;

        final HibernateConnectionRequestInfo other = (HibernateConnectionRequestInfo) obj;

        if (notEqual(dialect, other.dialect)) return false;
        if (notEqual(driverClass, other.driverClass)) return false;
        if (notEqual(url, other.url)) return false;
        if (notEqual(username, other.username)) return false;
        if (notEqual(password, other.password)) return false;
        if (notEqual(poolSize, other.poolSize)) return false;
        return !notEqual(cacheProviderClass, other.cacheProviderClass);
    }

    private boolean notEqual(Object x, Object y)
    {
        return (x == null ? y == null : x.equals(y));
    }
}
