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
/* ArmMetricDefinition                                                       */
/*                                                                           */
/* ArmMetricDefinition is a superclass for all the metric definition         */
/* interfaces. All the methods are defined in ArmMetricDefinition. The       */
/* subclasses serve as markers for the data types.                           */
/*                                                                           */
/* ArmMetricDefinition cannot be instantiated.                               */
/* Objects that implement a subclass of ArmMetricDefinition are created      */
/* using the newArmMetric...Definition() methods of ArmMetricFactory.        */
/*                                                                           */
/* ------------------------------------------------------------------------- */

package org.opengroup.arm40.metric;

import org.opengroup.arm40.transaction.ArmInterface;
import org.opengroup.arm40.transaction.ArmID;

public interface ArmMetricDefinition extends ArmInterface {

// No Public Constructors

// Constants

    public static final short METRIC_USE_GENERAL = 0;
        // No specific usage semantics are declared

    public static final short METRIC_USE_TRAN_SIZE = 1;
        // Metric represents the "size" of the transaction
        // (counter & gauge only)

    public static final short METRIC_USE_TRAN_STATUS = 2;
        // Metric represents status, like an error code
        // (numeric ID & string only)

// Public Instance Methods 

    public String getName();

    public String getUnits();

    public short getUsage();

    public ArmID getID();
}
