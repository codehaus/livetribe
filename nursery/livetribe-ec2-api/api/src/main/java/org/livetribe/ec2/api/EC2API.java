/**
 *
 * Copyright 2008 (C) The original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.livetribe.ec2.api;

import java.util.Set;

import org.livetribe.ec2.model.AmazonImage;


/**
 * @version $Revision$ $Date$
 */
public interface EC2API
{
    String registerImage(String imageLocation) throws EC2Exception;

    Set<AmazonImage> describeImages(String[] imageId, String[] owner, String[] executedBy) throws EC2Exception;

    boolean deregisterImage(String imageId) throws EC2Exception;
}
