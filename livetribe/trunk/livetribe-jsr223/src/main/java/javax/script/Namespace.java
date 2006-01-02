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

import java.util.Map;

/**
 * A mapping of key/value pairs, all of whose keys are Strings.
 *
 * @version $Revision: $ $Date: $
 */
public interface Namespace extends Map
{
    /**
     * Set a named value.
     *
     * @param name  The name to associate with the value.
     * @param value The value associated with the name.
     * @return The value previously associated with the given name.
     *         Returns null if no value was previously associated with
     *         the name.
     * @throws ClassCastException   if name is not a string.
     * @throws NullPointerException if name is null.
     */
    public Object put(Object name, Object value);

    /**
     * Adds all the mappings in a given Map to this Namespace.
     *
     * @param toMerge The Map to merge with this one.
     * @throws ClassCastException   if some key in the map is not a string.
     * @throws NullPointerException if some key in the map is null.
     */
    public void putAll(Map toMerge);
}
