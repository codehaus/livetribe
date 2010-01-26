/**
 *
 * Copyright 2006 - 2007 (C) The original author or authors
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

import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * @version $Revision$ $Date$
 */
public class SimpleScriptContext implements ScriptContext
{
    private static final List<Integer> SCOPES = Collections.unmodifiableList(Arrays.asList(ENGINE_SCOPE, GLOBAL_SCOPE));

    protected Bindings globalScope;
    protected Bindings engineScope;

    protected Writer writer;
    protected Writer errorWriter;
    protected Reader reader;

    public SimpleScriptContext()
    {
        engineScope = new SimpleBindings();
        reader = new InputStreamReader(System.in);
        writer = new PrintWriter(System.out, true);
        errorWriter = new PrintWriter(System.err, true);
    }

    public void setBindings(Bindings bindings, int scope)
    {
        switch (scope)
        {
            case ENGINE_SCOPE:
                if (bindings == null) throw new NullPointerException("Bindings for ENGINE scope is null");
                engineScope = bindings;
                break;
            case GLOBAL_SCOPE:
                globalScope = bindings;
                break;
            default:
                throw new IllegalArgumentException("Invalid scope");
        }
    }

    public Bindings getBindings(int scope)
    {
        switch (scope)
        {
            case ENGINE_SCOPE:
                return engineScope;
            case GLOBAL_SCOPE:
                return globalScope;
            default:
                throw new IllegalArgumentException("Invalid scope");
        }
    }

    public void setAttribute(String name, Object value, int scope)
    {
        if (name == null) throw new IllegalArgumentException("Name is null");

        Bindings bindings;
        switch (scope)
        {
            case ENGINE_SCOPE:
                bindings = engineScope;
                break;
            case GLOBAL_SCOPE:
                bindings = globalScope;
                break;
            default:
                throw new IllegalArgumentException("Invalid scope");
        }
        if (bindings != null) {
            bindings.put(name, value);
        }
    }

    public Object getAttribute(String name, int scope)
    {
        if (name == null) throw new IllegalArgumentException("Name is null");

        Bindings bindings;
        switch (scope)
        {
            case ENGINE_SCOPE:
                bindings = engineScope;
                break;
            case GLOBAL_SCOPE:
                bindings = globalScope;
                break;
            default:
                throw new IllegalArgumentException("Invalid scope");
        }

        return (bindings != null) ? bindings.get(name) : null;
    }

    public Object removeAttribute(String name, int scope)
    {
        if (name == null) throw new IllegalArgumentException("Name is null");

        Bindings bindings;
        switch (scope)
        {
            case ENGINE_SCOPE:
                bindings = engineScope;
                break;
            case GLOBAL_SCOPE:
                bindings = globalScope;
                break;
            default:
                throw new IllegalArgumentException("Invalid scope");
        }

        return (bindings != null) ? bindings.remove(name) : null;
    }

    public Object getAttribute(String name)
    {
        if (name == null) throw new IllegalArgumentException("Name is null");

        if (engineScope != null && engineScope.containsKey(name)) return engineScope.get(name);
        if (globalScope != null && globalScope.containsKey(name)) return globalScope.get(name);

        return null;
    }

    public int getAttributesScope(String name)
    {
        if (name == null) throw new IllegalArgumentException("Name is null");

        for (int scope : getScopes())
        {
            if (getBindings(scope) != null && getBindings(scope).containsKey(name))
            {
                return scope;
            }
        }
        return -1;
    }

    public List<Integer> getScopes()
    {
        return SCOPES;
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
