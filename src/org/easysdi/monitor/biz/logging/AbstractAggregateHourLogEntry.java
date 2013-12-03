/**
 * 
 */
package org.easysdi.monitor.biz.logging;

import java.util.Calendar;

import org.easysdi.monitor.dat.dao.LogDaoHelper;

/**
 * @author berg3428
 *
 */
public abstract class AbstractAggregateHourLogEntry {

	private AggregateStats h1Stats;
	private Calendar       logDate; 
	private AggregateStats inspireStats;
	
	/**
	 * @return the h1Stats
	 */
	public AggregateStats getH1Stats() {
		return h1Stats;
	}
	
	/**
	 * @param h1Stats the h1Stats to set
	 */
	public void setH1Stats(AggregateStats h1Stats) {
		this.h1Stats = h1Stats;
	}
	
	/**
	 * @return the logDate
	 */
	public Calendar getLogDate() {
		return logDate;
	}
	
	/**
	 * @param logDate the logDate to set
	 */
	public void setLogDate(Calendar logDate) {
		this.logDate = logDate;
	}
	
	/**
	 * @return the inspireStats
	 */
	public AggregateStats getInspireStats() {
		return inspireStats;
	}
	
	/**
	 * @param inspireStats the inspireStats to set
	 */
	public void setInspireStats(AggregateStats inspireStats) {
		this.inspireStats = inspireStats;
	}
	
	 /**
     * Erases this aggregate hour log entry through the persistence mechanism.
     * <p>
     * This won't erase the corresponding raw log entries.
     * 
     * @return  <code>true</code> if this aggregate hour log entry has been
     *          successfully deleted
     *          <code>false</code> otherwise
     */
    public boolean delete() {
        return LogDaoHelper.getLogDao().deleteAggregHourLog(this);
    }
}
