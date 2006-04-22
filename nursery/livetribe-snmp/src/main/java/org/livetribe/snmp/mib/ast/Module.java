/**
 *
 * Copyright 2005 (C) The original author or authors
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
package org.livetribe.snmp.mib.ast;

import java.util.Map;
import java.util.HashMap;


/**
 * @version $Revision: 1.1 $ $Date: 2005/01/26 04:05:03 $
 */
public class Module
{
    private ModuleIdentifier id;
    private TagDefaultEnum tagDefault = TagDefaultEnum.EXPLICIT;
    private boolean extensibilityImplied = false;
    private Map assignments = new HashMap();
    private Map typeAssignments = new HashMap();

    public ModuleIdentifier getModuleIdentifier()
    {
        return id;
    }

    public void setModuleIdentifier( ModuleIdentifier id )
    {
        this.id = id;
    }

    public TagDefaultEnum getTagDefault()
    {
        return tagDefault;
    }

    public void setTagDefault( TagDefaultEnum tagDefault )
    {
        this.tagDefault = tagDefault;
    }

    public boolean isExtensibilityImplied()
    {
        return extensibilityImplied;
    }

    public void setExtensibilityImplied( boolean extensibilityImplied )
    {
        this.extensibilityImplied = extensibilityImplied;
    }

    public void addTypeAssignment( TypeAssignment assignment )
    {
        assignments.put( assignment.getTypeReference(), assignment );
        typeAssignments.put( assignment.getTypeReference(), assignment );
    }

    public void addValueAssignment( ValueAssignment assignment )
    {
        assignments.put( assignment.getValueReference(), assignment );
    }

    public Map getTypeAssignments()
    {
        return typeAssignments;
    }

    public Map getAssignments()
    {
        return assignments;
    }
}
