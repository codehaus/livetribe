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
/* ArmSystemAddress                                                          */
/*                                                                           */
/* ArmSystemAddress encapsulates the network addressing information for a    */
/* system. It may be used with ArmTranReport if the reported transaction     */
/* executed on a different system.                                           */
/*                                                                           */
/* ArmSystemAddress is created with one of the newArmSystemAddress() methods */
/* of ArmTranReportFactory.                                                  */
/*                                                                           */
/* ------------------------------------------------------------------------- */

package org.opengroup.arm40.tranreport;

import org.opengroup.arm40.transaction.ArmToken;
import org.opengroup.arm40.transaction.ArmID;

public interface ArmSystemAddress extends ArmToken {

// public constants

    public static final short FORMAT_HOSTNAME = 7;

    public static final short FORMAT_IPV4 = 1;

    public static final short FORMAT_IPV4PORT = 2;

    public static final short FORMAT_IPV6 = 3;

    public static final short FORMAT_IPV6PORT = 4;

    public static final short FORMAT_SNA = 5;

    public static final short FORMAT_X25 = 6;

    public static final short FORMAT_UUID = 8;

// No Public Constructors

// Public Instance Methods (in addition to those defined by ArmToken)
// (Implementations should also override equals() and hashCode() 
// from java.lang.Object.)

    public byte[] getAddress();

    public short getFormat();

    public ArmID getID();
}

