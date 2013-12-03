/**
 * 
 */
package org.easysdi.monitor.biz.logging;

import java.io.Serializable;
import java.util.Calendar;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.easysdi.monitor.biz.job.Job;
import org.easysdi.monitor.biz.util.HashCodeConstants;

/**
 * Represents aggregation stats of raw log entries for a given hour and 
 * a given job.
 * 
 * @author Thomas Bergstedt
 * 2011-05-13
 *
 */
public class JobAggregateHourLogEntry extends AbstractAggregateHourLogEntry implements
Serializable {
	
	 private static final long serialVersionUID = 426114429754090200L;
	 private long              jobId;
	
	/**
	 * Dummy constructor
	 */
	@SuppressWarnings("unused")
	private JobAggregateHourLogEntry() {
	}
	
    /**
     * Creates a job aggregate hour log entry.
     * 
     * @param   parentJob   the job for which the stats are calculated
     * @param   dateLog     the date of the aggregated raw logs
     * @param   h1Stats    the aggregate stats for the 1 hour period
     * @param   inspireStats    the aggregate stats for the inspire
     */
    public JobAggregateHourLogEntry(Job parentJob, Calendar dateLog,
                    AggregateStats h1Stats, AggregateStats inspireStats) {
        this(parentJob, dateLog);
        this.setH1Stats(h1Stats);
        this.setInspireStats(inspireStats);
    }
    
    /**
     * Creates a new job aggregate log entry with empty stats.
     * 
     * @param   parentJob   the job for which the stats are calculated
     * @param   dateLog     the date of the aggregated raw logs
     */
    public JobAggregateHourLogEntry(Job parentJob, Calendar dateLog) {

        if (null == parentJob || !parentJob.isValid(false)) {
            throw new IllegalArgumentException(
                   "Parent job must be a valid one");
        }

        if (null == dateLog) {
            throw new IllegalArgumentException("Log date can't be null");
        }

        if (dateLog.compareTo(Calendar.getInstance()) > -1) {
            throw new IllegalArgumentException("Log date must be in the past");
        }
        this.setJobId(parentJob.getJobId());
        this.setLogDate(dateLog);
    }
    
    /**
     * Defines the identifier of the job for which the stats are calculated.
     * <p>
     * <i><b>Note:</b> This method is intended for internal purposes and 
     * shouldn't be called directly.</i>
     * 
     * @param   parentJobId the long identifying the parent job
     */
    private void setJobId(long parentJobId) {
        if (1 > parentJobId) {
            throw new IllegalArgumentException(
                   "Identifier must be strictly positive.");
        }
        this.jobId = parentJobId;
    }



    /**
     * Gets the identifier of the job for which the stats are calculated.
     * 
     * @return the long identifying the parent job
     */
    public long getJobId() {
        return this.jobId;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object anObject) {

        if (anObject instanceof JobAggregateHourLogEntry) {
            final JobAggregateHourLogEntry that = (JobAggregateHourLogEntry) anObject;

            return this.getJobId() == that.getJobId() 
                            && this.getLogDate() == that.getLogDate();
        }
        return false;
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder 
            = new HashCodeBuilder(HashCodeConstants.SEED, 
                                  HashCodeConstants.MULTIPLIER);
        
        hashCodeBuilder.append(this.getJobId());
        hashCodeBuilder.append(this.getLogDate());
        return hashCodeBuilder.toHashCode();
    }
	
}
