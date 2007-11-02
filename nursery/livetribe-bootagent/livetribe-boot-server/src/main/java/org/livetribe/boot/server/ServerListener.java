/**
 *  Copyright 2007 Picateers Inc., 1720 S. Amphlett Boulevard  Suite 320, San Mateo, CA 94402 U.S.A. All rights reserved.
 */
package org.livetribe.boot.server;

import java.net.InetAddress;

/**
 * A listener interface that provides access to the internal processing of the
 * boot agent server.
 *
 * @version $Revision$ $Date$
 */
public interface ServerListener
{
    void started();

    void processedRequest(InetAddress from);

    void stopped();
}
