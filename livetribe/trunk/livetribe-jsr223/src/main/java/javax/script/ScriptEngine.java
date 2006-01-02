/**
 *
 * Copyright 2005 (C) The original author or authors
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
 * ScriptEngine is the fundamental interface whose methods must be
 * fully functional in every implementation of this specification.
 * <p/>
 * These methods provide basic scripting functionality. Applications
 * written to this simple interface are expected to work with minimal
 * modifications in every implementation. It includes methods that
 * execute scripts, and ones that set and get values.
 * <p/>
 * The values are key/value pairs of two types. The first type of pairs
 * consists of those whose keys are reserved and defined in this
 * specification or by individual implementations. The values in the pairs
 * with reserved keys have specified meanings.
 * <p/>
 * The other type of pairs consists of those that create Java language
 * Bindings, the values are usually represented in scripts by the
 * corresponding keys or by decorated forms of them.
 *
 * @version $Revision: $ $Date: $
 */
public interface ScriptEngine
{
    /**
     * Reserved key for a named value that passes an array of
     * positional arguments to a script.
     */
    public final static String ARGV = "javax.script.argv";

    /**
     * Reserved key for a named value that is the name of the file
     * being executed.
     */
    public final static String FILENAME = "javax.script.filename";

    /**
     * Reserved key for a named value that is the name of the
     * ScriptEngine implementation.
     */
    public final static String ENGINE = "javax.script.engine";

    /**
     * Reserved key for a named value that identifies the version of
     * the ScriptEngine implementation.
     */
    public final static String ENGINE_VERSION = "javax.script.engine_version";

    /**
     * Reserved key for a named value that identifies the short name
     * of the scripting language. The name is used by the
     * ScriptEngineManager to locate a ScriptEngine with a given
     * name in the getEngineByName method.
     */
    public final static String NAME = "javax.script.name";

    /**
     * Reserved key for a named value that is the full name of
     * Scripting Language supported by the implementation.
     */
    public final static String LANGUAGE = "javax.script.language";

    /**
     * Reserved key for the named value that identifies the version
     * of the scripting language supported by the implementation.
     */
    public final static String LANGUAGE_VERSION = "javax.script.language_version";

    /**
     * Causes the immediate execution of the script whose source is
     * the String passed as the first argument. The script may be
     * reparsed or recompiled before execution. State left in the
     * engine from previous executions, including variable values
     * and compiled procedures may be visible during this execution.
     *
     * @param script  The script to be executed by the script engine.
     * @param context A ScriptContext exposing sets of attributes in
     *                different scopes. The meanings of the scopes
     *                ScriptContext.GLOBAL_SCOPE, and
     *                ScriptContext.ENGINE_SCOPE are defined in the
     *                specification.
     * @return The value returned from the execution of the script.
     * @throws ScriptException      if an error occurrs. ScriptEngines
     *                              should create and throw ScriptException wrappers for
     *                              checked Exceptions thrown by underlying scripting
     *                              implementations.
     * @throws NullPointerException if either argument is null.
     */
    public Object eval(String script, ScriptContext context) throws ScriptException;

    /**
     * Same as <code>eval(String, ScriptContext)</code> where the source of the
     * script is read from a Reader.
     *
     * @param reader  The source of the script to be executed by the
     *                script engine.
     * @param context The ScriptContext passed to the script engine.
     * @return The ScriptContext passed to the script engine.
     * @throws ScriptException      if an error occurrs.
     * @throws NullPointerException if either argument is null.
     */
    public Object eval(Reader reader, ScriptContext context) throws ScriptException;

    /**
     * Executes the specified script. The default ScriptContext for
     * the ScriptEngine is used.
     *
     * @param script The script language source to be executed.
     * @return The value returned from the execution of the script.
     * @throws ScriptException      if an error occurrs.
     * @throws NullPointerException if the argument is null.
     */
    public Object eval(String script) throws ScriptException;

    /**
     * Same as <code>eval(String)</code> except that the source of the script is
     * provided as a Reader
     *
     * @param reader The source of the script.
     * @return The value returned by the script.
     * @throws ScriptException      if an error occurrs.
     * @throws NullPointerException if the argument is null.
     */
    public Object eval(Reader reader) throws ScriptException;

