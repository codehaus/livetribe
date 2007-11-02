/**
 *  Copyright 2007 Picateers Inc., 1720 S. Amphlett Boulevard  Suite 320, San Mateo, CA 94402 U.S.A. All rights reserved.
 */
package org.livetribe.boot.client;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.concurrent.ScheduledExecutorService;


/**
 * @version $Revision$ $Date$
 */
public class Client
{
    private final InetSocketAddress serverAddress;
    private final InetAddress nic;
    private final ScheduledExecutorService scheduledExecutorService;
    private final ProvisionStore provisionStore;

    public Client(InetSocketAddress serverAddress, InetAddress nic, ScheduledExecutorService scheduledExecutorService, ProvisionStore provisionStore)
    {
        this.serverAddress = serverAddress;
        this.nic = nic;
        this.scheduledExecutorService = scheduledExecutorService;
        this.provisionStore = provisionStore;
    }

    public Client(InetSocketAddress serverAddress, ScheduledExecutorService scheduledExecutorService, ProvisionStore provisionStore)
    {
        this(serverAddress, null, scheduledExecutorService, provisionStore);
    }

    public void start() { }

    public void stop() { }
}
