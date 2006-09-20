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
package org.livetribe.arm.connection.model;

import java.util.Set;
import java.util.List;


/**
 * @version $Revision$ $Date$
 */
public class TransactionNode extends Model
{
    private TransactionNode parent;
    private Set children;
    private Correlator correlator;
    private String user;
    private String[] contextValues;
    private String contextURI;
    private int status;
    private String message;
    private long start;
    private long end;
    private boolean reset;
    private List blocks;
    private List updates;

}
