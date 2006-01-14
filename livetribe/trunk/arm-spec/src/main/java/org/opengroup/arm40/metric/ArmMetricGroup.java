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
/* ArmMetricGroup                                                            */
/*                                                                           */
/* ArmMetricGroup is used to bind objects that implement a subclass of       */
/* ArmMetric to an ArmTransactionWithMetrics or ArmTranReportWithMetrics     */
/* object.                                                                   */
/*                                                                           */
/* Instances of ArmMetricGroup are created using the newArmMetricGroup()     */
/* method of ArmMetricFactory.                                               */
/*                                                                           */
/* ------------------------------------------------------------------------- */

package org.opengroup.arm40.metric;

import org.opengroup.arm40.transaction.ArmInterface;

public interface ArmMetricGroup extends ArmInterface {

// No Public Constructors

// Public Instance Methods

    public ArmMetricGroupDefinition getDefinition();

    public ArmMetric getMetric(int index);

    public boolean isMetricValid(int index);

    public int setMetricValid(int index, boolean value);
}
