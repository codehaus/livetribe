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


/**
 * @version $Revision: $ $Date$
 */
public abstract class GenericScriptEngine implements ScriptEngine
{
    protected ScriptContext context;

    public GenericScriptEngine()
    {
        context = new GenericScriptContext();
    }

    public GenericScriptEngine(Namespace namespace)
    {
        context = new GenericScriptContext();
        context.setNamespace(namespace, ScriptContext.ENGINE_SCOPE);
    }

    public ScriptContext getContext()
    {
        return context;
    }

    public void setContext(ScriptContext context)
    {
        this.context = context;
    }

    public Namespace getNamespace(int scope)
    {
        return context.getNamespace(scope);
    }

    public void setNamespace(Namespace namespace, int scope)
    {
        context.setNamespace(namespace, scope);
    }

    public Object get(String key)
    {
        return context.getNamespace(ScriptContext.ENGINE_SCOPE).get(key);
    }

    public void put(String key, Object value)
    {
        if (key == null) throw new IllegalArgumentException("Key is null");

        context.getNamespace(ScriptContext.ENGINE_SCOPE).put(key, value);
    }

    public Object eval(Reader reader) throws ScriptException
    {
        return eval(reader, context);
    }

    public Object eval(String script, Namespace namespace) throws ScriptException
    {
        return eval(script, getScriptContext(namespace));
    }

    public Object eval(String script) throws ScriptException
    {
        return eval(script, context);
    }

    public Object eval(Reader reader, Namespace namespace) throws ScriptException
    {
        return eval(reader, getScriptContext(namespace));
    }

    protected ScriptContext getScriptContext(Namespace namespace)
    {
        ScriptContext result = new GenericScriptContext();

        result.setNamespace(namespace, ScriptContext.ENGINE_SCOPE);
        result.setNamespace(context.getNamespace(ScriptContext.GLOBAL_SCOPE), ScriptContext.GLOBAL_SCOPE);

        result.setReader(context.getReader());
        result.setWriter(context.getWriter());

        return result;
    }
}
