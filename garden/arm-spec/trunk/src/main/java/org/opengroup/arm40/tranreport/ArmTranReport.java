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
/* ArmTranReport                                                             */
/*                                                                           */
/* ArmTranReport is similar to ArmTransaction with two exceptions:           */
/* - With ArmTranReport, the application measures the response time, and     */
/*   reports it with a single report() event.                                */
/* - The transaction need not execute on the local system in the same VM     */
/*                                                                           */
/* Instances of ArmTranReport are created using the newArmTranReport()       */
/* method of ArmTranReportFactory.                                           */
/*                                                                           */
/* ------------------------------------------------------------------------- */

package org.opengroup.arm40.tranreport;

import org.opengroup.arm40.transaction.ArmInterface;
import org.opengroup.arm40.transaction.ArmCorrelator;
import org.opengroup.arm40.transaction.ArmApplication;
import org.opengroup.arm40.transaction.ArmTransactionDefinition;
import org.opengroup.arm40.transaction.ArmUser;

public interface ArmTranReport extends ArmInterface {

// No Public Constructors

// Public Instance Methods

    public ArmCorrelator generateCorrelator();

    public ArmApplication getApplication();

    public String getContextURIValue();

    public String getContextValue(int index);

    public ArmCorrelator getCorrelator();

    public ArmCorrelator getParentCorrelator();

    public long getResponseTime();

    public int getStatus();

    public ArmTransactionDefinition getDefinition();

    public ArmUser getUser();

    public int report(int status, long respTime);

    public int report(int status, long respTime, long stopTime);

    public int report(int status, long respTime, String diagnosticDetail);

    public int report(int status, long respTime, long stopTime, String diagnosticDetail);

    public int setContextURIValue(String value);

    public int setContextValue(int index, String value);

    public int setParentCorrelator(ArmCorrelator parent);

    public int setUser(ArmUser user);
}
