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
/* ArmConstants                                                              */
/*                                                                           */
/* ArmConstants are constants that are widely used in the ARM 4.0            */
/* interfaces. They are declared in one interface as a matter of convenience */
/* (instead of duplicating them in multiple interfaces).                     */
/*                                                                           */
/* ------------------------------------------------------------------------- */

package org.opengroup.arm40.transaction;

public interface ArmConstants {

// No Public Constructors

// Constants

    public static final int CORR_MAX_LENGTH = 512;
        // Maximum length of a correlator (currently = 512 bytes)

    public static final int CORR_MIN_LENGTH = 4;
        // Minimum length of a correlator ( = 4 bytes)

    public static final int DIAG_DETAILS_MAX_LENGTH = 4095;
        // Maximum length of diagnostic details string
        // (currently = 4095 characters)

    public static final int ID_LENGTH = 16;
        // Length of all IDs (16 bytes) 

    public static final int METRIC_MAX_COUNT = 7;
        // Maximum number of metric slots (currently = 7)

    public static final int METRIC_MAX_INDEX = 6;
        // Maximum index of a metric slot (currently = 6)

    public static final int METRIC_MIN_INDEX = 0;
        // Minimum index of a metric slot (currently = 0)

    public static final int NAME_MAX_LENGTH = 127;
        // Maximum chars in app/tran/metric name (currently = 127)

    public static final int PROPERTY_MAX_COUNT = 20;
        // Maximum number of identity and context properties
        // (currently = 20)

    public static final int PROPERTY_MAX_INDEX = 19;
        // Maximum array index of an identity or context property 
        // (currently = 19)

    public static final int PROPERTY_MIN_INDEX = 0;
        // Miniimum array index of an identity or context property 
        // (currently = 0)

    public static final int PROPERTY_NAME_MAX_LENGTH = 127;
        // Maximum chars in an identity or context property
        // (currently = 127)

    public static final int PROPERTY_URI_MAX_LENGTH = 4095;
        // Maximum chars in an URI property (currently = 4095)

    public static final int PROPERTY_VALUE_MAX_LENGTH = 255;
        // Maximum chars in an identity or context property 
        // (currently = 255)


    public static final int STATUS_ABORT = 1;
        // Valid status value for ArmTranReport and ArmTransaction (=1)

    public static final int STATUS_FAILED = 2;
        // Valid status value for ArmTranReport and ArmTransaction (=2)

    public static final int STATUS_GOOD = 0;
        // Valid status value for ArmTranReport and ArmTransaction (=0)

    public static final int STATUS_INVALID = -1;
        // Status value used when appl. passes an invalid value (=-1)

    public static final int STATUS_UNKNOWN = 3;
        // Valid status value for ArmTranReport and ArmTransaction (=3)
        

    public static final int USE_CURRENT_TIME = -1;
        // Used with ArmTranReport (currently = -1)
        
// No Instance Methods
}
