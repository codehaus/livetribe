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
/* ArmTransaction                                                            */
/*                                                                           */
/* For most applications, ArmTransaction is the most important of all the    */
/* ARM classes, and the most frequently used. Instances of ArmTransaction    */
/* represent transactions when they execute. A "transaction" is any unit of  */
/* work that has a clearly understood beginning and ending point, and which  */
/* begins and ends in the same VM.                                           */
/*                                                                           */
/* ArmTransaction is created with the newArmTransaction() method of          */
/* ArmTransactionFactory.                                                    */
/*                                                                           */
/* ------------------------------------------------------------------------- */

package org.opengroup.arm40.transaction;

public interface ArmTransaction extends ArmInterface {

// No Public Constructors

// Public Instance Methods

    public int bindThread();

    public long blocked();

    public ArmApplication getApplication();

    public String getContextURIValue();

    public String getContextValue(int index);

    public ArmCorrelator getCorrelator();

    public ArmCorrelator getParentCorrelator();

    public int getStatus();

    public ArmTransactionDefinition getDefinition();

    public ArmUser getUser();

    public boolean isTraceRequested();

    public int reset();

    public int setArrivalTime();

    public int setContextURIValue(String value);

    public int setContextValue(int index, String value);

    public int setTraceRequested(boolean traceState);

    public int setUser(ArmUser user);

    public int start();

    public int start(byte[] parentCorr);

    public int start(byte[] parentCorr, int offset);

    public int start(ArmCorrelator parentCorr);

    public int stop(int status);

    public int stop(int status, String diagnosticDetail);

    public int unbindThread();

    public int unblocked(long blockHandle);

    public int update();
}
