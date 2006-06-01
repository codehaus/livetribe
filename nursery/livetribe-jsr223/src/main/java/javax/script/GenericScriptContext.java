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
package javax.script;

import java.io.Reader;
import java.io.Writer;


/**
 * Generic implementation of ScriptContext.
 *
 * @version $Revision: $ $Date$
 */
public class GenericScriptContext implements ScriptContext
{
    /**
     * Namespace of attributes storing GLOBAL_SCOPE
     */
    protected Namespace globalScope;

    /**
     * Namespace of attributes storing SCRIPT_SCOPE
     */
    protected Namespace scriptScope;

    /**
     * Namespace of attributes storing ENGINE_SCOPE.
     */
    protected Namespace engineScope;

    protected Writer writer;
    protected Writer errorWriter;
    protected Reader reader;

    /**
     * {@inheritDoc}
     */
    public void setNamespace(Namespace namespace, int scope)
    {
        if (namespace == null) throw new IllegalArgumentException("Namespace is null");

        switch (scope)
        {
            case SCRIPT_SCOPE:
                scriptScope = namespace;
                break;
            case ENGINE_SCOPE:
                engineScope = namespace;
                break;
            case GLOBAL_SCOPE:
                globalScope = namespace;
                break;
            default:
                throw new IllegalArgumentException("Invaild scope");
        }
    }

    /**
     * {@inheritDoc}
     */
    public Namespace getNamespace(int scope)
    {
        switch (scope)
        {
            case SCRIPT_SCOPE:
                return scriptScope;
            case ENGINE_SCOPE:
                return engineScope;
            case GLOBAL_SCOPE:
                return globalScope;
            default:
                throw new IllegalArgumentException("Invaild scope");
        }
    }

    /**
     * {@inheritDoc}
     */
    public void setAttribute(String name, Object value, int scope)
    {
        if (name == null) throw new IllegalArgumentException("Name is null");

        Namespace namespace;
        switch (scope)
        {
            case SCRIPT_SCOPE:
                namespace = scriptScope;
                break;
            case ENGINE_SCOPE:
                namespace = engineScope;
                break;
            case GLOBAL_SCOPE:
                namespace = globalScope;
                break;
            default:
                throw new IllegalArgumentException("Invaild scope");
        }
        if (namespace == null) throw new IllegalArgumentException("Namespace not defined");

        namespace.put(name, value);
    }

    /**
     * {@inheritDoc}
     */
    public Object getAttribute(String name, int scope)
    {
        if (name == null) throw new IllegalArgumentException("Name is null");

        Namespace namespace;
        switch (scope)
        {
            case SCRIPT_SCOPE:
                namespace = scriptScope;
                break;
            case ENGINE_SCOPE:
                namespace = engineScope;
                break;
            case GLOBAL_SCOPE:
                namespace = globalScope;
                break;
            default:
                throw new IllegalArgumentException("Invaild scope");
        }
        if (namespace == null) throw new IllegalArgumentException("Namespace not defined");

        return namespace.get(name);
    }

    /**
     * {@inheritDoc}
     */
    public Object removeAttribute(String name, int scope)
    {
        if (name == null) throw new IllegalArgumentException("Name is null");

        Namespace namespace;
        switch (scope)
        {
            case SCRIPT_SCOPE:
                namespace = scriptScope;
                break;
            case ENGINE_SCOPE:
                namespace = engineScope;
                break;
            case GLOBAL_SCOPE:
                namespace = globalScope;
                break;
            default:
                throw new IllegalArgumentException("Invaild scope");
        }
        if (namespace == null) throw new IllegalArgumentException("Namespace not defined");

        return namespace.remove(name);
    }

    /**
     * {@inheritDoc}
     */
    public Object getAttribute(String name)
    {
        if (name == null) throw new IllegalArgumentException("Name is null");

        if (scriptScope != null && scriptScope.containsKey(name)) return scriptScope.get(name);
        if (engineScope != null && engineScope.containsKey(name)) return engineScope.get(name);
        if (globalScope != null && globalScope.containsKey(name)) return globalScope.get(name);

        return null;
    }

    /**
     * {@inheritDoc}
     */
    public int getAttributeScope(String name)
    {
        if (name == null) throw new IllegalArgumentException("Name is null");

        if (scriptScope != null && scriptScope.containsKey(name)) return SCRIPT_SCOPE;
        if (engineScope != null && engineScope.containsKey(name)) return ENGINE_SCOPE;
        if (globalScope != null && globalScope.containsKey(name)) return GLOBAL_SCOPE;

        return -1;
    }

    /**
     * {@inheritDoc}
     */
    public Writer getWriter()
    {
        return writer;
    }

    /**
     * {@inheritDoc}
     */
    public Writer getErrorWriter()
    {
        return errorWriter;
    }

    /**
     * {@inheritDoc}
     */
    public void setWriter(Writer writer)
    {
        this.writer = writer;
    }

    /**
     * {@inheritDoc}
     */
    public void setErrorWriter(Writer writer)
    {
        this.errorWriter = writer;
    }

    /**
     * {@inheritDoc}
     */
    public Reader getReader()
    {
        return reader;
    }

    /**
     * {@inheritDoc}
     */
    public void setReader(Reader reader)
    {
        this.reader = reader;
    }
}
