/**
 *
 * Copyright 2008-2011 (C) The original author or authors
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
package org.livetribe.s3.model;

/**
 * @version $Revision$ $Date$
 */
public class Grant
{
    private Grantee grantee;
    private Permission permission;

    public Grant()
    {
    }

    public Grant(Grantee grantee, Permission permission)
    {
        this.grantee = grantee;
        this.permission = permission;
    }

    public Grantee getGrantee()
    {
        return grantee;
    }

    public void setGrantee(Grantee grantee)
    {
        this.grantee = grantee;
    }

    public Permission getPermission()
    {
        return permission;
    }

    public void setPermission(Permission permission)
    {
        this.permission = permission;
    }
}
