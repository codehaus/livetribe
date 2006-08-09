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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Stack;


/**
 * @version $Revision$ $Date$
 */
public class ScriptEngineManager
{
    private final Set engineSpis = new HashSet();
    private final Map byName = new HashMap();
    private final Map registeredByName = new HashMap();
    private final Map byExtension = new HashMap();
    private final Map registeredByExtension = new HashMap();
    private final Map byMimeType = new HashMap();
    private final Map registeredByMimeType = new HashMap();
    private Namespace globalScope;

    public ScriptEngineManager()
    {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        try
        {
            Enumeration factoryResources = classLoader.getResources("META-INF/services/javax.script.ScriptEngineFactory");
            while (factoryResources.hasMoreElements())
            {
                URL url = (URL) factoryResources.nextElement();
                Iterator classNames = getClassNames(url);
                while (classNames.hasNext())
                {
                    String className = (String) classNames.next();
                    try
                    {
                        Class factoryClass = classLoader.loadClass(className);
                        Object object = factoryClass.newInstance();

                        if (object instanceof ScriptEngineFactory)
                        {
                            ScriptEngineFactory factory = (ScriptEngineFactory) object;

                            byName.put(factory.getEngineName(), factory);

                            String[] extensions = factory.getExtensions();
                            for (int i = 0; i < extensions.length; i++) byExtension.put(extensions[i], factory);

                            String[] mimeTypes = factory.getMimeTypes();
                            for (int i = 0; i < mimeTypes.length; i++) byMimeType.put(mimeTypes[i], factory);

                            engineSpis.add(factory);
                        }
                    }
                    catch (ClassNotFoundException doNothing)
                    {
                    }
                    catch (IllegalAccessException doNothing)
                    {
                    }
                    catch (InstantiationException doNothing)
                    {
                    }
                }

            }
        }
        catch (IOException doNothing)
        {
        }
    }

    public Namespace getGlobalScope()
    {
        return globalScope;
    }

    public void setGlobalScope(Namespace globalScope)
    {
        this.globalScope = globalScope;
    }

    public void put(Object key, Object value)
    {
        if (globalScope != null) globalScope.put(key, value);
    }

    public Object get(Object key)
    {
        if (globalScope != null)
        {
            return globalScope.get(key);
        }
        else return null;
    }

    public ScriptEngine getEngineByName(String shortName)
    {
        ScriptEngineFactory factory = (ScriptEngineFactory) registeredByName.get(shortName);

        if (factory == null) factory = (ScriptEngineFactory) byName.get(shortName);
        if (factory == null) return null;

        ScriptEngine engine = factory.getScriptEngine();

        engine.setNamespace(globalScope, ScriptContext.GLOBAL_SCOPE);

        return engine;
    }

    public ScriptEngine getEngineByExtension(String extension)
    {
        ScriptEngineFactory factory = (ScriptEngineFactory) registeredByExtension.get(extension);

        if (factory == null) factory = (ScriptEngineFactory) byExtension.get(extension);
        if (factory == null) return null;

        ScriptEngine engine = factory.getScriptEngine();

        engine.setNamespace(globalScope, ScriptContext.GLOBAL_SCOPE);

        return engine;
    }

    public ScriptEngine getEngineByMimeType(String mimeType)
    {
        ScriptEngineFactory factory = (ScriptEngineFactory) registeredByMimeType.get(mimeType);

        if (factory == null) factory = (ScriptEngineFactory) byMimeType.get(mimeType);
        if (factory == null) return null;

        ScriptEngine engine = factory.getScriptEngine();

        engine.setNamespace(globalScope, ScriptContext.GLOBAL_SCOPE);

        return engine;
    }

    public ScriptEngineFactory[] getEngineFactories()
    {
        return (ScriptEngineFactory[]) engineSpis.toArray(new ScriptEngineFactory[engineSpis.size()]);
    }

    public void registerEngineName(String name, ScriptEngineFactory factory)
    {
        registeredByName.put(name, factory);
    }

    public void registerEngineMimeType(String type, ScriptEngineFactory factory)
    {
        registeredByMimeType.put(type, factory);
    }

    public void registerEngineExtension(String extension, ScriptEngineFactory factory)
    {
        registeredByExtension.put(extension, factory);
    }

    private Iterator getClassNames(URL url)
    {
        Stack stack = new Stack();

        try
        {
            BufferedReader r = new BufferedReader(new InputStreamReader(url.openStream()));

            String line;
            while ((line = r.readLine()) != null)
            {
                int comment = line.indexOf('#');
                if (comment != -1) line = line.substring(0, comment);

                stack.push(line.trim());
            }
        }
        catch (IOException doNothing)
        {
        }

        return stack.iterator();
    }
}
