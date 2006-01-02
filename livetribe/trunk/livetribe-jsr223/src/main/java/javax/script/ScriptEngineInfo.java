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
 * ScriptEngineInfo contains methods which describe a ScriptEngine
 * and its capabilities. Every ScriptEngine is required to supply a
 * corresponding ScriptEngineInfo class.
 * <p/>
 * ScriptEngineFactory extends ScriptEngineInfo. Its
 * ScriptEngineInfo methods are be used to determine attributes of the
 * ScriptEngines enumerated the Script Engine Discovery mechanism of
 * the ScriptEngineManager.
 * <p/>
 * The ScriptEngine interface has a getFactory method that returns a
 * ScriptEngineFactory for that engine. Since ScriptEngineFactory
 * implements ScriptEngineInfo, the metadata for the ScriptEngine
 * can be obtained from the return value.
 *
 * @version $Revision: $ $Date: $
 */
public interface ScriptEngineInfo
{
    /**
     * Returns the full name of the ScriptEngine. For instance an
     * implementation based on the Mozilla Rhino Javascript engine
     * might return <i>Rhino Mozilla Javascript Engine</i>.
     *
     * @return The name of the engine implementation.
     */
    public String getEngineName();

    /**
     * Returns the version of the ScriptEngine.
     *
     * @return The ScriptEngine implementation version.
     */
    public String getEngineVersion();

    /**
     * Returns an array of filename extensions, which generally
     * identify scripts written in the language supported by this
     * ScriptEngine. The array is used by the ScriptEngineManager to
     * implement its getEngineByExtension method.
     *
     * @return The array of extensions.
     */
    public String[] getExtensions();

    /**
     * Returns an array of mimetypes, associated with scripts that
     * can be executed by the engine. The array is used by the
     * ScriptEngineManager class to implement its
     * getEngineByMimetype method.
     *
     * @return The array of mime types.
     */
    public String[] getMimeTypes();

    /**
     * Returns an array of short names for the ScriptEngine, which
     * may be used to identify the ScriptEngine by the
     * ScriptEngineManager. For instance, an implementation based on
     * the Mozilla Rhino Javascript engine might return
     * {"javascript", "rhino"}.
     *
     * @return The array of short names for the ScriptEngine.
     */
    public String[] getNames();

    /**
     * Returns the name of the scripting langauge supported by this
     * ScriptEngine.
     *
     * @return Returns the name of the scripting langauge supported by this
     *         ScriptEngine.
     */
    public String getLanguageName();

    /**
     * Returns the version of the scripting language supported by
     * this ScriptEngine.
     *
     * @return The version of the supported language.
     */
    public String getLanguageVersion();

    /**
     * Returns the value of an attribute whose meaning may be
     * implementation-specific. Keys for which the value is defined
     * in all implementations are:
     * <ul>
     * <li>ScriptEngine.ENGINE</li>
     * <li>ScriptEngine.ENGINE_VERSION</li>
     * <li>ScriptEngine.NAME</li>
     * <li>ScriptEngine.LANGUAGE</li>
     * <li>ScriptEngine.LANGUAGE_VERSION</li>
     * </ul>
     * The values for these keys are the Strings returned by
     * getEngineName, getEngineVersion, getName, getLanguageName and
     * getLanguageVersion respectively.
     * <p/>
     * A reserved key, THREADING, whose value describes the behavior
     * of the engine with respect to concurrent execution of scripts
     * and maintenance of state is also defined. These values for
     * the THREADING key are:
     * <p/>
     * <blockquote>null - The engine implementation is not thread safe,
     * and cannot be used to execute scripts concurrently on
     * multiple threads.</blockquote>
     * <blockquote>"MULTITHREADED" - The engine implementation is
     * internally thread-safe and scripts may execute
     * concurrently although effects of script execution on
     * one thread may be visible to scripts on other threads.</blockquote>
     * <blockquote>"THREAD-ISOLATED" - The implementation satisfies the
     * requirements of "MULTITHREADED", and also, the engine
     * maintains independent values for symbols in scripts
     * executing on different threads.</blockquote>
     * <blockquote>"STATELESS" - The implementation satisfies the
     * requirements of "THREAD-ISOLATED". In addition, script
     * executions do not alter the mappings in the Namespace
     * which is the engine scope of the ScriptEngine. In
     * particular, the keys in the Namespace and their
     * associated values are the same before and after the
     * execution of the script.</blockquote>
     * <p/>
     * Implementations may define implementation-specific keys.
     *
     * @param key
     * @return
     */
    public Object getParameter(java.lang.String key);
}
