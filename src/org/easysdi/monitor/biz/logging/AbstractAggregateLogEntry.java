package org.easysdi.monitor.biz.logging;

import java.util.Calendar;

import org.easysdi.monitor.dat.dao.LogDaoHelper;

/**
 * Holds aggregate stats for a job or a query.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
public abstract class AbstractAggregateLogEntry {

    private AggregateStats h24Stats;
    private Calendar       logDate;
    private AggregateStats slaStats;



    /**
     * Defines the stats on a 24-hour period. 
     * 
     * @param newH24Stats  the aggregate stats for the 24-hour period
     */
    protected void setH24Stats(AggregateStats newH24Stats) {
        this.h24Stats = newH24Stats;
    }



    /**
     * Gets the stats on a 24-hour period.
     * 
     * @return  the aggregate stats on 24 hours
     */
    public AggregateStats getH24Stats() {
        return this.h24Stats;
    }



    /**
     * Defines the date of the aggregated logs.
     * 
     * @param newLogDate   the date of the logs that were aggregated   
     */
    protected void setLogDate(Calendar newLogDate) {
        this.logDate = newLogDate;
    }



    /**
     * Returns the date of the aggregated logs.
     * 
     * @return the date of the logs that were aggregated
     */
    public Calendar getLogDate() {
        return this.logDate;
    }



    /**
     * Defines the stats for the validated time period.
     * 
     * @param newSlaStats  the aggregate stats for the validated time period
     */
    protected void setSlaStats(AggregateStats newSlaStats) {
        this.slaStats = newSlaStats;
    }



    /**
     * Gets the stats for the validated time period.
     * 
     * @return  the aggregate stats for the validated time period
     */
    public AggregateStats getSlaStats() {
        return this.slaStats;
    }



    /**
     * Erases this aggregate log entry through the persistence mechanism.
     * <p>
     * This won't erase the corresponding raw log entries.
     * 
     * @return  <code>true</code> if this aggregate log entry has been
     *          successfully deleted
     *          <code>false</code> otherwise
     */
    public boolean delete() {
        return LogDaoHelper.getLogDao().deleteAggregLog(this);
    }
}
