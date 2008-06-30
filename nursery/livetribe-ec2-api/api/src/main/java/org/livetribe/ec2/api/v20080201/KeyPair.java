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
package org.livetribe.ec2.api.v20080201;

/**
 * A response POJO from a <code>createKeyPair()</code> request.
 *
 * @version $Revision$ $Date$
 * @see org.livetribe.ec2.api.v20080201.EC2API#createKeyPair(String)
 */
public class KeyPair
{
    private final String name;
    private final String fingerprint;
    private final String material;

    /**
     * Constructor to be used if the RSA private key is not provided
     *
     * @param name        the key pair name provided in the original request
     * @param fingerprint the SHA-1 digest of the DER encoded private key
     */
    public KeyPair(String name, String fingerprint)
    {
        if (name == null) throw new IllegalArgumentException("name cannot be null");
        if (fingerprint == null) throw new IllegalArgumentException("fingerprint cannot be null");

        this.name = name;
        this.fingerprint = fingerprint;
        this.material = null;
    }

    /**
     * Constructor to be used if the RSA private key is  provided
     *
     * @param name        the key pair name provided in the original request
     * @param fingerprint the SHA-1 digest of the DER encoded private key
     * @param material    the unencrypted PEM encoded RSA private key
     */
    public KeyPair(String name, String fingerprint, String material)
    {
        if (name == null) throw new IllegalArgumentException("name cannot be null");
        if (fingerprint == null) throw new IllegalArgumentException("fingerprint cannot be null");
        if (material == null) throw new IllegalArgumentException("material cannot be null");

        this.name = name;
        this.fingerprint = fingerprint;
        this.material = material;
    }

    /**
     * The key pair name provided in the original request.
     *
     * @return the key pair name provided in the original request
     */
    public String getName()
    {
        return name;
    }

    /**
     * A SHA-1 digest of the DER encoded private key
     *
     * @return the SHA-1 digest of the DER encoded private key
     */
    public String getFingerprint()
    {
        return fingerprint;
    }

    /**
     * An unencrypted PEM encoded RSA private key.
     *
     * @return the unencrypted PEM encoded RSA private key
     */
    public String getMaterial()
    {
        return material;
    }

    @Override
    public String toString()
    {
        return "(KeyPair name: " + name + " fingerprint: " + fingerprint + " material: " + material + ")";
    }
}
