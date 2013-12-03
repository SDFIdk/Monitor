package org.easysdi.monitor.biz.logging;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.deegree.framework.util.DateUtil;
import org.easysdi.monitor.biz.job.Job;
import org.easysdi.monitor.biz.job.Query;

/**
 * Executes operations on the logs for a job or a query.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
public class LogManager {

    private Map<Date, AbstractAggregateLogEntry> aggregateLogs;
    private Map<Date, AbstractAggregateHourLogEntry> aggregateHourLogs;
    private ILogAggregator                       logAggregator;
    private ILogFetcher                          logFetcher;
    private Set<RawLogEntry>                     rawLogs;


    
    /**
     * Creates a log manager for a job.
     * 
     * @param   parentJob   the job
     */
    public LogManager(Job parentJob) {
        this.setLogAggregator(new JobLogAggregator(this));
        this.setLogFetcher(new JobLogFetcher(parentJob));
    }

    /**
     * Creates a log manager for a query.
     * 
     * @param   parentQuery the query
     */
    public LogManager(Query parentQuery) {
        this.setLogAggregator(new QueryLogAggregator(this));
        this.setLogFetcher(new QueryLogFetcher(parentQuery));
    }

    /**
     * Defines the object in charge of aggregating the logs.
     * 
     * @param   newLogAggregator    the log aggregator
     */
    private void setLogAggregator(ILogAggregator newLogAggregator) {
        this.logAggregator = newLogAggregator;
    }

    /**
     * Gets the object in charge of aggregating the logs.
     * 
     * @return  the log aggregator
     */
    public ILogAggregator getLogAggregator() {
        return this.logAggregator;
    }



    /**
     * Defines the object in charge of fetching the logs.
     * 
     * @param   newLogFetcher   the log fetcher
     */
    private void setLogFetcher(ILogFetcher newLogFetcher) {
        this.logFetcher = newLogFetcher;
    }



    /**
     * Gets the object in charge of fetching the logs.
     * 
     * @return  the log fetcher
     */
    public ILogFetcher getLogFetcher() {
        return this.logFetcher;
    }



    /**
     * Defines the set of raw logs for the job or the query.
     * <p>
     * <i><b>Note:</b> This method is intended for internal use and shouldn't be
     * called directly.</i>
     * 
     * @param   rawLogsSet  the set of raw logs for the target object
     */
    private void setRawLogs(Set<RawLogEntry> rawLogsSet) {
        this.rawLogs = rawLogsSet;
    }


    /**
     * Gets all the raw logs for the job or the query.
     * 
     * @return  a set containing the raw logs
     */
    public Set<RawLogEntry> getRawLogs() {

        if (null == this.rawLogs) {
            this.setRawLogs(this.getLogFetcher().fetchRawLogs());
        }

        return Collections.unmodifiableSet(this.rawLogs);
    }
    
    public List<Calendar> getRawlogMinMax()
    {
    	return this.getLogFetcher().fetchMaxMinDateLog();
    }



    /**
     * Gets some of the raw logs based on criteria.
     * <p>
     * Each parameter can be <code>null</code> if it must be ignored.
     * 
     * @param   minDate     the date from which the raw logs are fetched
     * @param   maxDate     the date up to which the raw logs are fetched
     * @param   maxResults  the maximum number of raw log entry to be fetched
     * @param   startIndex  the index of the first entry to fetched inside the
     *                      set defined by the other criteria. This is useful
     *                      for paging purposes
     * @return              the set of raw log entries matching the criteria
     */
    public Set<RawLogEntry> getRawLogsSubset(Calendar minDate,
                    Calendar maxDate, Integer maxResults, Integer startIndex) {

        Calendar minDateWithTime = null;
        Calendar maxDateWithTime = null;
        
        if (null != minDate) {
            minDateWithTime = DateUtil.truncateTime(minDate);
        }
        
        if (null != maxDate) {
            maxDateWithTime = DateUtil.setTime(maxDate, "23:59:59");
        }

        return this.getLogFetcher().fetchRawLogsSubset(
               minDateWithTime, maxDateWithTime, maxResults, startIndex);
    }



    /**
     * Set the set of aggregate logs for the job or the query.
     * <p>
     * <i><b>Note:</b> This method is intended for internal purposes and 
     * shouldn't be called directly.</i>
     * 
     * @param   aggregLogsSet   the set containing the aggregate log entries
     */
    private void setAggregateLogs(Map<Date, AbstractAggregateLogEntry> aggregLogsSet) {   
        this.aggregateLogs = aggregLogsSet;
    }
    
    /**
     * Set the set of aggregate logs for the job or the query.
     * <p>
     * <i><b>Note:</b> This method is intended for internal purposes and 
     * shouldn't be called directly.</i>
     * 
     * @param   aggregLogsSet   the set containing the aggregate log entries
     */
    private void setAggregateHourLogs(Map<Date, AbstractAggregateHourLogEntry> aggregHourLogsSet) {   
        this.aggregateHourLogs = aggregHourLogsSet;
    }



    /**
     * Gets all the aggregate logs for the job or the query.
     * 
     * @return  a set containing the aggregate log entries
     */
    public Map<Date, AbstractAggregateLogEntry> getAggregateLogs() {

        if (null == this.aggregateLogs) {
            this.setAggregateLogs(this.getLogFetcher().fetchAggregLogs());
        }

        return this.aggregateLogs;
    }
    
    /**
     * Gets all the aggregate logs for the job or the query.
     * 
     * @return  a set containing the aggregate log entries
     */
    public Map<Date, AbstractAggregateHourLogEntry> getAggregateHourLogs() {

        if (null == this.aggregateHourLogs) {
            this.setAggregateHourLogs(this.getLogFetcher().fetchAggregHourLogs());
        }

        return this.aggregateHourLogs;
    }


    /**
     * Gets some of the aggregate log entries based on criteria.
     * <p>
     * Each parameter can be <code>null</code> if it must be ignored.
     * 
     * @param   minDate     the date from which the aggregate logs are fetched
     * @param   maxDate     the date up to which the aggregate logs are fetched
     * @param   maxResults  the maximum number of aggregate log entry to be 
     *                      fetched
     * @param   startIndex  the index of the first entry to fetched inside the
     *                      set defined by the other criteria. This is useful
     *                      for paging purposes
     * @return              the set of aggregate log entries matching the 
     *                      given criteria
     */
    public Map<Date, AbstractAggregateLogEntry> getAggregLogsSubset(Calendar minDate,
                    Calendar maxDate, Integer maxResults, Integer startIndex) {
        
        Calendar minDateWithTime = null;
        Calendar maxDateWithTime = null;
        
        if (null != minDate) {
            minDateWithTime = DateUtil.truncateTime(minDate);
        }
        
        if (null != maxDate) {
            maxDateWithTime = DateUtil.setTime(maxDate, "23:59:59");
        }

        return this.getLogFetcher().fetchAggregLogsSubset(
                  minDateWithTime, maxDateWithTime, maxResults, startIndex);
    }
    
    /**
     * Gets some of the aggregate hour log entries based on criteria.
     * <p>
     * Each parameter can be <code>null</code> if it must be ignored.
     * 
     * @param   minDate     the date from which the aggregate hour logs are fetched
     * @param   maxDate     the date up to which the aggregate hour logs are fetched
     * @param   maxResults  the maximum number of aggregate hour log entry to be 
     *                      fetched
     * @param   startIndex  the index of the first entry to fetched inside the
     *                      set defined by the other criteria. This is useful
     *                      for paging purposes
     * @return              the set of aggregate hour log entries matching the 
     *                      given criteria
     */
    public Map<Date, AbstractAggregateHourLogEntry> getAggregHourLogsSubset(Calendar minDate,
     Calendar maxDate, Integer maxResults, Integer startIndex)
    {
    	Calendar minDateWithTime = null;
        Calendar maxDateWithTime = null;
        
        if (null != minDate) {
            minDateWithTime = DateUtil.truncateTime(minDate);
        }
        
        if (null != maxDate) {
            maxDateWithTime = DateUtil.setTime(maxDate, "23:59:59");
        }

        return this.getLogFetcher().fetchAggregHourLogsSubset(
                  minDateWithTime, maxDateWithTime, maxResults, startIndex);
    }

    /**
     * Deletes the aggregate logs for the job or the query.
     * 
     * @param   limit   the date (included) up to which the aggregate log 
     *                  entries must be deleted
     */
    public void purgeAggregateLogs(Calendar limit) {
        final Calendar limitDate = DateUtil.truncateTime(limit);
        final Calendar maxDate = (Calendar) limitDate.clone(); 
        maxDate.add(Calendar.DAY_OF_MONTH, 1);

        final Map<Date, AbstractAggregateLogEntry> aggregLogsToDelete 
            = this.getAggregLogsSubset(null, maxDate, null, null);

        for (Date aggregLogDate : aggregLogsToDelete.keySet()) {
            final Calendar logDate 
                = DateUtil.truncateTime(DateUtil.dateToCalendar(aggregLogDate));

            if (0 >= logDate.compareTo(limitDate)) {
                aggregLogsToDelete.get(aggregLogDate).delete();
            }
        }

        this.setAggregateLogs(this.getLogFetcher().fetchAggregLogs());
    }
    

    /**
     * Deletes the raw log entries for the job or the query.
     * 
     * @param   limit   the date (included) up to which the raw log entries
     *                  must be deleted
     */
    public void purgeRawLogs(Calendar limit) {
        final Calendar limitDate = DateUtil.truncateTime(limit);

        for (RawLogEntry logEntry 
                        : this.getRawLogsSubset(null, limitDate, null, null)) {
            
            final Calendar logDate 
                = DateUtil.truncateTime(logEntry.getRequestTime());

            if (0 >= logDate.compareTo(limitDate)) {
                logEntry.delete();
            }
        }

        this.setRawLogs(this.getLogFetcher().fetchRawLogs());
    }

    /**
     * Gets the number of items in a defined raw logs subset.
     * 
     * @param   minDate     the date from which the raw logs are fetched
     * @param   maxDate     the date up to which the raw logs are fetched
     * @param   maxResults  the maximum number of raw log entry to be fetched
     * @param   startIndex  the index of the first entry to fetched inside the
     *                      set defined by the other criteria. This is useful
     *                      for paging purposes
     * @return              the number of raw logs items matching the parameters
     */
    public Long getRawLogsItemsNumber(Calendar minDate, Calendar maxDate, 
                                      Integer maxResults, Integer startIndex) {

        Calendar minDateWithTime = null;
        Calendar maxDateWithTime = null;
        
        if (null != minDate) {
            minDateWithTime = DateUtil.truncateTime(minDate);
        }
        
        if (null != maxDate) {
            maxDateWithTime = DateUtil.setTime(maxDate, "23:59:59");
        }

        return this.getLogFetcher().getRawLogsItemsNumber(
               minDateWithTime, maxDateWithTime, maxResults, startIndex);
    }

    /**
     * Gets the number of items in a defined raw logs subset.
     * 
     * @param   minDate     the date from which the raw logs are fetched
     * @param   maxDate     the date up to which the raw logs are fetched
     * @param   maxResults  the maximum number of raw log entry to be fetched
     * @param   startIndex  the index of the first entry to fetched inside the
     *                      set defined by the other criteria. This is useful
     *                      for paging purposes
     * @return              the number of raw logs items matching the parameters
     */
    public Long getAggregateLogsItemsNumber(Calendar minDate, 
            Calendar maxDate, Integer maxResults, Integer startIndex) {

        Calendar minDateWithTime = null;
        Calendar maxDateWithTime = null;
        
        if (null != minDate) {
            minDateWithTime = DateUtil.truncateTime(minDate);
        }
        
        if (null != maxDate) {
            maxDateWithTime = DateUtil.setTime(maxDate, "23:59:59");
        }

        return this.getLogFetcher().getAggregateLogsItemsNumber(
               minDateWithTime, maxDateWithTime, null, null);
    }
    
    /**
     * Gets the number of items in a defined hour logs subset.
     * 
     * @param   minDate     the date from which the hour logs are fetched
     * @param   maxDate     the date up to which the hour logs are fetched
     * @param   maxResults  the maximum number of hour log entry to be fetched
     * @param   startIndex  the index of the first entry to fetched inside the
     *                      set defined by the other criteria. This is useful
     *                      for paging purposes
     * @return              the number of hour logs items matching the parameters
     */
    public Long getAggregateHourLogsItemsNumber(Calendar minDate, 
            Calendar maxDate, Integer maxResults, Integer startIndex) {

        Calendar minDateWithTime = null;
        Calendar maxDateWithTime = null;
        
        if (null != minDate) {
            minDateWithTime = DateUtil.truncateTime(minDate);
        }
        
        if (null != maxDate) {
            maxDateWithTime = DateUtil.setTime(maxDate, "23:59:59");
        }

        return this.getLogFetcher().getAggregateHourLogsItemsNumber(
               minDateWithTime, maxDateWithTime, null, null);
    }
    
    /**
     * Deletes the aggregate hour logs for the job or the query.
     * 
     * @param   limit   the date (included) up to which the aggregate hour log 
     *                  entries must be deleted
     */
    public void purgeAggregateHourLogs(Calendar limit) {
        final Calendar limitDate = DateUtil.truncateTime(limit);
        final Calendar maxDate = (Calendar) limitDate.clone(); 
        maxDate.add(Calendar.DAY_OF_MONTH, 1);

        final Map<Date, AbstractAggregateHourLogEntry> aggregHourLogsToDelete 
            = this.getAggregHourLogsSubset(null, maxDate, null, null);

        for (Date aggregHourLogDate : aggregHourLogsToDelete.keySet()) {
            final Calendar logDate 
                = DateUtil.truncateTime(DateUtil.dateToCalendar(aggregHourLogDate));

            if (0 >= logDate.compareTo(limitDate)) {
                aggregHourLogsToDelete.get(aggregHourLogDate).delete();
            }
        }

        this.setAggregateLogs(this.getLogFetcher().fetchAggregLogs());
    }
    
}
