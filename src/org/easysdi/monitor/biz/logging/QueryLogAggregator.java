package org.easysdi.monitor.biz.logging;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.deegree.framework.util.DateUtil;
import org.easysdi.monitor.biz.job.JobConfiguration;
import org.easysdi.monitor.biz.job.QueryConfiguration;
import org.easysdi.monitor.dat.dao.LogDaoHelper;

/**
 * Aggregates the raw log entries of a query into daily statistical summaries.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
public class QueryLogAggregator extends AbstractLogAggregator {

    private LogManager logManager;



    /**
     * Creates a new aggregator for query logs.
     * 
     * @param   queryLogManager  the object managing the log entries for the 
     *                           target query
     */
    public QueryLogAggregator(LogManager queryLogManager) {

        if (null == queryLogManager) {
            throw new IllegalArgumentException("Log manager can't be null");
        }

        this.setLogManager(queryLogManager);
    }



    /**
     * Sets the manager for the query's log entries.
     * 
     * @param   queryLogManager the log manager object
     */
    private void setLogManager(LogManager queryLogManager) {
        this.logManager = queryLogManager;
    }



    /**
     * Gets the manager for the query's log entries.
     * 
     * @return  the log manager
     */
    private LogManager getLogManager() {
        return this.logManager;
    }



    /**
     * Creates the aggregated logs entries for the query.
     */
    public void aggregateRawLogs() {
    	
    	List<Calendar> minmaxPeriod = this.getLogManager().getRawlogMinMax();
    
    	if(minmaxPeriod.size() > 1 && minmaxPeriod.get(0) != null && minmaxPeriod.get(1) != null )
    	{
    		Calendar dateRawLog = DateUtil.truncateTime(minmaxPeriod.get(1));
    		Calendar maxdate = DateUtil.setTime(minmaxPeriod.get(0),"23:59:59");
    		final Map<Date, AbstractAggregateLogEntry> allAggregateLogs 
            = this.getLogManager().getAggregateLogs();
    		final Calendar today = DateUtil.truncateTime(Calendar.getInstance());
    		while(maxdate.after(dateRawLog))
    		{
    			if (!allAggregateLogs.containsKey(dateRawLog.getTime())
   					 && 0 != DateUtil.compareWithoutTime(dateRawLog, today)) {
       			
    				final QueryLogFetcher logFetcher = (QueryLogFetcher) this.getLogManager().getLogFetcher();
    				final QueryConfiguration queryConfig = logFetcher.getParentQuery().getConfig(); 
    				final JobConfiguration parentJobConfig = queryConfig.getParentJob().getConfig();
    				final Calendar h24EndTime = DateUtil.setTime(dateRawLog, "23:59:59");
    				Calendar slaStart = DateUtil.mixDateTime(dateRawLog, parentJobConfig.getSlaStartTime());
    				Calendar slaEnd = DateUtil.mixDateTime(dateRawLog, parentJobConfig.getSlaEndTime());
    				Set<RawLogEntry> h24RawLogs;
    				Set<RawLogEntry> slaRawLogs;
    				h24RawLogs = logFetcher.fetchRawLogsSubset(dateRawLog,h24EndTime, null,null);
    				// Only create log when there is data
    				if(h24RawLogs.size() > 0)
    				{
    					slaRawLogs = logFetcher.fetchRawLogsSubset(slaStart, slaEnd,null, null);
    				
    					final AggregateStats h24Stats = this.calculateStats(h24RawLogs, dateRawLog, h24EndTime,this.getLogManager());
    					final AggregateStats slaStats = this.calculateStats(slaRawLogs, slaStart, slaEnd, this.getLogManager());
    					final QueryAggregateLogEntry aggregLog = new QueryAggregateLogEntry(logFetcher.getParentQuery(), 
    							dateRawLog, h24Stats, slaStats);
    				
    					LogDaoHelper.getLogDao().persistAggregLog(aggregLog);
    					allAggregateLogs.put(dateRawLog.getTime(), aggregLog);
    				}
    			}
    			dateRawLog.add(Calendar.DATE,1);
    		}
    	}
	
    	/*
    	final Set<RawLogEntry> allRawLogs = this.getLogManager().getRawLogs();
        final Set<Calendar> rawLogDates = this.getRawLogDates(allRawLogs);
        final Map<Date, AbstractAggregateLogEntry> allAggregateLogs 
            = this.getLogManager().getAggregateLogs();
        final Calendar today = DateUtil.truncateTime(Calendar.getInstance());

        for (Calendar dateRawLog : rawLogDates) {
            final QueryLogFetcher logFetcher 
                = (QueryLogFetcher) this.getLogManager().getLogFetcher();
            final QueryConfiguration queryConfig 
                = logFetcher.getParentQuery().getConfig(); 
            final JobConfiguration parentJobConfig 
                = queryConfig.getParentJob().getConfig();
            final Calendar h24EndTime = DateUtil.setTime(dateRawLog, 
                                                         "23:59:59");
            final Calendar slaStart 
                = DateUtil.mixDateTime(dateRawLog, 
                                       parentJobConfig.getSlaStartTime());
            final Calendar slaEnd 
                = DateUtil.mixDateTime(dateRawLog, 
                                       parentJobConfig.getSlaEndTime());
            Set<RawLogEntry> h24RawLogs;
            Set<RawLogEntry> slaRawLogs;

            if (!allAggregateLogs.containsKey(dateRawLog.getTime())
                && 0 != DateUtil.compareWithoutTime(dateRawLog, today)) {

                h24RawLogs = logFetcher.fetchRawLogsSubset(dateRawLog,
                                                           h24EndTime, null,
                                                           null);
                slaRawLogs = logFetcher.fetchRawLogsSubset(slaStart, slaEnd,
                                                           null, null);
                final AggregateStats h24Stats 
                    = this.calculateStats(h24RawLogs, dateRawLog, h24EndTime,
                                          this.getLogManager());
                final AggregateStats slaStats
                    = this.calculateStats(slaRawLogs, slaStart, slaEnd, 
                                          this.getLogManager());
                final QueryAggregateLogEntry aggregLog 
                    = new QueryAggregateLogEntry(logFetcher.getParentQuery(), 
                                                 dateRawLog, h24Stats, 
                                                 slaStats);
                LogDaoHelper.getLogDao().persistAggregLog(aggregLog);
                allAggregateLogs.put(dateRawLog.getTime(), aggregLog);
            }
        }
        */
    }
    
    /**
     * Creates the aggregated logs entries for the query.
     * Loops over every for between the rawlog start and end date
     */
    public void aggregateHourRawLogs()
    {
    	List<Calendar> minmaxPeriod = this.getLogManager().getRawlogMinMax();
    	if(minmaxPeriod.size() > 1 && minmaxPeriod.get(0) != null && minmaxPeriod.get(1) != null )
    	{
    		Calendar dateRawLog = DateUtil.truncateTime(minmaxPeriod.get(1));
    		Calendar maxdate = DateUtil.setTime(minmaxPeriod.get(0),"23:59:59");
    		final Map<Date, AbstractAggregateHourLogEntry> allAggregateHourLogs = this.getLogManager().getAggregateHourLogs();
            final Calendar today = DateUtil.truncateTime(Calendar.getInstance());
            while(maxdate.after(dateRawLog))
    		{
            	if (!allAggregateHourLogs.containsKey(dateRawLog.getTime()) && 
                		0 != DateUtil.compareWithoutTime(dateRawLog, today)) {
                
	            	final QueryLogFetcher logFetcher = (QueryLogFetcher) this.getLogManager().getLogFetcher();
	            	
	                Set<RawLogEntry> dailyRawLogs;
	                Set<RawLogEntry> inspireRawLogs;
	                Set<RawLogEntry> inspireHourRawLogs;
	                
	                // Time interval for each hour
	                Calendar timeStart = DateUtil.setTime(dateRawLog, "00:00:00");
	                Calendar timeEnd = DateUtil.setTime(dateRawLog, "23:59:59");
	                
	                dailyRawLogs = logFetcher.fetchRawLogsSubset(timeStart,timeEnd, null, null);
	                // Only create log when there is data
	                if(dailyRawLogs.size() > 0)
	            	{
		                // Inspire daily
		            	inspireRawLogs = this.inspireRawLog(dailyRawLogs, timeStart, timeEnd);
		            	
		            	// InspireRawLogs = 
		            	for(int i = 0;i<24;i++)
		            	{
		            		timeStart = DateUtil.setTime(dateRawLog, this.createHourTimeString(i,false));
		            		timeEnd = DateUtil.setTime(dateRawLog, this.createHourTimeString(i,true));
		            		// Fetch log data for hour
		            		dailyRawLogs = logFetcher.fetchRawLogsSubset(timeStart,timeEnd, null, null);
		            		
		            		// Method to get from set inspire
		            		inspireHourRawLogs = this.fetchSetRawLogsSubset(timeStart, timeEnd, inspireRawLogs);
		            		
		            		//  Only create states if any data
		            		if(dailyRawLogs.size() > 0)
			            	{   
			            	 		
		            			AggregateStats h1Stats = this.calculateHourStats(dailyRawLogs, timeStart, timeEnd,this.getLogManager());
		            			AggregateStats inspireStats = this.calculateHourStats(inspireHourRawLogs, timeStart, timeEnd,this.getLogManager());		             	    
		            	   
		            			// Create aggregLog
		            			QueryAggregateHourLogEntry aggregLog = new QueryAggregateHourLogEntry
		            			(logFetcher.getParentQuery(),timeStart, h1Stats,inspireStats);
		                 
		            			LogDaoHelper.getLogDao().persistAggregHourLog(aggregLog);
		            			allAggregateHourLogs.put(dateRawLog.getTime(), aggregLog);
			            	}
		            	}
	            	}
            	}
            	dateRawLog.add(Calendar.DATE,1);
    		}
    	}
    	/*
    	final Set<RawLogEntry> allRawLogs = this.getLogManager().getRawLogs();
    
        final Set<Calendar> rawLogsDates = this.getRawLogDates(allRawLogs);
        final Map<Date, AbstractAggregateHourLogEntry> allAggregateHourLogs = this.getLogManager().getAggregateHourLogs();
        final Calendar today = DateUtil.truncateTime(Calendar.getInstance());
        
        for (Calendar dateRawLog : rawLogsDates) {
        	// Check not today and if aggregated not created
            final QueryLogFetcher logFetcher = (QueryLogFetcher) this.getLogManager().getLogFetcher();
        	
            Set<RawLogEntry> dailyRawLogs;
            Set<RawLogEntry> inspireRawLogs;
            Set<RawLogEntry> inspireHourRawLogs;
            
            // Time interval for each hour
            Calendar timeStart = DateUtil.setTime(dateRawLog, "00:00:00");
            Calendar timeEnd = DateUtil.setTime(dateRawLog, "23:59:59");
            
            if (!allAggregateHourLogs.containsKey(dateRawLog.getTime()) && 
            		0 != DateUtil.compareWithoutTime(dateRawLog, today)) {
            	
            	dailyRawLogs = logFetcher.fetchRawLogsSubset(timeStart,timeEnd, null, null);
            	// Inspire daily
            	inspireRawLogs = this.inspireRawLog(dailyRawLogs, timeStart, timeEnd);
            	
            	// InspireRawLogs = 
            	for(int i = 0;i<24;i++)
            	{
            		timeStart = DateUtil.setTime(dateRawLog, this.createHourTimeString(i,false));
            		timeEnd = DateUtil.setTime(dateRawLog, this.createHourTimeString(i,true));
            		// Fetch log data for hour
            		dailyRawLogs = logFetcher.fetchRawLogsSubset(timeStart,timeEnd, null, null);
            		
            		// Create method to get from set inspire
            		inspireHourRawLogs = this.fetchSetRawLogsSubset(timeStart, timeEnd, inspireRawLogs);
            		
            		AggregateStats h1Stats = this.calculateHourStats(dailyRawLogs, timeStart, timeEnd,this.getLogManager());
            	    
            	    AggregateStats inspireStats = this.calculateHourStats(inspireHourRawLogs, timeStart, timeEnd,this.getLogManager());
             	    
            	    // Create aggregLog
            	    QueryAggregateHourLogEntry aggregLog = new QueryAggregateHourLogEntry
             	    (logFetcher.getParentQuery(),timeStart, h1Stats,inspireStats);
                 
             	    LogDaoHelper.getLogDao().persistAggregHourLog(aggregLog);
             	    allAggregateHourLogs.put(dateRawLog.getTime(), aggregLog);
            	}
            }	
        }*/
    }
}
