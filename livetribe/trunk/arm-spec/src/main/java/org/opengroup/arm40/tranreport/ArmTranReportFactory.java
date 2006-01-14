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
/* ArmTranReportFactory                                                      */
/*                                                                           */
/* ArmTranReportFactory provides methods to create instances of the classes  */
/* in the org.opengroup.arm40.tranreport package. An error callback method   */
/* can be registered for objects created with this factory.                  */
/*                                                                           */
/* ArmTranReportFactory is instantiated using a class loader. The actual     */
/* name of the factory implementation class is obtained from the system      */
/* property whose name is provided in the propertyKey constant of            */
/* ArmTranReportFactory.                                                     */
/*                                                                           */
/* ------------------------------------------------------------------------- */

package org.opengroup.arm40.tranreport;

import org.opengroup.arm40.transaction.ArmApplication;
import org.opengroup.arm40.transaction.ArmApplicationDefinition;
import org.opengroup.arm40.transaction.ArmID;
import org.opengroup.arm40.transaction.ArmInterface;
import org.opengroup.arm40.transaction.ArmTransactionDefinition;
import org.opengroup.arm40.transaction.ArmErrorCallback;

public interface ArmTranReportFactory extends ArmInterface {

// Public Constants

    public static final String propertyKey = "Arm40.ArmTranReportFactory";


// Public Instance Methods

    public ArmApplicationRemote newArmApplicationRemote(
        ArmApplicationDefinition definition, String group, String instance,
        String[] contextValues, ArmSystemAddress systemAddress);

    public ArmSystemAddress newArmSystemAddress(short format, 
        byte[] addressBytes, ArmID id);

    public ArmSystemAddress newArmSystemAddress(short format, 
        byte[] addressBytes, int offset, ArmID id);

    public ArmSystemAddress newArmSystemAddress(short format, 
        byte[] addressBytes, int offset, int length, ArmID id);

    public ArmTranReport newArmTranReport(ArmApplication app, 
        ArmTransactionDefinition definition);

    public boolean setErrorCallback(ArmErrorCallback errorCallback);
}
