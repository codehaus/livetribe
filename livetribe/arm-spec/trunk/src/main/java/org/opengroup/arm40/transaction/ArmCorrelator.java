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
/* ArmCorrelator                                                             */
/*                                                                           */
/* ArmCorrelator represents a correlation token passed from a calling        */
/* transaction to a called transaction. The correlation token may be used to */
/* establish a calling hierarchy across processes and systems.               */
/*                                                                           */
/* ArmCorrelator is created using one of these methods:                      */
/*  - The newArmCorrelator() method of ArmTransactionFactory                 */
/*  - The getCorrelator() method of ArmTransaction                           */
/*  - The generateCorrelator() method of ArmTranReport                       */
/*                                                                           */
/* ------------------------------------------------------------------------- */

package org.opengroup.arm40.transaction;

public interface ArmCorrelator extends ArmToken {
// No Public Constructors

// Public Instance Methods (in addition to those defined in ArmToken)
// (Implementations should also override equals() and hashCode() 
// from java.lang.Object.)

    public boolean isAgentTrace();
        
    public boolean isApplicationTrace();
}
