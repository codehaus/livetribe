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

/**
 * Extended by classes that store results of compilations. The state might be
 * stored in the form of Java classes, Java class files or scripting language
 * opcodes. The CompiledScript may be executed repeatedly without reparsing.
 * <p/>
 * Each CompiledScript is associated with a ScriptEngine, a call to whose eval
 * method of the CompiledScript causes the execution of the CompiledScript by
 * the ScriptEngine. Changes in the state of the ScriptEngine caused by
 * execution of the CompiledScript may visible during subsequent executions of
 * scripts by the engine.
 *
 * @version $Revision: $ $Date: $
 */
public abstract class CompiledScript
{
    public CompiledScript()
    {
    }

    /**
     * Executes the program stored in the CompiledScript object. The the
     * GLOBAL_SCOPE and ENGINE_SCOPE of the ScriptEngine associated with this
     * CompiledScript are used to initialize a GenericScriptContext in which
     * the script is executed.
     *
     * @return The return value from the script execution
     * @throws ScriptException if an error occurs.
     */
    public Object eval() throws ScriptException
    {
        GenericScriptContext context = new GenericScriptContext();

        context.setNamespace(getEngine().getNamespace(ScriptContext.GLOBAL_SCOPE), ScriptContext.GLOBAL_SCOPE);
        context.setNamespace(getEngine().getNamespace(ScriptContext.ENGINE_SCOPE), ScriptContext.ENGINE_SCOPE);

        return eval(context);
    }

    /**
     * Executes the program stored in the CompiledScript object using the
     * supplied Namespace of attributes as the SCRIPT_SCOPE. The the
     * GLOBAL_SCOPE and ENGINE_SCOPE of the ScriptEngine associated with this
     * CompiledScript are used to initialize a GenericScriptContext in which
     * the script is executed.
     *
     * @param namespace The namespace of attributes used for the SCRIPT_SCOPE.
     * @return The return value from the script execution
     * @throws ScriptException if an error occurs.
     */
    public java.lang.Object eval(Namespace namespace) throws ScriptException
    {
        GenericScriptContext context = new GenericScriptContext();

        context.setNamespace(namespace, ScriptContext.SCRIPT_SCOPE);
        context.setNamespace(getEngine().getNamespace(ScriptContext.GLOBAL_SCOPE), ScriptContext.GLOBAL_SCOPE);
        context.setNamespace(getEngine().getNamespace(ScriptContext.ENGINE_SCOPE), ScriptContext.ENGINE_SCOPE);

        return eval(context);
    }

    /**
     * Executes the program stored in this CompiledScript object.
     *
     * @param context A ScriptContext which is used in the same way as the
     *                ScriptContext passed to the eval methods of ScriptEngine.
     * @return The value returned by the script execution, if any. Should
     *         return null if no value is returned by the script execution.
     * @throws ScriptException
     */
    public abstract Object eval(ScriptContext context) throws ScriptException;

    /**
     * Returns the ScriptEngine wbose comile method created this CompiledScript.
     * The CompiledScript will execute in this engine.
     *
     * @return The ScriptEngine which created this CompiledScript
     */
    public abstract ScriptEngine getEngine();
}
