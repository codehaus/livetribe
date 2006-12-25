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
 * A simple "do nothing" connection.
 * <p/>
 * This connection is used if no connection is configured.
 *
 * @version $Revision: $ $Date: $
 */
public class DoNothingConnection implements Connection
{
    public void declareIdentityProperties(String idPropOID, String[] idNames, String[] idValues, String[] ctxNames)
    {
    }

    public void declareIdentityPropertiesTransaction(String idPropOID, String[] idNames, String[] idValues, String[] ctxNames, String uriValue)
    {
    }

    public void declareApplicationDefinition(String appDefOID, String name, String idPropOID, byte[] id)
    {
    }

    public void declareApplication(String appOID, String appDefOID, String group, String instance, String[] contextValues)
    {
    }

    public void declareApplicationRemote(String appOID, String appDefOID, String group, String instance, String[] contextValues, byte[] systemAddress)
    {
    }

    public void declareTransactionDefinition(String transDefOID, String name, String idPropTranOID, byte[] id)
    {
    }

    public void associateTransaction(String transOID, String appOID, String transDefOID)
    {
    }

    public void declareTranReport(String tranReportOID, String appOID, String tranDefOID)
    {
    }

    public void declareMetricCounter32Definition(String metricDefOID, String appDefOID, String name, String units, short usage, byte[] id)
    {
    }

    public void declareMetricCounter64Definition(String metricDefOID, String appDefOID, String name, String units, short usage, byte[] id)
    {
    }

    public void declareMetricCounterFloat32Definition(String metricDefOID, String appDefOID, String name, String units, short usage, byte[] id)
    {
    }

    public void declareMetricGauge32Definition(String metricDefOID, String appDefOID, String name, String units, short usage, byte[] id)
    {
    }

    public void declareMetricGauge64Definition(String metricDefOID, String appDefOID, String name, String units, short usage, byte[] id)
    {
    }

    public void declareMetricGaugeFloat32Definition(String metricDefOID, String appDefOID, String name, String units, short usage, byte[] id)
    {
    }

    public void declareMetricNumericId32Definition(String metricDefOID, String appDefOID, String name, String units, short usage, byte[] id)
    {
    }

    public void declareMetricNumericId64Definition(String metricDefOID, String appDefOID, String name, String units, short usage, byte[] id)
    {
    }

    public void declareMetricString32Definition(String metricDefOID, String appDefOID, String name, String units, short usage, byte[] id)
    {
    }

    public void declareMetricGroupDefinition(String metricGroupDefOID, String[] metricDefOIDs)
    {
    }

    public void declareTransactionWithMetricsDefinition(String transDefOID, String appDefOID, String name, String idPropOID, String metricGroupDefOID, byte[] id)
    {
    }

    public void declareMetricGroup(String metricGroupOID, String metricGroupDefOID)
    {
    }

    public void declareTranReportWithMetrics(String tranReportMetricsOID, String tranReportMetricsDefOID, String metricGroupOID)
    {
    }

    public void declareTranWithMetrics(String tranMetricsOID, String tranReportMetricsDefOID, String metricGroupOID)
    {
    }

    public void start(String transOID, byte[] correlator, long start, byte[] parent, String user, String[] contextValues, String contextURI)
    {
    }

    public void update(String transOID, byte[] correlator, long ts)
    {
    }

    public void block(String transOID, byte[] correlator, long handle, long ts)
    {
    }

    public void unblocked(String transOID, byte[] correlator, long handle, long ts)
    {
    }

    public void stop(String transOID, byte[] correlator, long end, int status, String message)
    {
    }

    public void stop(String transOID, byte[] correlator, long end, int status, String message, List[] metrics)
    {
    }

    public void reset(String transOID, byte[] correlator)
    {
    }

    public void report(String reportOID, byte[] parent, byte[] correlator, int status, long respTime, String diagnosticDetail)
    {
    }

    public void report(String reportOID, byte[] parent, byte[] correlator, int status, long respTime, long stopTime, String diagnosticDetail)
    {
    }

    public void report(String reportOID, byte[] parent, byte[] correlator, int status, long respTime, String diagnosticDetail, List[] metrics)
    {
    }

    public void report(String reportOID, byte[] parent, byte[] correlator, int status, long respTime, long stopTime, String diagnosticDetail, List[] metrics)
    {
    }

    public void start()
    {
    }

    public void stop()
    {
    }

    public void close()
    {
    }
}
