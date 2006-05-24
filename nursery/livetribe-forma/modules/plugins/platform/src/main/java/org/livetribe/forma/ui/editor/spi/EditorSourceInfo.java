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
package org.livetribe.forma.ui.editor.spi;

import java.util.HashSet;
import java.util.Set;

import org.livetribe.forma.AbstractInfo;
import org.livetribe.forma.PartiallyOrdered;

/**
 * @version $Rev$ $Date$
 */
public class EditorSourceInfo extends AbstractInfo implements PartiallyOrdered
{
    private String editorSourceClassName;
    private final Set<String> specializations = new HashSet<String>();

    public String getElementId()
    {
        return getEditorSourceClassName();
    }

    public String getEditorSourceClassName()
    {
        return editorSourceClassName;
    }

    public void setEditorSourceClassName(String editorSourceClassName)
    {
        this.editorSourceClassName = editorSourceClassName;
    }

    public void addSpecializedEditorSource(String specializedEditorSourceClassName)
    {
        specializations.add(specializedEditorSourceClassName);
    }

    public Set<String> getLesserElements()
    {
        return specializations;
    }
}
