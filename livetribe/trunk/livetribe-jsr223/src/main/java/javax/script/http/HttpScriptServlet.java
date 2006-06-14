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
package javax.script.http;

import javax.script.Namespace;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptException;
import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Reader;


/**
 * @version $Revision$ $Date$
 */
public abstract class HttpScriptServlet extends GenericServlet
{
    public abstract HttpScriptContext getContext(HttpServletRequest request, HttpServletResponse response) throws ServletException;

    public abstract ScriptEngine getEngine(HttpServletRequest request);

    public abstract void releaseEngine(ScriptEngine engine);

    public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException
    {
        if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse))
            throw new IllegalArgumentException();

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpScriptContext scriptContext = getContext(httpRequest, httpResponse);

        if (scriptContext.disableScript())
        {
            httpResponse.setStatus(403);
            return;
        }

        String[] methods = scriptContext.getMethods();
        String method = httpRequest.getMethod();
        boolean found = false;
        for (int i = 0; i < methods.length; i++)
        {
            if (method.compareToIgnoreCase(methods[i]) == 0)
            {
                found = true;
                break;
            }
        }

        if (!found)
        {
            httpResponse.setStatus(405);
            return;
        }

        Reader reader = scriptContext.getScriptSource();
        if (reader == null)
        {
            httpResponse.setStatus(404);
            return;
        }

        ScriptEngine engine = getEngine(httpRequest);

        if ("THREAD-ISOLATED".equals(engine.getFactory().getParameter("THREADING")))
        {
            synchronized (engine)
            {
                service(engine, httpRequest, httpResponse);
            }
        }
        else
        {
            service(engine, httpRequest, httpResponse);
        }
    }

    protected void service(ScriptEngine engine, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws ServletException, IOException
    {
        HttpScriptContext scriptContext = getContext(httpRequest, httpResponse);
        ScriptEngineFactory factory = engine.getFactory();

        String[] languages = scriptContext.getAllowedLanguages();
        if (languages != null)
        {
            boolean found = false;
            String[] names = factory.getNames();

            for (int i = 0; i < languages.length; i++)
            {
                String language = languages[i];

                for (int j = 0; j < names.length; j++)
                {
                    if (language.equals(names[j]))
                    {
                        found = true;
                        break;
                    }
                }
            }

            if (!found)
            {
                httpResponse.setStatus(403);
                return;
            }
        }

        Reader reader = scriptContext.getScriptSource();
        try
        {
            Namespace engineScope = scriptContext.getNamespace(ScriptContext.ENGINE_SCOPE);

            engineScope.put("request", scriptContext.getRequest());
            engineScope.put("response", scriptContext.getResponse());
            engineScope.put("servlet", this);

            engineScope.put("javax.script.filename", httpRequest.getRequestURI());
            engineScope.put("javax.script.engine", factory.getEngineName());
            engineScope.put("javax.script.engine_version", factory.getEngineVersion());
            engineScope.put("javax.script.language", factory.getLanguageName());
            engineScope.put("javax.script.language_version", factory.getLanguageVersion());
            engineScope.put("context", scriptContext);

            Object result = engine.eval(scriptContext.getScriptSource(), scriptContext);

            if (result != null && scriptContext.displayResults()) httpResponse.getWriter().write(result.toString());

            httpResponse.getWriter().flush();
        }
        catch (ScriptException scriptexception)
        {
            httpResponse.setStatus(500);
            throw new ServletException(scriptexception);
        }
        finally
        {
            releaseEngine(engine);
            scriptContext.release();
            reader.close();
        }
    }
}
