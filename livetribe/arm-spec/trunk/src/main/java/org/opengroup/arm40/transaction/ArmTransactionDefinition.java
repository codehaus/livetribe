/* ------------------------------------------------------------------------- */
/*                                                                           */
/* Copyright (c) 2003 The Open Group                                         */
/*                                                                           */
/* Permission is hereby granted, free of charge, to any person obtaining a   */
/* copy of this software (the "Software"), to deal in the Software without   */
/* restriction, including without limitation the rights to use, copy,        */
/* modify, merge, publish, distribute, sublicense, and/or sell copies of     */
/* the Software, and to permit persons to whom the Software is furnished     */
/* to do so, subject to the following conditions:                            */
/*                                                                           */
/* The above copyright notice and this permission notice shall be included   */
/* in all copies or substantial portions of the Software.                    */
/*                                                                           */
/* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS   */
/* OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF                */
/* MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.    */
/* IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY      */
/* CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT */
/* OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR  */
/* THE USE OR OTHER DEALINGS IN THE SOFTWARE.                                */
/*                                                                           */
/* ------------------------------------------------------------------------- */
/*                                                                           */
/* File revision information                                                 */
/*                                                                           */
/* $Source$ */
/* $Revision$ */
/* $Date$ */
/*                                                                           */
/* ------------------------------------------------------------------------- */
/*                                                                           */
/* ArmTransactionDefinition                                                  */
/*                                                                           */
/* ArmTransactionDefinition contains the metadata that is the same for all   */
/* instances of a transaction type (represented by ArmTransaction or         */
/* ArmTranReport).                                                           */
/*                                                                           */
/* ArmTransactionDefinition is created with the                              */
/* newArmTransactionDefinition() method of ArmTransactionFactory.            */
/*                                                                           */
/* ------------------------------------------------------------------------- */

package org.opengroup.arm40.transaction;

public interface ArmTransactionDefinition extends ArmInterface {

// No Public Constructors

// Public Instance Methods

    public ArmApplicationDefinition getApplicationDefinition();

    public ArmID getID();

    public ArmIdentityPropertiesTransaction getIdentityProperties();

    public String getName();
}
