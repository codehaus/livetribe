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

/**
 * @version $Revision: $ $Date: $
 */
public abstract class CompiledScript
{
    public CompiledScript()
    {
    }

    public Object eval() throws ScriptException
    {
        return eval(getEngine().getContext());
    }

    public Object eval(Bindings bindings) throws ScriptException
    {
        ScriptEngine engine = getEngine();
        SimpleScriptContext context = new SimpleScriptContext();

        context.setBindings(bindings, ScriptContext.ENGINE_SCOPE);
        context.setBindings(engine.getBindings(ScriptContext.GLOBAL_SCOPE), ScriptContext.GLOBAL_SCOPE);

        context.setReader(engine.getContext().getReader());
        context.setWriter(engine.getContext().getWriter());

        return eval(context);
    }

    public abstract Object eval(ScriptContext context) throws ScriptException;

    public abstract ScriptEngine getEngine();
}
