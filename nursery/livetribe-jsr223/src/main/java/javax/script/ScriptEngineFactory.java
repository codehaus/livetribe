/**
 *
 * Copyright 2005 (C) The LogicBlaze, Inc. All Rights Reserved.
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
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package javax.script;

/**
 * ScriptEngineFactory is used to describe and instantiate
 * ScriptEngines.
 * <p/>
 * Each class implementing ScriptEngine has a corresponding factory
 * that exposes metadata describing the engine class.
 * <p/>
 * The ScriptEngineManager uses the service provider mechanism
 * described in the Jar File Specification to obtain instances of all
 * ScriptEngineFactories available in the current ClassLoader.
 *
 * @version $Revision: $ $Date: $
 */
public interface ScriptEngineFactory extends ScriptEngineInfo
{
    /**
     * Returns an instance of the ScriptEngine associated with this
     * ScriptEngineFactory. Properties of this ScriptEngine class
     * are described by the ScriptEngineInfo members of this
     * ScriptEngineFactory.
     *
     * @return A new ScriptEngine instance.
     */
    public ScriptEngine getScriptEngine();
}
