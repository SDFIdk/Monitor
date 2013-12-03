/**
 * 
 */
package org.easysdi.monitor.biz.logging;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.easysdi.monitor.biz.job.Job;
import org.easysdi.monitor.dat.dao.ILogDao.ParentType;
import org.easysdi.monitor.dat.dao.LogDaoHelper;

/**
 * Gives access to the logs of a job.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
public class JobLogFetcher implements ILogFetcher {

    private final Logger logger = Logger.getLogger(JobLogFetcher.class);
    
    private Job parentJob;



    /**
     * Creates a new log fetcher.
     * 
     * @param job   the job whose logs are accessed by this fetcher
     */
    public JobLogFetcher(Job job) {
        this.setParentJob(job);
    }



    /**
     * Defines the job whose logs are accessed by this fetcher.
     * 
     * @param   job the parent job
     */
    private void setParentJob(Job job) {
        this.parentJob = job;
    }



    /**
     * Gets the job whose logs are accessed by this fetcher.
     * 
     * @return the parent job
     */
    public Job getParentJob() {
        return this.parentJob;
    }



    /**
     * {@inheritDoc}
     */
    public Map<Date, AbstractAggregateLogEntry> fetchAggregLogs() {
        return this.fetchAggregLogsSubset(null, null, null, null);
    }
    
    /**
     * {@inheritDoc}
     */
    public Map<Date, AbstractAggregateHourLogEntry> fetchAggregHourLogs()
    {
    	return this.fetchAggregHourLogsSubset(null,null,null,null);
    }
    
    /**
     * {@inheritDoc}
     */
    public Map<Date, AbstractAggregateHourLogEntry> fetchAggregHourLogsSubset(Calendar minDate,
            Calendar maxDate, Integer maxResults, Integer startIndex)
    {
    	  try {

              return LogDaoHelper.getLogDao().fetchAggregHourLogs(
                      ParentType.JOB, this.getParentJob().getJobId(), minDate,
                      maxDate, maxResults, startIndex);

          } catch (LogFetcherException e) {
              this.logger.error(
                     "An error occurred while the aggregate logs were fetched.", 
                     e);
              return null;

          }
    }



    /**
     * {@inheritDoc}
     */
    public Map<Date, AbstractAggregateLogEntry> fetchAggregLogsSubset(
                    Calendar minDate, Calendar maxDate, Integer maxResults,
                    Integer startIndex) {

        try {

            return LogDaoHelper.getLogDao().fetchAggregLogs(
                    ParentType.JOB, this.getParentJob().getJobId(), minDate,
                    maxDate, maxResults, startIndex);

        } catch (LogFetcherException e) {
            this.logger.error(
                   "An error occurred while the aggregate logs were fetched.", 
                   e);
            return null;

        }

    }



    /**
     * {@inheritDoc}
     */
    public Set<RawLogEntry> fetchRawLogs() {
        return this.fetchRawLogsSubset(null, null, null, null);
    }
    




    /**
     * {@inheritDoc}
     */
    public Set<RawLogEntry> fetchRawLogsSubset(Calendar minDate,
                Calendar maxDate, Integer maxResults, Integer startIndex) {

        final Set<Long> queriesIds = this.getParentJob().getQueriesIds();

        try {
            return LogDaoHelper.getLogDao().fetchRawLogs(
                    queriesIds.toArray(new Long[queriesIds.size()]), minDate, 
                    maxDate, maxResults, startIndex);
        
        } catch (LogFetcherException e) {
            this.logger.error(
                   "An error occurred while the raw logs were fetched", 
                   e);
            return null;

        }
    }

    /**
     * {@inheritDoc}
     */
    public RawLogEntry fetchLastLogBeforeDate(Calendar date) {
        final Collection<Long> queryIds = this.getParentJob().getQueriesIds();

        return LogDaoHelper.getLogDao().fetchLastLogBeforeDate(
                queryIds.toArray(new Long[queryIds.size()]), date);

    }



    /**
     * {@inheritDoc}
     */
    public long getAggregateLogsItemsNumber(Calendar minDate, Calendar maxDate,
                    Integer maxResults, Integer startIndex) {
        
        return LogDaoHelper.getLogDao().getAggregateLogsItemsNumber(
                ParentType.JOB, this.parentJob.getJobId(), minDate, 
                maxDate, maxResults, startIndex);        
    }



    /**
     * {@inheritDoc}
     */
    public long getRawLogsItemsNumber(Calendar minDate, Calendar maxDate,
                    Integer maxResults, Integer startIndex) {
        final Set<Long> queriesIds = this.getParentJob().getQueriesIds();

        return LogDaoHelper.getLogDao().getRawLogsItemsNumber(
                queriesIds.toArray(new Long[queriesIds.size()]), minDate, 
                maxDate, maxResults, startIndex);
    }
    
    /**
     * {@inheritDoc}
     */
    public long getAggregateHourLogsItemsNumber(Calendar minDate, Calendar maxDate,
                    Integer maxResults, Integer startIndex) {

        return LogDaoHelper.getLogDao().getAggregateHourLogsItemsNumber(
                ParentType.JOB, this.getParentJob().getJobId(), minDate, 
                maxDate, maxResults, startIndex);
    }
    
    /**
     * {@inheritDoc}
     */
    public List<Calendar> fetchMaxMinDateLog(){
    	final Set<Long> queriesIds = this.getParentJob().getQueriesIds();
    	
        return LogDaoHelper.getLogDao().getRawlogMaxMinDate(
                queriesIds.toArray(new Long[queriesIds.size()]));
    }
    
}
