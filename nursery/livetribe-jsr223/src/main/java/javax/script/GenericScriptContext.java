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
 * @version $Revision: $ $Date$
 */
public class GenericScriptContext implements ScriptContext
{
    protected Namespace globalScope;
    protected Namespace engineScope;

    protected Writer writer;
    protected Writer errorWriter;
    protected Reader reader;

    public GenericScriptContext()
    {
    }

    public void setNamespace(Namespace namespace, int scope)
    {
        switch (scope)
        {
            case ENGINE_SCOPE:
                if (namespace == null) throw new NullPointerException("Namespace for ENGINE scope is null");
                engineScope = namespace;
                break;
            case GLOBAL_SCOPE:
                globalScope = namespace;
                break;
            default:
                throw new IllegalArgumentException("Invaild scope");
        }
    }

    public Namespace getNamespace(int scope)
    {
        switch (scope)
        {
            case ENGINE_SCOPE:
                return engineScope;
            case GLOBAL_SCOPE:
                return globalScope;
            default:
                throw new IllegalArgumentException("Invaild scope");
        }
    }

    public void setAttribute(String name, Object value, int scope)
    {
        if (name == null) throw new IllegalArgumentException("Name is null");

        Namespace namespace;
        switch (scope)
        {
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

    public Object getAttribute(String name, int scope)
    {
        if (name == null) throw new IllegalArgumentException("Name is null");

        Namespace namespace;
        switch (scope)
        {
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

    public Object removeAttribute(String name, int scope)
    {
        if (name == null) throw new IllegalArgumentException("Name is null");

        Namespace namespace;
        switch (scope)
        {
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

    public Object getAttribute(String name)
    {
        if (name == null) throw new IllegalArgumentException("Name is null");

        if (engineScope != null && engineScope.containsKey(name)) return engineScope.get(name);
        if (globalScope != null && globalScope.containsKey(name)) return globalScope.get(name);

        return null;
    }

    public int getAttributeScope(String name)
    {
        if (name == null) throw new IllegalArgumentException("Name is null");

        if (engineScope != null && engineScope.containsKey(name)) return ENGINE_SCOPE;
        if (globalScope != null && globalScope.containsKey(name)) return GLOBAL_SCOPE;

        return -1;
    }

    public Writer getWriter()
    {
        return writer;
    }

    public Writer getErrorWriter()
    {
        return errorWriter;
    }

    public void setWriter(Writer writer)
    {
        this.writer = writer;
    }

    public void setErrorWriter(Writer writer)
    {
        this.errorWriter = writer;
    }

    public Reader getReader()
    {
        return reader;
    }

    public void setReader(Reader reader)
    {
        this.reader = reader;
    }
}
