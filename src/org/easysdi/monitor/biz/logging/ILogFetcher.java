package org.easysdi.monitor.biz.logging;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Provides methods to read log entries (raw or aggregate).
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
public interface ILogFetcher {

    /**
     * The kind of log to fetch. 
     */
    enum LogType {
        RAW,
        AGGREGATE
    }


    /**
     * Gets all the raw log entries for the parent object.
     * 
     * @return  a set containing all the raw log entries
     */
    Set<RawLogEntry> fetchRawLogs();

    /**
     * Get the max and min raw log date for the parent object
     * @return
     */
    List<Calendar> fetchMaxMinDateLog();

    /**
     * Gets the raw log entries that match the given criteria.
     * <p>
     * Each parameter can be set to <code>null</code> if it must be ignored.
     * <p>
     * Logs entries are always sorted by date in descending order. This is 
     * important to keep in mind if you use the <code>maxResults</code> or 
     * <code>startIndex</code> parameter.  
     * 
     * @param   minDate         the date (included) starting from which the 
     *                          entries must be fetched
     * @param   maxDate         the date (included) up to which the entries must
     *                          be fetched
     * @param   maxResults      the maximum number of log entries to fetch
     * @param   startIndex      the 0-based index number of the first entry to 
     *                          fetch. For instance, if you set this parameter 
     *                          to 3, the 3 most recent log entries meeting
     *                          the other criteria will be ignored. This 
     *                          parameter is mainly used for paging  
     * @return                  a set containing the log entries meeting the 
     *                          criteria
     */
    Set<RawLogEntry> fetchRawLogsSubset(Calendar minDate, Calendar maxDate,
                    Integer maxResults, Integer startIndex);


    
    /**
     * Gets the number of raw log entries that match the given criteria.
     * <p>
     * Each parameter can be set to <code>null</code> if it must be ignored.
     * <p>
     * Logs entries are always sorted by date in descending order. This is 
     * important to keep in mind if you use the <code>maxResults</code> or 
     * <code>startIndex</code> parameter.  
     * 
     * @param   minDate         the date (included) starting from which the 
     *                          entries must be fetched
     * @param   maxDate         the date (included) up to which the entries must
     *                          be fetched
     * @param   maxResults      the maximum number of log entries to fetch
     * @param   startIndex      the 0-based index number of the first entry to 
     *                          fetch. For instance, if you set this parameter 
     *                          to 3, the 3 most recent log entries meeting
     *                          the other criteria will be ignored. This 
     *                          parameter is mainly used for paging  
     * @return                  a set containing the log entries meeting the 
     *                          criteria
     */
    long getRawLogsItemsNumber(Calendar minDate, Calendar maxDate,
                               Integer maxResults, Integer startIndex);


    
    /**
     * Gets the number of aggregate log entries that match the given criteria.
     * <p>
     * Each parameter can be set to <code>null</code> if it must be ignored.
     * <p>
     * Logs entries are always sorted by date in descending order. This is 
     * important to keep in mind if you use the <code>maxResults</code> or 
     * <code>startIndex</code> parameter.  
     * 
     * @param   minDate         the date (included) starting from which the 
     *                          entries must be fetched
     * @param   maxDate         the date (included) up to which the entries must
     *                          be fetched
     * @param   maxResults      the maximum number of log entries to fetch
     * @param   startIndex      the 0-based index number of the first entry to 
     *                          fetch. For instance, if you set this parameter 
     *                          to 3, the 3 most recent log entries meeting
     *                          the other criteria will be ignored. This 
     *                          parameter is mainly used for paging  
     * @return                  a set containing the log entries meeting the 
     *                          criteria
     */
    long getAggregateLogsItemsNumber(Calendar minDate, Calendar maxDate,
                                     Integer maxResults, Integer startIndex);


    /**
     * Gets all the aggregate log entries for the parent object.
     * 
     * @return  a map with the entry date as the key and the entry as the value
     */
    Map<Date, AbstractAggregateLogEntry> fetchAggregLogs();

