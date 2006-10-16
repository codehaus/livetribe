/**
 *
 * Copyright 2006 (C) The original author or authors.
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
package org.livetribe.arm40.connection;

import java.util.List;


/**
 * @version $Revision: $ $Date: $
 */
public interface Connection
{
    /**
     * @param idPropOID
     * @param idNames
     * @param idValues
     * @param ctxNames
     */
    public void declareIdentityProperties(String idPropOID, String[] idNames, String[] idValues, String[] ctxNames);

    /**
     * @param idPropOID
     * @param idNames
     * @param idValues
     * @param ctxNames
     * @param uriValue
     */
    public void declareIdentityPropertiesTransaction(String idPropOID, String[] idNames, String[] idValues, String[] ctxNames, String uriValue);

    /**
     * @param appDefOID
     * @param name
     * @param idPropOID
     * @param id
     */
    public void declareApplicationDefinition(String appDefOID, String name, String idPropOID, byte[] id);

    /**
     * @param appOID
     * @param appDefOID
     * @param group
     * @param instance
     * @param contextValues
     */
    public void declareApplication(String appOID, String appDefOID, String group, String instance, String[] contextValues);

    /**
     * @param appOID
     * @param appDefOID
     * @param group
     * @param instance
     * @param contextValues
     * @param systemAddress
     */
    public void declareApplicationRemote(String appOID, String appDefOID, String group, String instance, String[] contextValues, byte[] systemAddress);

    /**
     * @param transDefOID
     * @param name
     * @param idPropTranOID
     * @param id
     */
    public void declareTransactionDefinition(String transDefOID, String name, String idPropTranOID, byte[] id);

    /**
     * @param transOID
     * @param appOID
     * @param transDefOID
     */
    public void associateTransaction(String transOID, String appOID, String transDefOID);

    /**
     * @param tranReportOID
     * @param appOID
     * @param tranDefOID
     */
    public void declareTranReport(String tranReportOID, String appOID, String tranDefOID);

    /**
     * @param metricDefOID
     * @param appDefOID
     * @param name
     * @param units
     * @param usage
     * @param id
     */
    public void declareMetricCounter32Definition(String metricDefOID, String appDefOID, String name, String units, short usage, byte[] id);

    /**
     * @param metricDefOID
     * @param appDefOID
     * @param name
     * @param units
     * @param usage
     * @param id
     */
    public void declareMetricCounter64Definition(String metricDefOID, String appDefOID, String name, String units, short usage, byte[] id);

    /**
     * @param metricDefOID
     * @param appDefOID
     * @param name
     * @param units
     * @param usage
     * @param id
     */
    public void declareMetricCounterFloat32Definition(String metricDefOID, String appDefOID, String name, String units, short usage, byte[] id);

    /**
     * @param metricDefOID
     * @param appDefOID
     * @param name
     * @param units
     * @param usage
     * @param id
     */
    public void declareMetricGauge32Definition(String metricDefOID, String appDefOID, String name, String units, short usage, byte[] id);

    /**
     * @param metricDefOID
     * @param appDefOID
     * @param name
     * @param units
     * @param usage
     * @param id
     */
    public void declareMetricGauge64Definition(String metricDefOID, String appDefOID, String name, String units, short usage, byte[] id);

    /**
     * @param metricDefOID
     * @param appDefOID
     * @param name
     * @param units
     * @param usage
     * @param id
     */
    public void declareMetricGaugeFloat32Definition(String metricDefOID, String appDefOID, String name, String units, short usage, byte[] id);

    /**
     * @param metricDefOID
     * @param appDefOID
     * @param name
     * @param units
     * @param usage
     * @param id
     */
    public void declareMetricNumericId32Definition(String metricDefOID, String appDefOID, String name, String units, short usage, byte[] id);

    /**
     * @param metricDefOID
     * @param appDefOID
     * @param name
     * @param units
     * @param usage
     * @param id
     */
    public void declareMetricNumericId64Definition(String metricDefOID, String appDefOID, String name, String units, short usage, byte[] id);

    /**
     * @param metricDefOID
     * @param appDefOID
     * @param name
     * @param units
     * @param usage
     * @param id
     */
    public void declareMetricString32Definition(String metricDefOID, String appDefOID, String name, String units, short usage, byte[] id);

    /**
     * @param metricGroupDefOID
     * @param metricDefOIDs
     */
    public void declareMetricGroupDefinition(String metricGroupDefOID, String[] metricDefOIDs);

    /**
     * @param transDefOID
     * @param appDefOID
     * @param name
     * @param idPropOID
     * @param metricGroupDefOID
     * @param id
     */
    public void declareTransactionWithMetricsDefinition(String transDefOID, String appDefOID, String name, String idPropOID, String metricGroupDefOID, byte[] id);

    /**
     * @param metricGroupOID
     * @param metricGroupDefOID
     */
    public void declareMetricGroup(String metricGroupOID, String metricGroupDefOID);

    /**
     * @param tranReportMetricsOID
     * @param tranReportMetricsDefOID
     * @param metricGroupOID
     */
    public void declareTranReportWithMetrics(String tranReportMetricsOID, String tranReportMetricsDefOID, String metricGroupOID);

    /**
     * @param tranMetricsOID
     * @param tranReportMetricsDefOID
     * @param metricGroupOID
     */
    public void declareTranWithMetrics(String tranMetricsOID, String tranReportMetricsDefOID, String metricGroupOID);

    public void start(String transOID, byte[] correlator, long start, byte[] parent, String user, String[] contextValues, String contextURI);

    public void update(String transOID, byte[] correlator, long ts);

    public void block(String transOID, byte[] correlator, long handle, long ts);

    public void unblocked(String transOID, byte[] correlator, long handle, long ts);

    public void stop(String transOID, byte[] correlator, long end, int status, String message);

    public void stop(String transOID, byte[] correlator, long end, int status, String message, List[] metrics);

    public void reset(String transOID, byte[] correlator);

    public void report(String reportOID, byte[] parent, byte[] correlator, int status, long respTime, String diagnosticDetail);

    public void report(String reportOID, byte[] parent, byte[] correlator, int status, long respTime, long stopTime, String diagnosticDetail);

    public void report(String reportOID, byte[] parent, byte[] correlator, int status, long respTime, String diagnosticDetail, List[] metrics);

    public void report(String reportOID, byte[] parent, byte[] correlator, int status, long respTime, long stopTime, String diagnosticDetail, List[] metrics);

    public void start();

    public void stop();

    public void close();
}
