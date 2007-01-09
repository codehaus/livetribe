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
 * @version $Revision: $ $Date: $
 */
public interface ScriptEngine
{
    public final static String ARGV = "javax.script.argv";
    public final static String FILENAME = "javax.script.filename";
    public final static String ENGINE = "javax.script.engine";
    public final static String ENGINE_VERSION = "javax.script.engine_version";
    public final static String NAME = "javax.script.name";
    public final static String LANGUAGE = "javax.script.language";
    public final static String LANGUAGE_VERSION = "javax.script.language_version";

    public Object eval(String script, ScriptContext context) throws ScriptException;

    public Object eval(Reader reader, ScriptContext context) throws ScriptException;

    public Object eval(String script) throws ScriptException;

    public Object eval(Reader reader) throws ScriptException;

    public Object eval(String script, Bindings bindings) throws ScriptException;

    public Object eval(Reader reader, Bindings bindings) throws ScriptException;

    public Object get(String key);

    public Bindings getBindings(int scope);

    public void setBindings(Bindings bindings, int scope);

    public Bindings createBindings();

    public ScriptContext getContext();

    public void setContext(ScriptContext context);

    public ScriptEngineFactory getFactory();
}
