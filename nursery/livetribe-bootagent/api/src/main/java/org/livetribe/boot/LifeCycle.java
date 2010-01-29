/**
 *
 * Copyright 2007-2010 (C) The original author or authors
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
package org.livetribe.boot;

/**
 * A simple lifecycle interface that must be implemented by boot classes.
 * Instances of this interface should have light constructors since they will
 * be, at times, instantiated and thrown away as part of a consistency check.
 * <p/>
 * This interface contains no dependencies and belongs in its own jar so that
 * boot classes have no additional dependencies.
 *
 * @version $Revision$ $Date$
 */
public interface LifeCycle
{
    /**
     * Start the boot class instance.
     */
    void start();

    /**
     * Stop the boot class instance.
     */
    void stop();
}
