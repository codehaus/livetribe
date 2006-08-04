/*
 * Copyright 2006 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.livetribe.ioc;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Methods annotated with this annotation are normally setter methods
 * that receive as argument an instance of a service needed by the class
 * declaring the annotated method for its functionalities.
 * <br />
 * The class {@link StandardContainer} contains the logic to inject service
 * instances into objects whose class contains methods with this annotation.
 * @see PostConstruct
 * @version $Rev$ $Date$
 */
@Documented
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Inject
{
    /**
     * Allows to specify the name of the service instance to inject.
     * <br />
     * By default the service name is not specified, and autowiring
     * by type is used to infer the correct service to inject.
     */
    public String serviceName() default "";
}
