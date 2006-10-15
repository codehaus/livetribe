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
package org.livetribe.arm40.connection.hibernate;

/**
 * @version $Revision$ $Date$
 */
public class AtmCustomer
{
    public int no;
    public String name;
    public int pin;
    public double balance;
    public String currency;

    public AtmCustomer(int no, String name, int pin, double balance, String currency)
    {
        this.no = no;
        this.name = name;
        this.pin = pin;
        this.balance = balance;
        this.currency = currency;
    }

}
