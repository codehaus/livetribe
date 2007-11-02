/**
 *  Copyright 2007 Picateers Inc., 1720 S. Amphlett Boulevard  Suite 320, San Mateo, CA 94402 U.S.A. All rights reserved.
 */
package org.livetribe.boot.client;

import java.net.URL;
import java.util.List;

/**
 * @version $Revision$ $Date$
 */
public interface ProvisionStore
{
    String getUuid();

    void setUuid();

    void setProvisionList(List<ProvisionPair> list);

    List<ProvisionPair> getUpdateList();

    URL getClasspath();
}
