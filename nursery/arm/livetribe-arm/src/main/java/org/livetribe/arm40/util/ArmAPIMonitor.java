/**
 *  Copyright 2006 Simula Labs Corporation, 4676 Admiralty Way, Suite 520 Marina del Rey, CA 90292 U.S.A. All rights reserved.
 */
package org.livetribe.arm40.util;

import org.opengroup.arm40.transaction.ArmInterface;


/**
 * @version $Revision: $ $Date: $
 */
public interface ArmAPIMonitor
{
    public void begin(ArmInterface object);

    public void warning(int code);

    public void error(int code);

    public void end();
}
