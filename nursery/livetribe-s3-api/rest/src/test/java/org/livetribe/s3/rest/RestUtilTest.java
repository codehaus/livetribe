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
package org.livetribe.s3.rest;

import org.apache.ahc.HttpHeaders;
import org.apache.ahc.api.HttpVerb;
import org.junit.Assert;
import org.junit.Test;


/**
 * @version $Revision$ $Date$
 */
public class RestUtilTest
{
    private final static String AWSSecretAccessKey = "uV3F3YluFJax1cknvbcGwgjvx4QpvB+leU8dUj2o";

    @Test
    public void testUnfold()
    {
        String test = "This    is a \n test\nfor unfolding.";

        Assert.assertEquals("This is a test for unfolding.", RestUtil.unfold(test));
    }

    @Test
    public void testSignExampleObjectGET() throws Exception
    {
        HttpHeaders headers = new HttpHeaders();

        headers.put("Host", "johnsmith.s3.amazonaws.com");
        headers.put("Date", "Tue, 27 Mar 2007 19:36:42 +0000");

        Assert.assertEquals("xXjDGYUmKxnwqr5KXNPGldn5LbA=", RestUtil.sign(headers, AWSSecretAccessKey, HttpVerb.GET, "/johnsmith/photos/puppy.jpg"));
    }

    @Test
    public void testSignExampleObjectPUT() throws Exception
    {
        HttpHeaders headers = new HttpHeaders();

        headers.put("Content-Type", "image/jpeg");
        headers.put("Content-Length", "94328");
        headers.put("Host", "johnsmith.s3.amazonaws.com");
        headers.put("Date", "Tue, 27 Mar 2007 21:15:45 +0000");

        Assert.assertEquals("hcicpDDvL9SsO6AkvxqmIWkmOuQ=", RestUtil.sign(headers, AWSSecretAccessKey, HttpVerb.PUT, "/johnsmith/photos/puppy.jpg"));
    }

    @Test
    public void testSignExampleList() throws Exception
    {
        HttpHeaders headers = new HttpHeaders();

        headers.put("User-Agent", "Mozilla/5.0");
        headers.put("Host", "johnsmith.s3.amazonaws.com");
        headers.put("Date", "Tue, 27 Mar 2007 19:42:41 +0000");

        Assert.assertEquals("jsRt/rhG+Vtp88HrYL706QhE4w4=", RestUtil.sign(headers, AWSSecretAccessKey, HttpVerb.GET, "/johnsmith/"));
    }

    @Test
    public void testSignExampleFetch() throws Exception
    {
        HttpHeaders headers = new HttpHeaders();

        headers.put("Host", "johnsmith.s3.amazonaws.com");
        headers.put("Date", "Tue, 27 Mar 2007 19:44:46 +0000");

        Assert.assertEquals("thdUi9VAkzhkniLj96JIrOPGi0g=", RestUtil.sign(headers, AWSSecretAccessKey, HttpVerb.GET, "/johnsmith/?acl"));
    }

    @Test
    public void testSignExampleDelete() throws Exception
    {
        HttpHeaders headers = new HttpHeaders();

        headers.put("Host", "johnsmith.s3.amazonaws.com");
        headers.put("x-amz-date", "Tue, 27 Mar 2007 21:20:26 +0000");
        headers.put("Date", "Tue, 27 Mar 2007 21:20:27 +0000");

        Assert.assertEquals("k3nL7gH3+PadhTEVn5Ip83xlYzk=", RestUtil.sign(headers, AWSSecretAccessKey, HttpVerb.DELETE, "/johnsmith/photos/puppy.jpg"));
    }

    @Test
    public void testSignExampleUpload() throws Exception
    {
        HttpHeaders headers = new HttpHeaders();

        headers.put("User-Agent", "curl/7.15.5");
        headers.put("Host", "static.johnsmith.net:8080");
        headers.put("Date", "Tue, 27 Mar 2007 21:06:08 +0000");
        headers.put("x-amz-acl", "public-read");
        headers.put("content-type", "application/x-download");
        headers.put("Content-MD5", "4gJE4saaMU4BqNR0kLY+lw==");
        headers.put("X-Amz-Meta-ReviewedBy", "joe@johnsmith.net");
        headers.put("X-Amz-Meta-ReviewedBy", "jane@johnsmith.net");
        headers.put("X-Amz-Meta-FileChecksum", "0x02661779");
        headers.put("X-Amz-Meta-ChecksumAlgorithm", "crc32");
        headers.put("Content-Disposition", "attachment; filename=database.dat");
        headers.put("Content-Encoding", "gzip");
        headers.put("Content-Length", "5913339");

        Assert.assertEquals("C0FlOtU8Ylb9KDTpZqYkZPX91iI=", RestUtil.sign(headers, AWSSecretAccessKey, HttpVerb.PUT, "/static.johnsmith.net/db-backup.dat.gz"));
    }
}
