/**
 *
 * Copyright 2006 (C) The original author or authors
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
package org.livetribe.arm40.model;

/**
 * @version $Revision$ $Date$
 */
public class TranReportWithMetrics extends AbstractMetricDefinition
{
    private TransactionWithMetricsDefinition transactionWithMetricsDefinition;
    private MetricGroupDefinition metricGroupDefinition;

    public TransactionWithMetricsDefinition getTransactionWithMetricsDefinition()
    {
        return transactionWithMetricsDefinition;
    }

    public void setTransactionWithMetricsDefinition(TransactionWithMetricsDefinition transactionWithMetricsDefinition)
    {
        this.transactionWithMetricsDefinition = transactionWithMetricsDefinition;
    }

    public MetricGroupDefinition getMetricGroupDefinition()
    {
        return metricGroupDefinition;
    }

    public void setMetricGroupDefinition(MetricGroupDefinition metricGroupDefinition)
    {
        this.metricGroupDefinition = metricGroupDefinition;
    }
}
