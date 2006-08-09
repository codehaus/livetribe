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

import javax.script.ScriptContext;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Reader;


/**
 * @version $Revision$ $Date$
 */
public interface HttpScriptContext extends ScriptContext
{
    public static final int REQUEST_SCOPE = 10;
    public static final int SESSION_SCOPE = 11;
    public static final int APPLICATION_SCOPE = 12;

    public void initialize(Servlet servlet, HttpServletRequest request, HttpServletResponse response) throws ServletException;

    public void release();

    public Reader getScriptSource();

    public HttpServletRequest getRequest();

    public HttpServletResponse getResponse();

    public Servlet getServlet();

    public void forward(String relativePath) throws ServletException, IOException;

    public void include(String relativePath) throws ServletException, IOException;

    public boolean disableScript();

    public boolean useSession();

    public boolean displayResults();

    public String[] getMethods();

    public String[] getAllowedLanguages();
}
