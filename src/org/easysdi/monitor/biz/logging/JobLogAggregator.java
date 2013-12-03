package org.easysdi.monitor.biz.logging;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;


import org.deegree.framework.util.DateUtil;
import org.easysdi.monitor.biz.job.JobConfiguration;
import org.easysdi.monitor.dat.dao.LogDaoHelper;

/**
 * Aggregates raw log entries of a job into daily statistical summaries.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
public class JobLogAggregator extends AbstractLogAggregator {

    private LogManager logManager;



    /**
     * Creates a new log aggregator.
     * 
     * @param   jobLogManager   the object used to manage the logs for the job
     */
    public JobLogAggregator(LogManager jobLogManager) {

        if (null == jobLogManager) {
            throw new IllegalArgumentException("Log manager can't be null");
        }

        this.setLogManager(jobLogManager);
    }



    /**
     * Defines the object used to manage the log entries.
     * 
     * @param   newLogManager   the object that provides an access to the logs
     */
    private void setLogManager(LogManager newLogManager) {
        this.logManager = newLogManager;
    }



    /**
     * Gets the object used to manage the log entries.
     * 
     * @return  the object that provides an access to the logs
     */
    private LogManager getLogManager() {
        return this.logManager;
    }


    
    /**
     *  Aggregates the raw logs for the job.
     *  <p>
     *  The raw logs are aggregated into daily summaries. Dates for which an
     *  aggregate log entry already exists are skipped. (That is, existing
     *  aggregate log entries are not overwritten.)
     */
    public void aggregateRawLogs() {
        
    	List<Calendar> minmaxPeriod = this.getLogManager().getRawlogMinMax();
    	if(minmaxPeriod.size() > 1 && minmaxPeriod.get(0) != null && minmaxPeriod.get(1) != null)
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
    				 
    				 final JobLogFetcher logFetcher = (JobLogFetcher) this.getLogManager().getLogFetcher();
    				 final JobConfiguration parentJobConfig = logFetcher.getParentJob().getConfig();
    				 final Calendar slaStart = DateUtil.mixDateTime(dateRawLog, parentJobConfig.getSlaStartTime());
    				 final Calendar h24EndTime = DateUtil.setTime(dateRawLog,"23:59:59");
    				 final Calendar slaEnd = DateUtil.mixDateTime(dateRawLog, parentJobConfig.getSlaEndTime());
    				 Set<RawLogEntry> h24RawLogs;
    				 Set<RawLogEntry> slaRawLogs;

    				 h24RawLogs = logFetcher.fetchRawLogsSubset(dateRawLog, h24EndTime, null, null);
    				 // Only create log when there is data
    				 if(h24RawLogs.size() > 0)
    				 {
    					 slaRawLogs = logFetcher.fetchRawLogsSubset(slaStart, slaEnd, null, null);
	
    					 final AggregateStats h24Stats = this.calculateStats(h24RawLogs, dateRawLog, h24EndTime, this.getLogManager());
    					 final AggregateStats slaStats = this.calculateStats(slaRawLogs, slaStart, slaEnd,this.getLogManager());
	                 
    					 final JobAggregateLogEntry aggregLog = new JobAggregateLogEntry(
	                           logFetcher.getParentJob(), dateRawLog, h24Stats,
	                           slaStats);
	
    					 LogDaoHelper.getLogDao().persistAggregLog(aggregLog);
    					 allAggregateLogs.put(dateRawLog.getTime(), aggregLog);
    				 }
    			 }
    			 dateRawLog.add(Calendar.DATE,1);
    		}
    		
    	}
    	
    	/*
    	final Set<RawLogEntry> allRawLogs = this.getLogManager().getRawLogs();
        
        final Set<Calendar> rawLogsDates = this.getRawLogDates(allRawLogs);
        final Map<Date, AbstractAggregateLogEntry> allAggregateLogs 
        = this.getLogManager().getAggregateLogs();
        final Calendar today = DateUtil.truncateTime(Calendar.getInstance());

        for (Calendar dateRawLog : rawLogsDates) {

            if (!allAggregateLogs.containsKey(dateRawLog.getTime())
                && 0 != DateUtil.compareWithoutTime(dateRawLog, today)) {

                final JobLogFetcher logFetcher 
                    = (JobLogFetcher) this.getLogManager().getLogFetcher();
                final JobConfiguration parentJobConfig 
                    = logFetcher.getParentJob().getConfig();
                final Calendar slaStart = DateUtil.mixDateTime(
                           dateRawLog, parentJobConfig.getSlaStartTime());
                final Calendar h24EndTime = DateUtil.setTime(dateRawLog, 
                                                             "23:59:59");
                final Calendar slaEnd = DateUtil.mixDateTime(
                           dateRawLog, parentJobConfig.getSlaEndTime());
                Set<RawLogEntry> h24RawLogs;
                Set<RawLogEntry> slaRawLogs;

                h24RawLogs = logFetcher.fetchRawLogsSubset(
                                   dateRawLog, h24EndTime, null, null);
                slaRawLogs = logFetcher.fetchRawLogsSubset(
                                   slaStart, slaEnd, null, null);

                final AggregateStats h24Stats 
                    = this.calculateStats(h24RawLogs, dateRawLog, 
                                          h24EndTime, this.getLogManager());
                
                final AggregateStats slaStats
                    = this.calculateStats(slaRawLogs, slaStart, slaEnd,
                                          this.getLogManager());
                
                final JobAggregateLogEntry aggregLog = new JobAggregateLogEntry(
                          logFetcher.getParentJob(), dateRawLog, h24Stats,
                          slaStats);

                LogDaoHelper.getLogDao().persistAggregLog(aggregLog);
                allAggregateLogs.put(dateRawLog.getTime(), aggregLog);
            }
        }*/
	
    }
    
    /**
     *  Aggregates the raw logs for the job.
     *  <p>
     *  The raw logs are aggregated into daily summaries. Dates for which an
     *  aggregate log entry already exists are skipped. (That is, existing
     *  aggregate log entries are not overwritten.)
     *  Creates the aggregated log values with inspire rules and without
     */
    public void aggregateHourRawLogs()
    {
    	List<Calendar> minmaxPeriod = this.getLogManager().getRawlogMinMax();
    	if(minmaxPeriod.size() > 1 && minmaxPeriod.get(0) != null && minmaxPeriod.get(1) != null)
    	{
    		Calendar dateRawLog = DateUtil.truncateTime(minmaxPeriod.get(1));
    		Calendar maxdate = DateUtil.setTime(minmaxPeriod.get(0),"23:59:59");
    		final Map<Date, AbstractAggregateHourLogEntry> allAggregateHourLogs = this.getLogManager().getAggregateHourLogs(); 
    		final Calendar today = DateUtil.truncateTime(Calendar.getInstance()); 
    		while(maxdate.after(dateRawLog))
    		{
    			final JobLogFetcher logFetcher = (JobLogFetcher) this.getLogManager().getLogFetcher();	
    	    	Set<RawLogEntry> dailyRawLogs;
    	        Set<RawLogEntry> inspireRawLogs;
    	        Set<RawLogEntry> inspireHourRawLogs;
                
    	        if (!allAggregateHourLogs.containsKey(dateRawLog.getTime()) && 
                  		0 != DateUtil.compareWithoutTime(dateRawLog, today)) {
    	        	Calendar timeStart = DateUtil.setTime(dateRawLog, "00:00:00");
    	          	Calendar timeEnd = DateUtil.setTime(dateRawLog, "23:59:59");
    	            
                	dailyRawLogs = logFetcher.fetchRawLogsSubset(timeStart,timeEnd, null, null);
                	// Only create log when there is data
                	if(dailyRawLogs.size() > 0)
                  	{
	                	// Inspire daylog
	                  	inspireRawLogs = this.inspireRawLog(dailyRawLogs, timeStart, timeEnd);
	                  	for(int i = 0;i<24;i++)
	                	{
	                  		timeStart = DateUtil.setTime(dateRawLog, this.createHourTimeString(i,false));
	                		timeEnd = DateUtil.setTime(dateRawLog, this.createHourTimeString(i,true));
	                		
	                		// Fetch log data for hour
	                		dailyRawLogs = logFetcher.fetchRawLogsSubset(timeStart,timeEnd, null, null);
	                		
	                		inspireHourRawLogs = this.fetchSetRawLogsSubset(timeStart, timeEnd, inspireRawLogs);
	                		
	                		if(dailyRawLogs.size() > 0)
			            	{ 
	                			AggregateStats h1Stats = this.calculateHourStats(dailyRawLogs, timeStart, timeEnd,this.getLogManager());
	                	    	AggregateStats inspireStats = this.calculateHourStats(inspireHourRawLogs, timeStart, timeEnd,this.getLogManager());
	                 	    
	                	    	// Create aggregLog
	                	    	JobAggregateHourLogEntry aggregLog = new JobAggregateHourLogEntry
	                 	    	(logFetcher.getParentJob(),timeStart, h1Stats,inspireStats);
	                     
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
        // Get raw logs dates
    	final Set<Calendar> rawLogsDates = this.getRawLogDates(allRawLogs);
        final Map<Date, AbstractAggregateHourLogEntry> allAggregateHourLogs = this.getLogManager().getAggregateHourLogs();
        final Calendar today = DateUtil.truncateTime(Calendar.getInstance());
        
        for (Calendar dateRawLog : rawLogsDates) {        	
        	final JobLogFetcher logFetcher = (JobLogFetcher) this.getLogManager().getLogFetcher();	
	    	Set<RawLogEntry> dailyRawLogs;
	        Set<RawLogEntry> inspireRawLogs;
	        Set<RawLogEntry> inspireHourRawLogs;
              
          	// Time interval for each hour
          	Calendar timeStart = DateUtil.setTime(dateRawLog, "00:00:00");
          	Calendar timeEnd = DateUtil.setTime(dateRawLog, "23:59:59");
            if (!allAggregateHourLogs.containsKey(dateRawLog.getTime()) && 
              		0 != DateUtil.compareWithoutTime(dateRawLog, today)) {
            	  
            	dailyRawLogs = logFetcher.fetchRawLogsSubset(timeStart,timeEnd, null, null);
              	// Inspire daylog
              	inspireRawLogs = this.inspireRawLog(dailyRawLogs, timeStart, timeEnd);
              	for(int i = 0;i<24;i++)
            	{
              		timeStart = DateUtil.setTime(dateRawLog, this.createHourTimeString(i,false));
            		timeEnd = DateUtil.setTime(dateRawLog, this.createHourTimeString(i,true));
            		
            		// Fetch log data for hour
            		dailyRawLogs = logFetcher.fetchRawLogsSubset(timeStart,timeEnd, null, null);
            		
            		inspireHourRawLogs = this.fetchSetRawLogsSubset(timeStart, timeEnd, inspireRawLogs);
            		
            		AggregateStats h1Stats = this.calculateHourStats(dailyRawLogs, timeStart, timeEnd,this.getLogManager());
            	    
            	    AggregateStats inspireStats = this.calculateHourStats(inspireHourRawLogs, timeStart, timeEnd,this.getLogManager());
             	    
            	    // Create aggregLog
            	    JobAggregateHourLogEntry aggregLog = new JobAggregateHourLogEntry
             	    (logFetcher.getParentJob(),timeStart, h1Stats,inspireStats);
                 
             	    LogDaoHelper.getLogDao().persistAggregHourLog(aggregLog);
             	    allAggregateHourLogs.put(dateRawLog.getTime(), aggregLog);
            	}
              	
              }
        }
        */

    	}
    	    
}
