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
package org.livetribe.forma.ui.editor;

import java.net.URI;

import org.livetribe.forma.ui.PartContainer;
import org.livetribe.forma.ui.action.ActionContext;
import org.livetribe.forma.ui.editor.spi.EditorInfo;
import org.livetribe.forma.ui.editor.spi.EditorSourceInfo;

/**
 * @version $Rev$ $Date$
 */
public interface EditorManager
{
    public static final String ID = EditorManager.class.getName();

    public void spiAddEditorSourceInfo(EditorSourceInfo sourceInfo);

    public void spiAddEditorInfo(EditorInfo editorInfo);

    public Editor displayEditor(URI uri, ActionContext context, PartContainer container);
}
