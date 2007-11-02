/**
 *  Copyright 2007 Picateers Inc., 1720 S. Amphlett Boulevard  Suite 320, San Mateo, CA 94402 U.S.A. All rights reserved.
 */
package org.livetribe.boot.server;

import net.jcip.annotations.Immutable;

/**
 * @version $Revision$ $Date$
 */
@Immutable
public class ProvisionPair
{
    private final String name;
    private final long version;

    public ProvisionPair(String name, long version)
    {
        this.name = name;
        this.version = version;
    }

    public String getName()
    {
        return name;
    }

    public long getVersion()
    {
        return version;
    }
}
