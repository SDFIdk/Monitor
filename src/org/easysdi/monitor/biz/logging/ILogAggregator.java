package org.easysdi.monitor.biz.logging;

/**
 * Defines an interface for raw logs aggregation.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
public interface ILogAggregator {

    /**
     * Aggregates the raw logs into daily summaries.
     */
    void aggregateRawLogs();
    
    /**
     * Aggregates the raw logs into hourly summaries.
     */
    void aggregateHourRawLogs();
    

}
