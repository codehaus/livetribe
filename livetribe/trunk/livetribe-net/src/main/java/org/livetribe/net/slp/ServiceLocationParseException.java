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
package org.livetribe.net.slp;


/**
 * This exception occurs only when service location itself fails to parse.
 * This should never occur - it indicates a failure in the protocol handling
 * software to correctly deal with outgoing or incoming data, though it may be
 * a different SLP implementation which is supplying the unparsable protocol
 * stream.
 *
 * @version $Revision: 1.1 $ $Date: 2005/01/26 04:05:03 $
 */
public class ServiceLocationParseException extends ServiceLocationException
{
    public ServiceLocationParseException()
    {
        super( PARSE_ERROR );
    }
}
