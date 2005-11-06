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
package org.livetribe.snmp.mib.ast;

import antlr.Token;


/**
 * @version $Revision: 1.1 $ $Date: 2005/01/26 04:05:03 $
 */
public class IntegerPrimitive extends Primitive
{
    private transient Integer value;
    private boolean negative;

    public IntegerPrimitive()
    {
        super();
    }

    public IntegerPrimitive( Token token )
    {
        super( token );
    }

    public boolean isNegative()
    {
        return negative;
    }

    public void setNegative( boolean negative )
    {
        this.negative = negative;
    }

    public Object toPrimitive() throws ParseException
    {
        if ( value == null )
        {
            try
            {
                value = new Integer( ( negative ? "-" : "" ) + getToken().getText() );
            }
            catch ( NumberFormatException nfe )
            {
                throw new ParseException( nfe );
            }
        }

        return value;
    }

    public String toString()
    {
        try
        {
            return ( (Integer) toPrimitive() ).toString();
        }
        catch ( ParseException e )
        {
            return "[PARSE ERROR]";
        }
    }
}

