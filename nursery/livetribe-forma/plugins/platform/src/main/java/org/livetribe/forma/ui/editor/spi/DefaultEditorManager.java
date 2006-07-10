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

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.livetribe.forma.AbstractInfo;
import org.livetribe.forma.ui.PartContainer;
import org.livetribe.forma.ui.Context;
import org.livetribe.forma.ui.editor.Editor;
import org.livetribe.forma.ui.editor.EditorException;
import org.livetribe.forma.ui.editor.EditorManager;
import org.livetribe.forma.ui.editor.EditorSource;
import org.livetribe.ioc.Container;
import org.livetribe.ioc.Inject;

/**
 * @version $Rev$ $Date$
 */
public class DefaultEditorManager implements EditorManager
{
    @Inject private Container containerManager;
    private final Map<String, EditorSourceInfo> editorSourceInfos = new HashMap<String, EditorSourceInfo>();
    private final List<EditorSource> editorSources = new ArrayList<EditorSource>();
    private final Map<String, EditorInfo> editorInfos = new HashMap<String, EditorInfo>();
    private final Map<URI, Editor> editors = new HashMap<URI, Editor>();

    public void spiAddEditorSourceInfo(EditorSourceInfo sourceInfo)
    {
        editorSourceInfos.put(sourceInfo.getEditorSourceClassName(), sourceInfo);
    }

    public void spiAddEditorInfo(EditorInfo editorInfo)
    {
        editorInfos.put(editorInfo.getEditorClassName(), editorInfo);
    }

    public Editor displayEditor(URI uri, Context context, PartContainer container)
    {
        Editor editor = editors.get(uri);
        if (editor == null)
        {
            String editorClassName = findEditorClassName(uri, context);
            if (editorClassName != null)
            {
                EditorInfo editorInfo = editorInfos.get(editorClassName);
                if (editorInfo != null)
                {
                    editor = (Editor)newInstance(editorInfo.getEditorClassName());
                    containerManager.resolve(editor);
                    editors.put(uri, editor);
                }
            }
        }

        if (editor != null)
        {
            editor.spiDisplayIn(container, null);
            editor.spiOpen(uri, context);
        }

        return editor;
    }

    private String findEditorClassName(URI uri, Context context)
    {
        if (editorSources.isEmpty()) initializeEditorSources();
        for (EditorSource editorSource : editorSources)
        {
            String editorClassName = editorSource.spiGetEditorClassName(uri, context);
            if (editorClassName != null) return editorClassName;
        }
        return null;
    }

    private void initializeEditorSources()
    {
        List<EditorSourceInfo> sortedSourceInfos = AbstractInfo.sort(editorSourceInfos);
        Collections.reverse(sortedSourceInfos);
        for (EditorSourceInfo editorSourceInfo : sortedSourceInfos)
        {
            String editorSourceClassName = editorSourceInfo.getEditorSourceClassName();
            EditorSource editorSource = (EditorSource)newInstance(editorSourceClassName);
            editorSources.add(editorSource);
        }
    }

    private Object newInstance(String className)
    {
        try
        {
            return Thread.currentThread().getContextClassLoader().loadClass(className).newInstance();
        }
        catch (Exception x)
        {
            throw new EditorException(x);
        }
    }
}
