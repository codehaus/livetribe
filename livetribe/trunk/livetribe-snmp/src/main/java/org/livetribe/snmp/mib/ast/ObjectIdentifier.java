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

import java.util.ArrayList;

import antlr.Token;


/**
 * @version $Revision: 1.1 $ $Date: 2005/01/26 04:05:03 $
 */
public class ObjectIdentifier
{
    private ArrayList list = new ArrayList();

    public void addNameForm( Token name )
    {
        list.add( name );
    }

    public void addNumberForm( IntegerPrimitive number )
    {
        list.add( number );
    }

    public void addNameAndNumberForm( Token name, IntegerPrimitive number )
    {
        list.add( new NameAndNumber( name, number ) );
    }

    private class NameAndNumber
    {
        private Token name;
        private IntegerPrimitive number;

        NameAndNumber( Token name, IntegerPrimitive number )
        {
            this.name = name;
            this.number = number;
        }

        public String toString()
        {
            return name.getText() + "(" + number + ")";
        }

        public boolean equals( Object object )
        {
            if ( object instanceof NameAndNumber )
            {
                NameAndNumber other = (NameAndNumber) object;
                return name.getText().equals( other.name.getText() ) && number.equals( other.number );
            }
            else
                return false;
        }
    }
}