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
import java.io.Writer;

/**
 * The interface whose implementing classes are used to connect Script
 * Engines with objects, such as scoped Namespaces, in hosting
 * applications. Each scope is a set of named attributes whose values can
 * be set and retrieved using the ScriptContext methods. ScriptContexts
 * also expose Readers and Writers that can be used by the ScriptEngines
 * for input and output.
 *
 * @version $Revision: $ $Date: $
 */
public interface ScriptContext
{
    /**
     * EngineScope attributes are visible during the lifetime of a
     * single ScriptEngine and a set of attributes is maintained for
     * each engine.
     */
    public final static int ENGINE_SCOPE = 0;

    /**
     * GlobalScope attributes are visible to all engines in
     * associated with a ScriptEngineManager.
     */
    public final static int GLOBAL_SCOPE = 1;

    /**
     * ScriptScope attributes are visible during the execution of a single
     * script.
     */
    public final static int SCRIPT_SCOPE = 2;

    /**
     * Associates a Namespace instance with a particular scope in
     * this ScriptContext. Calls to the getAttribute and
     * setAttribute methods must map to the put and get methods of
     * the Namespace for the specified scope.
     *
     * @param namespace The Namespace to associate with the given
     *                  scope
     * @param scope     The scope
     * @throws IllegalArgumentException if no Namespace is defined
     *                                  for the specified scope value
     *                                  in ScriptContexts of this type.
     * @throws NullPointerException     if value of scope is ENGINE_SCOPE
     *                                  and the specified Namespace is null.
     */
    public void setNamespace(Namespace namespace, int scope);

    /**
     * Gets the Namespace associated with the given scope in this
     * ScriptContext.
     *
     * @param scope The scope
     * @return The associated Namespace. Returns null if it has not
     *         been set.
     * @throws IllegalArgumentException if no Namespace is defined
     *                                  for the specified scope value
     *                                  in ScriptContext of this type.
     */
    public Namespace getNamespace(int scope);

    /**
     * Sets the value of an attribute in a given scope.
     *
     * @param name  The name of the attribute to set.
     * @param value The value
     * @param scope The scope in which to set the attribute
     * @throws IllegalArgumentException if scope is invalid.
     * @throws NullPointerException     if name is null.
     */
    public void setAttribute(String name, Object value, int scope);

    /**
     * Gets the value of an attribute in a given scope.
     *
     * @param name  The name of the attribute to retrieve.
     * @param scope The scope in which to retrieve the attribute.
     * @return The value of the attribute. Returns null is the name
     *         does not exist in the given scope.
     * @throws IllegalArgumentException if scope is invalid.
     * @throws NullPointerException     if name is null.
     */
    public Object getAttribute(String name, int scope);

    /**
     * Remove an attribute in a given scope.
     *
     * @param name
     * @param scope
     * @return The removed item.
     * @throws IllegalArgumentException if scope is invalid.
     * @throws NullPointerException     if name is null.
     */
    public Object removeAttribute(String name, int scope);

    /**
     * Retrieves the value of the attribute with the given name in
     * the scope occurring earliest in the search order. The order
     * is determined by the numeric value of the scope parameter
     * (lowest scope values first.)
     *
     * @param name The name of the the attribute to retrieve.
     * @return The value of the attribute in the lowest scope for which
     *         an attribute with the given name is defined. Returns
     *         null if no attribute with the name exists in any scope.
     * @throws NullPointerException if name is null.
     */
    public Object getAttribute(String name);

    /**
     * Get the lowest scope in which an attribute is defined.
     *
     * @param name Name of the attribute.
     * @return The lowest scope. Returns -1 if no attribute with the
     *         given name is defined in any scope.
     */
    public int getAttributeScope(String name);

    /**
     * Returns the Writer for scripts to use when displaying output.
     *
     * @return The Writer.
     */
    public Writer getWriter();

    /**
     * Returns the Writer used to display error output.
     *
     * @return The Writer.
     */
    public Writer getErrorWriter();

    /**
     * Sets the Writer for scripts to use when displaying output.
     *
     * @param writer Sets the Writer for scripts to use when displaying output.
     */
    public void setWriter(Writer writer);

    /**
     * Sets the Writer used to display error output.
     *
     * @param writer The Writer.
     */
    public void setErrorWriter(Writer writer);

    /**
     * Returns a Reader to be used by the script to read input.
     *
     * @return The Reader.
     */
    public Reader getReader();

    /**
     * Sets the Reader for scripts to read input.
     *
     * @param reader The new Reader.
     */
    public void setReader(Reader reader);
}