    /**
     * Gets all the aggregate log entries for the parent object.
     * 
     * @return  a map with the entry date with time as the key and the entry as the value
     */
    Map<Date, AbstractAggregateHourLogEntry> fetchAggregHourLogs();


    /**
     * Gets the aggregate log entries that match the given criteria.
     * <p>
     * Each parameter can be set to <code>null</code> if it must be ignored.
     * <p>
     * Logs are always sorted by date in descending order. This is important to 
     * keep in mind if you use the <code>maxResults</code> or 
     * <code>startIndex</code> parameter.  
     * 
     * @param   minDate         the date (included) starting from which the logs
     *                          must be fetched
     * @param   maxDate         the date (included) up to which the logs must
     *                          be fetched
     * @param   maxResults      the maximum number of log entries to fetch
     * @param   startIndex      the 0-based index number of the first entry to 
     *                          fetch. For instance, if you set this parameter 
     *                          to 3, the 3 most recent log entries meeting
     *                          the other criteria will be ignored. This 
     *                          parameter is mainly used for paging  
     * @return                  a map containing the entries that meet the 
     *                          criteria, with the entry date as the key and 
     *                          the entry as the value
     */
    Map<Date, AbstractAggregateLogEntry> fetchAggregLogsSubset(Calendar minDate,
                    Calendar maxDate, Integer maxResults, Integer startIndex);

    
    /**
     * Gets the aggregate log entries that match the given criteria.
     * <p>
     * Each parameter can be set to <code>null</code> if it must be ignored.
     * <p>
     * Logs are always sorted by date in descending order. This is important to 
     * keep in mind if you use the <code>maxResults</code> or 
     * <code>startIndex</code> parameter.  
     * 
     * @param   minDate         the date (included) starting from which the logs
     *                          must be fetched
     * @param   maxDate         the date (included) up to which the logs must
     *                          be fetched
     * @param   maxResults      the maximum number of log entries to fetch
     * @param   startIndex      the 0-based index number of the first entry to 
     *                          fetch. For instance, if you set this parameter 
     *                          to 3, the 3 most recent log entries meeting
     *                          the other criteria will be ignored. This 
     *                          parameter is mainly used for paging  
     * @return                  a map containing the entries that meet the 
     *                          criteria, with the entry date as the key and 
     *                          the entry as the value
     */
    Map<Date, AbstractAggregateHourLogEntry> fetchAggregHourLogsSubset(Calendar minDate,
                    Calendar maxDate, Integer maxResults, Integer startIndex);
    

    /**
     * Gets the last raw log entry before the given date.
     * 
     * @param   date    the date
     * @return          the last log entry before the date if there is one, or
     *                  <br>
     *                  <code>null</code> otherwise
     */
    RawLogEntry fetchLastLogBeforeDate(Calendar date);
    
    /**
     * Gets the number of aggregate hour log entries that match the given criteria.
     * <p>
     * Each parameter can be set to <code>null</code> if it must be ignored.
     * <p>
     * Logs entries are always sorted by date in descending order. This is 
     * important to keep in mind if you use the <code>maxResults</code> or 
     * <code>startIndex</code> parameter.  
     * 
     * @param   minDate         the date (included) starting from which the 
     *                          entries must be fetched
     * @param   maxDate         the date (included) up to which the entries must
     *                          be fetched
     * @param   maxResults      the maximum number of log entries to fetch
     * @param   startIndex      the 0-based index number of the first entry to 
     *                          fetch. For instance, if you set this parameter 
     *                          to 3, the 3 most recent log entries meeting
     *                          the other criteria will be ignored. This 
     *                          parameter is mainly used for paging  
     * @return                  a set containing the log entries meeting the 
     *                          criteria
     */
    long getAggregateHourLogsItemsNumber(Calendar minDate, Calendar maxDate,
            Integer maxResults, Integer startIndex);
    
}
