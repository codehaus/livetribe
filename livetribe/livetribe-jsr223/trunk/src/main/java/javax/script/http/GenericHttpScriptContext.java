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

import javax.script.GenericScriptContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Set;


/**
 * @version $Revision$ $Date$
 */
public class GenericHttpScriptContext extends GenericScriptContext implements HttpScriptContext
{
    public static final String[] defaultMethods = {"GET", "POST"};

    protected boolean disableScript;
    protected boolean displayResults;
    protected boolean useSession;
    protected String[] methods;
    protected String[] languages;
    protected String docRoot;
    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected Servlet servlet;

    public void initialize(Servlet servlet, HttpServletRequest request, HttpServletResponse response) throws ServletException
    {
        this.request = new InternalHttpServletRequestWrapper(request);
        this.response = response;
        this.servlet = servlet;

        ServletContext context = servlet.getServletConfig().getServletContext();

        String disableScript = context.getInitParameter("script-disable");
        if (disableScript != null && disableScript.equals("true"))
        {
            this.disableScript = true;
        }

        String displayResults = context.getInitParameter("script-display-results");
        if (displayResults != null && displayResults.equals("true"))
        {
            this.displayResults = true;
        }

        String useSession = context.getInitParameter("script-use-session");
        if (useSession != null && useSession.equals("false"))
        {
            this.useSession = false;
        }

        String methodNames = context.getInitParameter("script-methods");
        if (methodNames != null)
        {
            methods = methodNames.split(",");
        }
        else
        {
            methods = defaultMethods;
        }

        docRoot = context.getInitParameter("script-directory");

        String languageNames = context.getInitParameter("allowed-languages");
        if (languageNames != null)
        {
            languages = languageNames.split(",");
        }
    }

    public void release()
    {
        request = null;
        response = null;
        servlet = null;

        disableScript = false;
        displayResults = false;
        useSession = true;
        docRoot = null;
        languages = null;

        methods = defaultMethods;
    }

    public void forward(String relativePath) throws ServletException, IOException
    {
        String servletPath = request.getServletPath();
        String path = servletPath.substring(0, servletPath.lastIndexOf(47)) + "/" + relativePath;
        servlet.getServletConfig().getServletContext().getRequestDispatcher(path).forward(request, response);
    }

    public void include(String relativePath) throws ServletException, IOException
    {
        String servletPath = request.getServletPath();
        String path = servletPath.substring(0, servletPath.lastIndexOf(47)) + "/" + relativePath;
        servlet.getServletConfig().getServletContext().getRequestDispatcher(path).include(request, response);
    }

    public void setAttribute(String name, Object value, int scope)
    {
        if (name == null) throw new IllegalArgumentException("Name is null");

        switch (scope)
        {
            case REQUEST_SCOPE:
                request.setAttribute(name, value);
                break;
            case SESSION_SCOPE:
                if (!useSession) throw new IllegalArgumentException("Scripting session state disabled");

                HttpSession session = request.getSession();
                if (session == null) throw new IllegalArgumentException("Session state disabled");

                session.setAttribute(name, value);
                break;
            case APPLICATION_SCOPE:
                servlet.getServletConfig().getServletContext().setAttribute(name, value);
                break;
            default:
                super.setAttribute(name, value, scope);
        }
    }

    public Object getAttribute(String name, int scope)
    {
        if (name == null) throw new IllegalArgumentException("Name is null");

        switch (scope)
        {
            case REQUEST_SCOPE:
                if ("Request".equals(name)) return request;
                if ("Response".equals(name)) return response;
                if ("Servlet".equals(name)) return servlet;

                return request.getAttribute(name);
            case SESSION_SCOPE:
                if (!useSession) return null;

                HttpSession session = request.getSession();
                if (session == null) return null;

                return request.getSession().getAttribute(name);
            case APPLICATION_SCOPE:
                return servlet.getServletConfig().getServletContext().getAttribute(name);
            default:
                return super.getAttribute(name, scope);
        }
    }

    public Object getAttribute(String name)
    {
        Object result = getAttribute(name, REQUEST_SCOPE);
        if (result != null) return result;

        result = getAttribute(name, SESSION_SCOPE);
        if (result != null) return result;

        result = getAttribute(name, APPLICATION_SCOPE);
        if (result != null) return result;

        return super.getAttribute(name);
    }

