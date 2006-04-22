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
 * The optional interface implemented by ScriptEngines whose methods allow the
 * invocation of procedures in scripts that have previously been executed.
 *
 * @version $Revision: $ $Date: $
 */
public interface Invocable
{
    /**
     * Calls a procedure compiled during a previous script execution, which is
     * retained in the state of the ScriptEngine.
     *
     * @param thiz If the procedure is a member of a class defined
     *             in the script and thiz is an instance of that class
     *             returned by a previous execution or invocation, the
     *             named method is called through that instance.
     *             <p/>
     *             If non-null, the argument must represent an instance of
     *             a scripting class.
     * @param name The name of the procedure to be called.
     * @param args An array of arguments to pass to the procedure.
     *             The rules for converting the arguments to scripting
     *             variables are implementation-specific.
     * @return The value returned by the procedure. The rules for
     *         converting the scripting variable returned by the
     *         procedure to a Java Object are implementation-specific.
     * @throws ScriptException          if an error occurs during script execution.
     * @throws NoSuchMethodException    If method with given name or
     *                                  matching argument types cannot be found.
     * @throws IllegalArgumentException If the thiz argument is non-null and
     *                                  does not represent an instance of a
     *                                  scripting class.
     * @throws NullPointerException     if method name is null.
     */
    public Object call(Object thiz, String name, Object[] args) throws ScriptException, NoSuchMethodException;

    /**
     * Same as <code>call(Object, String, Object[])</code> with null as the
     * first argument. Used to call top-level procedures defined in
     * scripts.
     *
     * @param name The name of the procedure to be called.
     * @param args An array of arguments to pass to the procedure.
     *             The rules for converting the arguments to scripting
     *             variables are implementation-specific.
     * @return The value returned by the procedure. The rules for
     *         converting the scripting variable returned by the
     *         procedure to a Java Object are implementation-specific.
     * @throws ScriptException       if an error occurs during script execution.
     * @throws NoSuchMethodException If method with given name or
     *                               matching argument types cannot be found.
     * @throws NullPointerException  if method name is null.
     */
    public Object call(String name, Object[] args) throws ScriptException, NoSuchMethodException;

    /**
     * Returns an implementation of an interface using top-level
     * procedures compiled in the interpreter. The methods of the
     * interface may be implemented using the <code>call(String, Object[])</code>
     * method.
     *
     * @param clazz The Class object of the interface to return.
     * @return An instance of requested interface - null if the
     *         requested interface is unavailable, i. e. if compiled
     *         methods in the ScriptEngine cannot be found matching the
     *         ones in the requested interface.
     * @throws IllegalArgumentException if the specified Class object does not
     *                                  exist or is not an interface.
     */
    public Object getInterface(Class clazz);

    /**
     * Returns an implementation of an interface using member
     * functions of a scripting object compiled in the interpreter.
     * The methods of the interface may be implemented using the
     * <code>call(Object, String, Object[])</code> method.
     *
     * @param thiz  The scripting object whose member functions are
     *              used to implement the methods of the interface.
     * @param clazz The Class object of the interface to return.
     * @return An instance of requested interface - null if the
     *         requested interface is unavailable, i. e. if compiled
     *         methods in the ScriptEngine cannot be found matching the
     *         ones in the requested interface.
     * @throws IllegalArgumentException if the specified Class object does not
     *                                  exist or is not an interface, or
     *                                  if the specified Object is null or does
     *                                  not represent a scripting object.
     */
    public Object getInterface(Object thiz, Class clazz);
}
