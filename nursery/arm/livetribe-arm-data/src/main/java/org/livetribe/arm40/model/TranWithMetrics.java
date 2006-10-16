package org.livetribe.arm40.model;

/**
 * @version $Revision$ $Date$
 */
public class TranWithMetrics extends AbstractMetricDefinition
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
