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
 * @version $Revision: $ $Date: $
 */
public interface ScriptContext
{
    public final static int ENGINE_SCOPE = 0;
    public final static int GLOBAL_SCOPE = 1;

    public void setNamespace(Namespace namespace, int scope);

    public Namespace getNamespace(int scope);

    public void setAttribute(String name, Object value, int scope);

    public Object getAttribute(String name, int scope);

    public Object removeAttribute(String name, int scope);

    public Object getAttribute(String name);

    public int getAttributeScope(String name);

    public Writer getWriter();

    public Writer getErrorWriter();

    public void setWriter(Writer writer);

    public void setErrorWriter(Writer writer);

    public Reader getReader();

    public void setReader(Reader reader);
}
