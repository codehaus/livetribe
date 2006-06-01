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
 * The optional interface implemented by ScriptEngines able to compile scripts
 * to a form that can be executed repeatedly without recompilation.
 *
 * @version $Revision: $ $Date: $
 */
public interface Compilable
{
    /**
     * Compiles the script (source read from Reader) for later execution.
     * Functionality is identical to compile(String) other than the way in
     * which the source is passed.
     *
     * @param script The reader from which the script source is obtained.
     * @return A subclass of CompiledScript to be executed later using one of
     *         the eval methods of CompiledScript.
     * @throws ScriptException      if compilation fails.
     * @throws NullPointerException if script is null.
     */
    public CompiledScript compile(Reader script) throws ScriptException;

    /**
     * Compiles the script (source represented as a String) for later
     * execution.
     *
     * @param script The source of the script, represented as a String.
     * @return A subclass of CompiledScript to be executed later using one of
     *         the eval methods of CompiledScript.
     * @throws ScriptException      if compilation fails.
     * @throws NullPointerException if script is null.
     */
    public CompiledScript compile(String script) throws ScriptException;
}
