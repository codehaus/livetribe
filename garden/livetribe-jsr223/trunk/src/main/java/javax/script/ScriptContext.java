/**
 *
 * Copyright 2006 - 2007 (C) The original author or authors
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
import java.util.List;


/**
 * @version $Revision: $ $Date: $
 */
public interface ScriptContext
{
    public final static int ENGINE_SCOPE = 0;
    public final static int GLOBAL_SCOPE = 1;


    public Object getAttribute(String name);

    public Object getAttribute(String name, int scope);

    public int getAttributeScope(String name);

    public Bindings getBindings(int scope);

    public Writer getErrorWriter();

    public Reader getReader();

    public List<Integer> getScopes();

    public Writer getWriter();

    public Object removeAttribute(String name, int scope);

    public void setAttribute(String name, Object value, int scope);

    public void setBindings(Bindings bindings, int scope);

    public void setErrorWriter(Writer writer);

    public void setReader(Reader reader);

    public void setWriter(Writer writer);
}
