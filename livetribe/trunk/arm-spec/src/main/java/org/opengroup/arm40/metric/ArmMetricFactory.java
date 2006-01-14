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
/* ArmMetricFactory                                                          */
/*                                                                           */
/* ArmMetricFactory provides methods to create instances of the classes in   */
/* the org.opengroup.arm40.metric package. An error callback method can be   */
/* registered for objects created with this factory.                         */
/*                                                                           */
/* ArmMetricFactory is instantiated using a class loader. The actual name    */
/* of the factory implementation class is obtained from the system property  */
/* whose name is provided in the propertyKey constant of ArmMetricFactory.   */
/*                                                                           */
/* ------------------------------------------------------------------------- */

package org.opengroup.arm40.metric;

import org.opengroup.arm40.transaction.ArmInterface;
import org.opengroup.arm40.transaction.ArmApplicationDefinition;
import org.opengroup.arm40.transaction.ArmApplication;
import org.opengroup.arm40.transaction.ArmID;
import org.opengroup.arm40.transaction.ArmIdentityPropertiesTransaction;
import org.opengroup.arm40.transaction.ArmErrorCallback;

public interface ArmMetricFactory extends ArmInterface {

// Public Constants

    public static final String propertyKey = "Arm40.ArmMetricFactory";
        // name of system property holding the implementation class name

// Public Instance Methods

/* --- metric definitions --- */

    public ArmMetricCounter32Definition newArmMetricCounter32Definition(
        ArmApplicationDefinition app, String name, String units, 
        short usage, ArmID id);

    public ArmMetricCounter64Definition newArmMetricCounter64Definition(
        ArmApplicationDefinition app, String name, String units, 
        short usage, ArmID id);

    public ArmMetricCounterFloat32Definition 
        newArmMetricCounterFloat32Definition(
            ArmApplicationDefinition app, String name, String units,
            short usage, ArmID id);

    public ArmMetricGauge32Definition newArmMetricGauge32Definition(
        ArmApplicationDefinition appDef, String name, String units, 
        short usage, ArmID id);

    public ArmMetricGauge64Definition newArmMetricGauge64Definition(
        ArmApplicationDefinition app, String name, String units, 
        short usage, ArmID id);

    public ArmMetricGaugeFloat32Definition 
        newArmMetricGaugeFloat32Definition(
            ArmApplicationDefinition app, String name, String units,
            short usage, ArmID id);

    public ArmMetricNumericId32Definition newArmMetricNumericId32Definition(
        ArmApplicationDefinition app, String name, String units,
        short usage, ArmID id);

    public ArmMetricNumericId64Definition newArmMetricNumericId64Definition(
        ArmApplicationDefinition app, String name, String units, 
        short usage, ArmID id);

    public ArmMetricString32Definition newArmMetricString32Definition(
        ArmApplicationDefinition app, String name, String units, 
        short usage, ArmID id);


/* --- other definitions --- */

    public ArmMetricGroupDefinition newArmMetricGroupDefinition(
        ArmMetricDefinition[] definitions);

    public ArmTransactionWithMetricsDefinition 
        newArmTransactionWithMetricsDefinition(
            ArmApplicationDefinition app, String name, 
            ArmIdentityPropertiesTransaction identityProperties, 
            ArmMetricGroupDefinition definition, ArmID id);


/* --- metric instances --- */

    public ArmMetricCounter32 newArmMetricCounter32(
        ArmMetricCounter32Definition definition);

    public ArmMetricCounter64 newArmMetricCounter64(
        ArmMetricCounter64Definition definition);

    public ArmMetricCounterFloat32 newArmMetricCounterFloat32(
        ArmMetricCounterFloat32Definition definition);

    public ArmMetricGauge32 newArmMetricGauge32(
        ArmMetricGauge32Definition definition);

    public ArmMetricGauge64 newArmMetricGauge64(
        ArmMetricGauge64Definition definition);

    public ArmMetricGaugeFloat32 newArmMetricGaugeFloat32(
        ArmMetricGaugeFloat32Definition definition);

    public ArmMetricNumericId32 newArmMetricNumericId32(
        ArmMetricNumericId32Definition definition);

    public ArmMetricNumericId64 newArmMetricNumericId64(
        ArmMetricNumericId64Definition definition);

    public ArmMetricString32 newArmMetricString32(
        ArmMetricString32Definition definition);


/* --- other instances --- */
    public ArmMetricGroup newArmMetricGroup(
        ArmMetricGroupDefinition groupDefinition, ArmMetric[] metrics);

    public ArmTranReportWithMetrics newArmTranReportWithMetrics(
        ArmApplication app, ArmTransactionWithMetricsDefinition definition, 
        ArmMetricGroup group);

    public ArmTransactionWithMetrics newArmTransactionWithMetrics(
        ArmApplication app, ArmTransactionWithMetricsDefinition definition,
        ArmMetricGroup group);

    public boolean setErrorCallback(ArmErrorCallback errorCallback);
}
