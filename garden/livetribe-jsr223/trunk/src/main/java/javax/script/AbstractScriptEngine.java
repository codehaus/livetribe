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

import java.io.Reader;


/**
 * @version $Revision: $ $Date$
 */
public abstract class AbstractScriptEngine implements ScriptEngine
{
    protected ScriptContext context;

    public AbstractScriptEngine()
    {
        context = new SimpleScriptContext();
    }

    public AbstractScriptEngine(Bindings bindings)
    {
        context = new SimpleScriptContext();
        context.setBindings(bindings, ScriptContext.ENGINE_SCOPE);
    }

    public ScriptContext getContext()
    {
        return context;
    }

    public void setContext(ScriptContext context)
    {
        this.context = context;
    }

    public Bindings getBindings(int scope)
    {
        return context.getBindings(scope);
    }

    public void setBindings(Bindings bindings, int scope)
    {
        context.setBindings(bindings, scope);
    }

    public Object get(String key)
    {
        return context.getBindings(ScriptContext.ENGINE_SCOPE).get(key);
    }

    public void put(String key, Object value)
    {
        if (key == null) throw new IllegalArgumentException("Key is null");

        context.getBindings(ScriptContext.ENGINE_SCOPE).put(key, value);
    }

    public Object eval(Reader reader) throws ScriptException
    {
        return eval(reader, context);
    }

    public Object eval(String script, Bindings bindings) throws ScriptException
    {
        return eval(script, getScriptContext(bindings));
    }

    public Object eval(String script) throws ScriptException
    {
        return eval(script, context);
    }

    public Object eval(Reader reader, Bindings bindings) throws ScriptException
    {
        return eval(reader, getScriptContext(bindings));
    }

    protected ScriptContext getScriptContext(Bindings bindings)
    {
        ScriptContext result = new SimpleScriptContext();

        result.setBindings(bindings, ScriptContext.ENGINE_SCOPE);
        result.setBindings(context.getBindings(ScriptContext.GLOBAL_SCOPE), ScriptContext.GLOBAL_SCOPE);

        result.setReader(context.getReader());
        result.setWriter(context.getWriter());

        return result;
    }
}
