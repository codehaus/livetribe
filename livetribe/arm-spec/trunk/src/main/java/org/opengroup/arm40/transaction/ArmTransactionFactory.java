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
/* ArmTransactionFactory                                                     */
/*                                                                           */
/* ArmTransactionFactory provides methods to create instances of the classes */
/* in the org.opengroup.arm40.transaction package. An error callback method  */
/* can be registered for objects created with this factory.                  */
/*                                                                           */
/* ArmTransactionFactory is instantiated using a class loader. The actual    */
/* name of the factory implementation class is obtained from the system      */
/* property whose name is provided in the propertyKey constant of            */
/* ArmTransactionFactory.                                                    */
/*                                                                           */
/* ------------------------------------------------------------------------- */

package org.opengroup.arm40.transaction;

public interface ArmTransactionFactory extends ArmInterface {

// Public Constants
    public static final String propertyKey = "Arm40.ArmTransactionFactory";

// Public Instance Methods
    public ArmApplication newArmApplication(
        ArmApplicationDefinition definition, String group, 
        String instance, String[] contextValues);
        
    public ArmApplicationDefinition newArmApplicationDefinition(
        String name, ArmIdentityProperties identityProperties, ArmID id);
        
    public ArmCorrelator newArmCorrelator(byte[] corrBytes);
    
    public ArmCorrelator newArmCorrelator(byte[] corrBytes, int offset);
    
    public ArmID newArmID(byte[] idBytes);
    
    public ArmID newArmID(byte[] idBytes, int offset);
    
    public ArmIdentityProperties newArmIdentityProperties(
        String[] identityNames, String[] identityValues, 
        String[] contextNames);
        
    public ArmIdentityPropertiesTransaction 
        newArmIdentityPropertiesTransaction(
            String[] identityNames, String[] identityValues, 
            String[] contextNames, String uriValue);
            
    public ArmTransaction newArmTransaction(ArmApplication app, 
        ArmTransactionDefinition definition);
        
    public ArmTransactionDefinition newArmTransactionDefinition(
	    ArmApplicationDefinition app, String name, 
        ArmIdentityPropertiesTransaction identityProperties, ArmID id);
        
    public ArmUser newArmUser(String name, ArmID id);
    
    public boolean setErrorCallback(ArmErrorCallback errorCallback);
}