    public Reader getScriptSource()
    {
        String requestURI = request.getRequestURI();
        String file = requestURI.substring(request.getContextPath().length());
        try
        {
            if (docRoot != null)
            {
                return (new InputStreamReader(new FileInputStream(docRoot + File.separator + file)));
            }
            else
            {
                return (new BufferedReader(new InputStreamReader(servlet.getServletConfig().getServletContext().getResourceAsStream(file))));
            }
        }
        catch (IOException exception)
        {
            return null;
        }
    }

    public HttpServletRequest getRequest()
    {
        return request;
    }

    public HttpServletResponse getResponse()
    {
        return response;
    }

    public Servlet getServlet()
    {
        return new InternalServletWrapper(servlet);
    }

    public boolean disableScript()
    {
        return disableScript;
    }

    public boolean useSession()
    {
        return useSession;
    }

    public boolean displayResults()
    {
        return displayResults;
    }

    public String[] getMethods()
    {
        return methods;
    }

    public String[] getAllowedLanguages()
    {
        return languages;
    }

    class InternalHttpServletRequestWrapper extends HttpServletRequestWrapper
    {
        InternalHttpServletRequestWrapper(HttpServletRequest httpServletRequest)
        {
            super(httpServletRequest);
        }

        public HttpSession getSession()
        {
            if (disableScript) return null;
            if (!useSession) return null;

            return super.getSession();
        }

        public RequestDispatcher getRequestDispatcher(String string)
        {
            return null;
        }
    }

    class InternalServletWrapper implements Servlet
    {
        private final Servlet base;

        public InternalServletWrapper(Servlet base)
        {
            this.base = base;
        }

        public void init(ServletConfig servletConfig)
                throws ServletException
        {
            base.init(servletConfig);
        }

        public ServletConfig getServletConfig()
        {
            return new InternalServletConfigWrapper(base.getServletConfig());
        }

        public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException
        {
            base.service(servletRequest, servletResponse);
        }

        public String getServletInfo()
        {
            return base.getServletInfo();
        }

        public void destroy()
        {
            base.destroy();
        }
    }

    class InternalServletConfigWrapper implements ServletConfig
    {
        private final ServletConfig base;

        public InternalServletConfigWrapper(ServletConfig base)
        {
            this.base = base;
        }

        public String getServletName()
        {
            return base.getServletName();
        }

        public ServletContext getServletContext()
        {
            return new InternalServletContext(base.getServletContext());
        }

        public String getInitParameter(String s)
        {
            return base.getInitParameter(s);
        }

        public Enumeration getInitParameterNames()
        {
            return base.getInitParameterNames();
        }
    }

    class InternalServletContext implements ServletContext
    {
        private final ServletContext base;

        public InternalServletContext(ServletContext base)
        {
            this.base = base;
        }

        public ServletContext getContext(String s)
        {
            return base.getContext(s);
        }

        public int getMajorVersion()
        {
            return base.getMajorVersion();
        }

        public int getMinorVersion()
        {
            return base.getMinorVersion();
        }

        public String getMimeType(String s)
        {
            return base.getMimeType(s);
        }

        public Set getResourcePaths(String s)
        {
            return base.getResourcePaths(s);
        }

        public URL getResource(String s) throws MalformedURLException
        {
            return base.getResource(s);
        }

        public InputStream getResourceAsStream(String s)
        {
            return base.getResourceAsStream(s);
        }

        public RequestDispatcher getRequestDispatcher(String s)
        {
            return null;
        }

        public RequestDispatcher getNamedDispatcher(String s)
        {
            return null;
        }

        public Servlet getServlet(String s) throws ServletException
        {
            return base.getServlet(s);
        }

        public Enumeration getServlets()
        {
            return base.getServlets();
        }

        public Enumeration getServletNames()
        {
            return base.getServletNames();
        }

        public void log(String s)
        {
            base.log(s);
        }

        public void log(Exception e, String s)
        {
            base.log(e, s);
        }

        public void log(String s, Throwable throwable)
        {
            base.log(s, throwable);
        }

        public String getRealPath(String s)
        {
            return base.getRealPath(s);
        }

        public String getServerInfo()
        {
            return base.getServerInfo();
        }

        public String getInitParameter(String s)
        {
            return base.getInitParameter(s);
        }

        public Enumeration getInitParameterNames()
        {
            return base.getInitParameterNames();
        }

        public Object getAttribute(String s)
        {
            return base.getAttribute(s);
        }

        public Enumeration getAttributeNames()
        {
            return base.getAttributeNames();
        }

        public void setAttribute(String s, Object o)
        {
            base.setAttribute(s, o);
        }

        public void removeAttribute(String s)
        {
            base.removeAttribute(s);
        }

        public String getServletContextName()
        {
            return base.getServletContextName();
        }
    }
}
