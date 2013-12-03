package org.easysdi.monitor.biz.logging;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.deegree.framework.util.DateUtil;
import org.easysdi.monitor.biz.job.Status.StatusValue;

/**
 * Defines generic log aggregation (that is, statistics calculation on a given 
 * period).
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
public abstract class AbstractLogAggregator implements ILogAggregator {

    /**
     * The number of milliseconds in an hour.
     */
    private static final int MILLISECONDS_IN_HOUR = 3600000;
        
    /**
     * The total percentage.
     */
    private static final int TOTAL_PERCENT = 100;

    /**
     * Gets the dates for which a log entry exists in the given set.
     * 
     * @param rawLogs   the set containing the raw log entries to process
     * @return          a set containing the log dates
     */
    protected final Set<Calendar> getRawLogDates(Set<RawLogEntry> rawLogs) {
        final Set<Calendar> rawLogDates = new LinkedHashSet<Calendar>();

        if (null == rawLogs) {
            return null;
        }

        for (RawLogEntry entry : rawLogs) {
            final Calendar entryDate = 
                    DateUtil.truncateTime(entry.getRequestTime());

            if (!rawLogDates.contains(entryDate)) {
                rawLogDates.add(entryDate);
            }
        }

        return rawLogDates;
    }

    /**
     * Calculates statistics for a given set of log entries.
     * 
     * @param logs                  the set containing the logs to aggregate
     * @param availStart            the time when the availability period 
     *                              starts. (The date part is ignored)
     * @param availEnd              the time when the availability period ends. 
     *                              (The date parts is ignored)
     * @param logManager            the log manager for the object whose logs 
     *                              are processed.
     * @return                      the aggregate stats for the passed log 
     *                              entries 
     */
    protected final AggregateStats calculateStats(Set<RawLogEntry> logs,
            Calendar availStart, Calendar availEnd,
            LogManager logManager) {

        float totalRespTime = 0;
        float availHours = 0;
        float unavailLogs = 0;
        float failureLogs = 0;
        float untestedLogs = 0;
        final float sizeLog = logs.size();
        int nbBizErrors = 0;
        int nbConnErrors = 0;
        int nbSuccessQueries = 0;
        float maxRespTime = 0;
        float minRespTime = -1;
        Calendar spanEnd = availEnd;
        final float totalSpanHours = this.computeSpanHours(availStart, 
                                                           availEnd);
       
        for (RawLogEntry logEntry : logs) {
            final float respDelay = logEntry.getResponseDelay();
            final Calendar requestTime = logEntry.getRequestTime();
       
            if (0 < respDelay) {
                totalRespTime += respDelay;
                ++nbSuccessQueries;
            }
            
            if(0 < respDelay && respDelay > maxRespTime)
            {
            	maxRespTime = respDelay;
            }
            
            if(0 < respDelay && (respDelay < minRespTime || minRespTime == -1))
            {
        		minRespTime = respDelay;
            }
            
            if (logEntry.isBusinessError()) {
                ++nbBizErrors;
            } else if (logEntry.isConnectError()) {
                ++nbConnErrors;
            }

            if (StatusValue.AVAILABLE == logEntry.getStatusValue()) {
                //availHours += this.computeSpanHours(requestTime, spanEnd);
            	availHours += 1;
            }
            if(StatusValue.UNAVAILABLE == logEntry.getStatusValue())
            {
            	unavailLogs += 1;
            }
            if(StatusValue.OUT_OF_ORDER == logEntry.getStatusValue())
            {
            	failureLogs += 1;
            }
            if(StatusValue.NOT_TESTED == logEntry.getStatusValue())
            {
            	untestedLogs += 1;
            }
            spanEnd = (Calendar) requestTime.clone();
        }
        
        availHours += this.getSpanStartAvailHours(availStart, spanEnd, 
                                                  logManager);
       
        return this.buildStatsObject(nbSuccessQueries, totalRespTime, 
                                     availHours, totalSpanHours, nbBizErrors, 
                                     nbConnErrors,minRespTime,maxRespTime,sizeLog,unavailLogs,failureLogs,untestedLogs
                                     );
    }
    
    /**
     * Calculates statistics for a given set of log entries.
     * 
     * @param logs                  the set containing the logs to aggregate
     * @param availStart            the time when the availability period 
     *                              starts. (The date part is ignored)
     * @param availEnd              the time when the availability period ends. 
     *                              (The date parts is ignored)
     * @param logManager            the log manager for the object whose logs 
     *                              are processed.
     * @return                      the aggregate stats for the passed log 
     *                              entries 
     */
    protected final AggregateStats calculateHourStats(Set<RawLogEntry> logs,
            Calendar availStart, Calendar availEnd,
            LogManager logManager) {

        float totalRespTime = 0;
        float availLogs = 0;
        float unavailLogs = 0;
        float failureLogs = 0;
        float untestedLogs = 0;
        final float sizeLog = logs.size();
        int nbBizErrors = 0;
        int nbConnErrors = 0;
        int nbSuccessQueries = 0;
        float maxRespTime = 0;
        float minRespTime = -1;
        
        for (RawLogEntry logEntry : logs) {
            final float respDelay = logEntry.getResponseDelay();

            if (0 < respDelay) {
                totalRespTime += respDelay;
                ++nbSuccessQueries;
            }
            
            if(0 < respDelay && respDelay > maxRespTime)
            {
            	maxRespTime = respDelay;
            }
            
            if(0 < respDelay && (respDelay < minRespTime || minRespTime == -1))
            {
        		minRespTime = respDelay;
            }

            if (logEntry.isBusinessError()) {
                ++nbBizErrors;
            } else if (logEntry.isConnectError()) {
                ++nbConnErrors;
            }
            
            if (StatusValue.AVAILABLE == logEntry.getStatusValue()) {
                availLogs += 1;
            }
            
            if(StatusValue.UNAVAILABLE == logEntry.getStatusValue())
            {
            	unavailLogs += 1;
            }
            
            if(StatusValue.OUT_OF_ORDER == logEntry.getStatusValue())
            {
            	failureLogs += 1;
            }
            
            if(StatusValue.NOT_TESTED == logEntry.getStatusValue())
            {
            	untestedLogs +=1;
            }
            
            
        }
        return this.buildHourStatsObject(nbSuccessQueries, totalRespTime, availLogs, sizeLog, nbBizErrors, nbConnErrors,minRespTime,maxRespTime,
        		unavailLogs,failureLogs,untestedLogs);
    }

    /**
     * Gets how long the service was available during the span between the start
     * of the aggregation span and the first log entry.
     * 
     * @param   spanStart       the start of the aggregation span
     * @param   firstLogTime    the request date and time for the first log 
     *                          entry in the span 
     * @param   logManager      a log manager allowing to fetch the parent 
     *                          object's last log entry before the aggregation 
     *                          span 
     * @return                  the number of hours during which the service was
     *                          was available between the span start and the 
     *                          first log entry
     */
    private float getSpanStartAvailHours(Calendar spanStart, 
                                         Calendar firstLogTime, 
                                         LogManager logManager) {
        
        final ILogFetcher fetcher = logManager.getLogFetcher(); 
        final RawLogEntry lastLog = fetcher.fetchLastLogBeforeDate(spanStart); 

        if (null != lastLog
            && StatusValue.AVAILABLE == lastLog.getStatusValue()) {
            
            return this.computeSpanHours(spanStart, firstLogTime);
        }
        
        return 0;
    }
    
    /**
     * Generates an object holding aggregate hour stats.
     * 
     * @param   nbSuccessQueries    the number of times a query succeeded 
     * @param   totalRespTime       the sum of the successful queries' response
     *                              time
     * @param   availHours          the number of hours during which the web
     *                              service was available
     * @param   totalSpanHours      the total number of hours in the stats 
     *                              aggregation span
     * @param   nbBizErrors         the number of business errors in the span
     * @param   nbConnErrors        the number of connection errors in the span
     * @return                      the aggregate stats object
     */
    private AggregateStats buildHourStatsObject(int nbSuccessQueries, 
            float totalRespTime, float availLogs, float logSize,
            int nbBizErrors, int nbConnErrors, float minRespTime, float maxRespTime,
            float unavailLogs, float failureLogs, float untestedLogs) {
        
        final AggregateStats stats = new AggregateStats();
        if (0 < nbSuccessQueries) {
            stats.setMeanRespTime(totalRespTime / nbSuccessQueries);
        } else {
            stats.setMeanRespTime(0F);
        }
        float availRatio = 0F;
        float unavailRation = 0F;
        float failureRation = 0F;
        float untestedRation = 0F;
        if(logSize > 0)
        {
        	availRatio = availLogs / logSize;
        	unavailRation = unavailLogs / logSize;
        	failureRation = failureLogs / logSize;
        	unavailRation = unavailLogs / logSize; 
        }
        stats.setAvailability(availRatio * AbstractLogAggregator.TOTAL_PERCENT);
        stats.setUnavailability(unavailRation * AbstractLogAggregator.TOTAL_PERCENT);
        stats.setFailure(failureRation * AbstractLogAggregator.TOTAL_PERCENT);
        stats.setUntested(untestedRation * AbstractLogAggregator.TOTAL_PERCENT); 
        
        stats.setNbBizErrors(nbBizErrors);
        stats.setNbConnErrors(nbConnErrors);
        if(0 < maxRespTime)
        {
        	stats.setMaxRespTime(maxRespTime);
        }else
        {
        	stats.setMaxRespTime(0F);
        }
        
        if(0 < minRespTime)
        {
        	stats.setMinRespTime(minRespTime);
        	
        }else
        {
        	stats.setMinRespTime(0F);
        }
        
        return stats;
    }

    /**
     * Generates an object holding aggregate stats.
     * 
     * @param   nbSuccessQueries    the number of times a query succeeded 
     * @param   totalRespTime       the sum of the successful queries' response
     *                              time
     * @param   availHours          the number of hours during which the web
     *                              service was available
     * @param   totalSpanHours      the total number of hours in the stats 
     *                              aggregation span
     * @param   nbBizErrors         the number of business errors in the span
     * @param   nbConnErrors        the number of connection errors in the span
     * @return                      the aggregate stats object
     */
    private AggregateStats buildStatsObject(int nbSuccessQueries, 
            float totalRespTime, float availLogs, float totalSpanHours,
            int nbBizErrors, int nbConnErrors, float minRespTime, float maxRespTime,float logSize,
            float unavailLogs, float failureLogs, float untestedLogs) {
        
        final AggregateStats stats = new AggregateStats();
        
        if (0 < nbSuccessQueries) {
            stats.setMeanRespTime(totalRespTime / nbSuccessQueries);
        } else {
            stats.setMeanRespTime(0F);
        }
        
        //final float availRatio = availHours / totalSpanHours;
        float availRatio = 0F;
        float unavailRation = 0F;
        float failureRation = 0F;
        float untestedRation = 0F;
        if(logSize > 0)
        {
        	availRatio = availLogs / logSize;
        	unavailRation = unavailLogs / logSize;
        	failureRation = failureLogs / logSize;
        	unavailRation = unavailLogs / logSize; 
        }
        stats.setAvailability(availRatio * AbstractLogAggregator.TOTAL_PERCENT);
        stats.setUnavailability(unavailRation * AbstractLogAggregator.TOTAL_PERCENT);
        stats.setFailure(failureRation * AbstractLogAggregator.TOTAL_PERCENT);
        stats.setUntested(untestedRation * AbstractLogAggregator.TOTAL_PERCENT); 
        
        stats.setNbBizErrors(nbBizErrors);
        stats.setNbConnErrors(nbConnErrors);
        if(0 < maxRespTime)
        {
        	stats.setMaxRespTime(maxRespTime);
        }else
        {
        	stats.setMaxRespTime(0F);
        }
        
        if(0 < minRespTime)
        {
        	stats.setMinRespTime(minRespTime);
        	
        }else
        {
        	stats.setMinRespTime(0F);
        }
        	
        return stats;
    }
  
    /**
     * Calculates how many hours separates two dates. 
     * 
     * @param spanStart the start of the period
     * @param spanEnd   the end of the period
     * @return          the number of hours between the start and the end of
     *                  the period
     */
    private float computeSpanHours(Calendar spanStart, 
                                           Calendar spanEnd) {
        
        final long spanEndMillis = spanEnd.getTimeInMillis(); 
        final long spanStartMillis = spanStart.getTimeInMillis();
        final long differenceMillis = spanEndMillis - spanStartMillis;    
        return differenceMillis / AbstractLogAggregator.MILLISECONDS_IN_HOUR;
    }
    
    /**
     * Finds and remove the 10% worst delivery time record
     * 
     * @param logs
     * @param availStart (Not used)/
     * @param availEnd (Not used)
     * @return set with the 90% best result of the given set 
     */
    protected final Set<RawLogEntry> inspireRawLog(Set<RawLogEntry> logs, Calendar availStart, Calendar availEnd)
    {
    	final int procent = 10;      
    	int countToRemove = (logs.size() * procent) / 100;
    	
    	List<RawLogEntry> sortLogs = new ArrayList<RawLogEntry>(logs);
		// Sort after response time the lowest first
    	Collections.sort(sortLogs);
		
    	// Find elements to remove
		while(countToRemove > 0)
    	{
			int index = sortLogs.size()-1;
			if(index > -1)
			{
				countToRemove = 0;
			}
    		sortLogs.remove(index);
    		countToRemove -= 1;
    	}
    	Set<RawLogEntry> sortedLog =  new LinkedHashSet<RawLogEntry> (sortLogs);
    	return sortedLog;
    }
    
    /**
     * Create a hourly start/end time string 
     * @param hour
     * @param endHour
     * @return time string in format "HH:mm.ss"
     */
    protected final String createHourTimeString(int hour,boolean endHour)
    {
    	String time = "";    	
    	if(endHour)
    	{	
    		if(hour > 9)
    		{
    			time = ""+hour +":59:59"; 
    		}else
    		{
    			time = "0"+hour +":59:59";
    		}
    	}else
    	{
    		if(hour > 9)
    		{
    			time = ""+hour +":00:00";
    		}else
    		{
    			time = "0"+hour +":00:00"; 
    		}
    	}
    	return time;		  
    }
    
    /**
     * Gets a subset of a rawlogentry set 
     * @param minDate
     * @param maxDate
     * @param rawLog
     * @return filtered rawlogentry set
     */
    public Set<RawLogEntry> fetchSetRawLogsSubset(Calendar minDate,
                Calendar maxDate, Set<RawLogEntry> rawLog) {
    	
    	Set<RawLogEntry> logEntry = new LinkedHashSet<RawLogEntry>();
    	
    	for(RawLogEntry log : rawLog)
    	{
    		if((log.getRequestTime().after(minDate) && log.getRequestTime().before(maxDate)) || 
    		(log.getRequestTime().equals(minDate) || log.getRequestTime().equals(maxDate)))
    		{
    			logEntry.add(log);
    		}
    	}
    	return logEntry;
    }
    
}
