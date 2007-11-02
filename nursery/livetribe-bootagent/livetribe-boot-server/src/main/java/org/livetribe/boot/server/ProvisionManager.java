/**
 *  Copyright 2007 Picateers Inc., 1720 S. Amphlett Boulevard  Suite 320, San Mateo, CA 94402 U.S.A. All rights reserved.
 */
package org.livetribe.boot.server;

import java.io.OutputStream;
import java.util.List;

/**
 * @version $Revision$ $Date$
 */
public interface ProvisionManager
{
    List<ProvisionPair> obtain(String uuid);

    OutputStream obtainOutputStream(String uuid, String name, long version);
}
