/*
 * Copyright 2006 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.livetribe.slp.spi.da;

import org.livetribe.slp.Attributes;
import org.livetribe.slp.Scopes;
import org.livetribe.slp.spi.msg.DAAdvert;

/**
 * A POJO that holds information about an SLP DirectoryAgent.
 * <br />
 * This class is not thread safe, and should be created and used within the same thread.
 * <br />
 * DirectoryAgentInfos are used as key in hash data structures, so they should implement
 * {@link #equals(Object)} and {@link #hashCode()} correctly. Since there can be only
 * one DirectoryAgent per host, the host address or a string containing it should be used.
 * @version $Rev$ $Date$
 */
public class DirectoryAgentInfo
{
    private final Attributes attributes;
    private final long bootTime;
    private final String language;
    private final Scopes scopes;
    private final String url;
    private final String host;

    public static DirectoryAgentInfo from(DAAdvert daAdvert)
    {
        return new DirectoryAgentInfo(daAdvert.getAttributes(), daAdvert.getBootTime(), daAdvert.getLanguage(), daAdvert.getScopes(), daAdvert.getURL());
    }

    private DirectoryAgentInfo(Attributes attributes, long bootTime, String language, Scopes scopes, String url)
    {
        this.attributes = attributes;
        this.bootTime = bootTime;
        this.language = language;
        this.scopes = scopes;
        this.url = url;
        this.host = parseHost(url);
    }

    private String parseHost(String url)
    {
        String authoritySeparator = "://";
        int index = url.indexOf(authoritySeparator);
        if (index < 0) throw new IllegalArgumentException("DirectoryAgent URL is malformed: " + url);
        String host = url.substring(index + authoritySeparator.length());
        if (host.trim().length() == 0) throw new IllegalArgumentException("DirectoryAgent URL is malformed: " + url);
        return host;
    }

    public Attributes getAttributes()
    {
        return attributes;
    }

    public long getBootTime()
    {
        return bootTime;
    }

    public String getLanguage()
    {
        return language;
    }

    public Scopes getScopes()
    {
        return scopes;
    }

    /**
     * Returns true if at least one of the given scopes is also a scope of this DirectoryAgentInfo
     */
    public boolean matchScopes(Scopes others)
    {
        if (others == null) return false;
        return getScopes().weakMatch(others);
    }

    public String getHost()
    {
        return host;
    }

    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        final DirectoryAgentInfo that = (DirectoryAgentInfo)obj;
        return url.equals(that.url);
    }

    public int hashCode()
    {
        return url.hashCode();
    }
}