    /**
     * Executes the script using the Namespace argument as the
     * ENGINE_SCOPE Namespace of the ScriptEngine during the script
     * execution. The Reader, Writer and non-ENGINE_SCOPE Namespaces
     * of the default ScriptContext are used. The ENGINE_SCOPE
     * Namespace of the ScriptEngine is not changed, and its
     * mappings are unaltered by the script execution.
     *
     * @param script    The source for the script.
     * @param namespace The Namespace of attributes to be used for script
     *                  execution.
     * @return The value returned by the script.
     * @throws ScriptException      if an error occurrs.
     * @throws NullPointerException if either argument is null.
     */
    public Object eval(String script, Namespace namespace) throws ScriptException;

    /**
     * Same as eval(String, Namespace) except that the source of the
     * script is provided as a Reader.
     *
     * @param reader    The source of the script.
     * @param namespace The source of the script.
     * @return The value returned by the script.
     * @throws ScriptException      if an error occurrs.
     * @throws NullPointerException if either argument is null.
     */
    public Object eval(Reader reader, Namespace namespace) throws ScriptException;

    /**
     * Retrieves a value set in the state of this engine. The value
     * might be one which was set using setValue or some other value
     * in the state of the ScriptEngine, depending on the
     * implementation. Must have the same effect as getNamespace
     * (ScriptContext.ENGINE_SCOPE).get
     *
     * @param key The key whose value is to be returned
     * @return The key whose value is to be returned
     */
    public Object get(String key);

    /**
     * Returns a scope of named values. The possible scopes are:
     * <ul>
     * <li>ScriptContext.GLOBAL_SCOPE - A set of named values
     * shared by multiple ScriptEngines If the ScriptEngine is
     * created by a ScriptEngineManager, a reference to the
     * global scope stored by the ScriptEngineManager should
     * be returned. May return null if no global scope is
     * associated with this ScriptEngine.</li>
     * <li>ScriptContext.ENGINE_SCOPE - The set of named values
     * representing the state of this ScriptEngine. The values
     * are generally visible in scripts using the associated
     * keys as variable names.</li>
     * <li>Any other value of scope defined in the default
     * ScriptContext of the ScriptEngine.</li>
     * </ul>
     * The Namespace instances that are returned must be identical
     * to those returned by the getNamespace method of ScriptContext
     * called with corresponding arguments on the default
     * ScriptContext of the ScriptEngine.
     *
     * @param scope Either ScriptContext.ENGINE_SCOPE or
     *              ScriptContext.GLOBAL_SCOPE which specifies the Namespace
     *              to return. Implementations of ScriptContext may define
     *              additional scopes. If the default ScriptContext of the
     *              ScriptEngine defines additional scopes, any of them can
     *              be passed to get the corresponding Namespace.
     * @return The Namespace with the specified scope.
     * @throws IllegalArgumentException if specified scope is invalid
     */
    public Namespace getNamespace(int scope);

    /**
     * Sets a scope of named values to be used by scripts.
     * <p/>
     * The method must have the same effect as calling the
     * setNamespace method of ScriptContext with the corresponding
     * value of scope on the default ScriptContext of the
     * ScriptEngine.
     *
     * @param namespace The Namespace for the specified scope.
     * @param scope     The specified scope. Either
     *                  ScriptContext.ENGINE_SCOPE, ScriptContext.GLOBAL_SCOPE,
     *                  or any other valid value of scope.
     * @throws IllegalArgumentException if specified scope is invalid
     */
    public void setNamespace(Namespace namespace, int scope);

    /**
     * Returns an uninitialized Namespace.
     *
     * @return A Namespace that can be used to replace the state of
     *         this ScriptEngine.
     */
    public Namespace createNamespace();

    /**
     * Returns the default ScriptContext of the ScriptEngine whose
     * Namespaces, Reader and Writers are used for script executions
     * when no ScriptContext is specified.
     *
     * @return The default ScriptContext of the ScriptEngine.
     */
    public ScriptContext getContext();

    /**
     * Sets the default ScriptContext of the ScriptEngine whose
     * Namespaces, Reader and Writers are used for script executions
     * when no ScriptContext is specified.
     *
     * @param context A ScriptContext that will replace the
     *                default ScriptContext in the ScriptEngine.
     */
    public void setContext(ScriptContext context);

    /**
     * Returns a ScriptEngineFactory for the class to which this
     * ScriptEngine belongs. The returned ScriptEngineFactory
     * implements ScriptEngineInfo, which describes attributes of
     * this ScriptEngine implementation.
     *
     * @return The ScriptEngineFactory
     */
    public ScriptEngineFactory getFactory();
}
