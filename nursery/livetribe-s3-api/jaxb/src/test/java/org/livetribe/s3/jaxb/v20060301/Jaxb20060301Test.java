/**
 *
 * Copyright 2008-2011(C) The original author or authors
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
package org.livetribe.s3.jaxb.v20060301;

import javax.xml.bind.JAXBContext;
import java.io.StringReader;

import org.junit.Test;


/**
 * @version $Revision$ $Date: 2008-05-30 09:40:48 -0700 (Fri, 30 May 2008) $
 */
public class Jaxb20060301Test
{
    @Test
    public void testCreateBucket() throws Exception
    {
        JAXBContext context20060301 = JAXBContext.newInstance("org.livetribe.s3.jaxb.v20060301");

        context20060301.createUnmarshaller().unmarshal(new StringReader("<CreateBucket xmlns=\"http://s3.amazonaws.com/doc/2006-03-01/\">\n" +
                                                                        "  <Bucket>quotes</Bucket>\n" +
                                                                        "  <AWSAccessKeyId>1D9FVRAYCP1VJEXAMPLE=</AWSAccessKeyId>\n" +
                                                                        "  <Timestamp>2006-03-01T12:00:00.183Z</Timestamp>\n" +
                                                                        "  <Signature>Iuyz3d3P0aTou39dzbqaEXAMPLE=</Signature>\n" +
                                                                        "</CreateBucket>"));
    }

    @Test
    public void testListBucketResult() throws Exception
    {
        JAXBContext context20060301 = JAXBContext.newInstance("org.livetribe.s3.jaxb.v20060301");

        context20060301.createUnmarshaller().unmarshal(new StringReader("<ListBucketResponse xmlns=\"http://s3.amazonaws.com/doc/2006-03-01/\">\n" +
                                                                        "  <Name>quotes</Name>\n" +
                                                                        "  <Prefix>N</Prefix>\n" +
                                                                        "  <Marker>Ned</Marker>\n" +
                                                                        "  <MaxKeys>40</MaxKeys>\n" +
                                                                        "  <IsTruncated>false</IsTruncated>\n" +
                                                                        "  <Contents>\n" +
                                                                        "    <Key>Nelson</Key>\n" +
                                                                        "    <LastModified>2006-01-01T12:00:00.000Z</LastModified>\n" +
                                                                        "    <ETag>&quot;828ef3fdfa96f00ad9f27c383fc9ac7f&quot;</ETag>\n" +
                                                                        "    <Size>5</Size>\n" +
                                                                        "    <StorageClass>STANDARD</StorageClass>\n" +
                                                                        "    <Owner>\n" +
                                                                        "      <ID>bcaf1ffd86f41caff1a493dc2ad8c2c281e37522a640e161ca5fb16fd081034f</ID>\n" +
                                                                        "      <DisplayName>webfile</DisplayName>\n" +
                                                                        "     </Owner>\n" +
                                                                        "  </Contents>\n" +
                                                                        "  <Contents>\n" +
                                                                        "    <Key>Neo</Key>\n" +
                                                                        "    <LastModified>2006-01-01T12:00:00.000Z</LastModified>\n" +
                                                                        "    <ETag>&quot;828ef3fdfa96f00ad9f27c383fc9ac7f&quot;</ETag>\n" +
                                                                        "    <Size>4</Size>\n" +
                                                                        "    <StorageClass>STANDARD</StorageClass>\n" +
                                                                        "     <Owner>\n" +
                                                                        "      <ID>bcaf1ffd86f41caff1a493dc2ad8c2c281e37522a640e161ca5fb16fd081034f</ID>\n" +
                                                                        "      <DisplayName>webfile</DisplayName>\n" +
                                                                        "    </Owner>\n" +
                                                                        " </Contents>\n" +
                                                                        "</ListBucketResponse>"));
    }

    @Test
    public void testAnotherListBucketResult() throws Exception
    {
        JAXBContext context20060301 = JAXBContext.newInstance("org.livetribe.s3.jaxb.v20060301");

        context20060301.createUnmarshaller().unmarshal(new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                                                                        "<ListAllMyBucketsResult xmlns=\"http://s3.amazonaws.com/doc/2006-03-01/\">" +
                                                                        "<Owner>" +
                                                                        "<ID>552975a30196c72b56d22b412eb483b198ad51c651fd0a18727750816f85399d</ID>" +
                                                                        "<DisplayName>sysadmin-beta</DisplayName>" +
                                                                        "</Owner>" +
                                                                        "<Buckets/>" +
                                                                        "</ListAllMyBucketsResult>"));
    }
}
