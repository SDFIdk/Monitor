/**
 * 
 */
package org.easysdi.monitor.gui.webapp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.deegree.framework.util.DateUtil;


import org.easysdi.monitor.biz.job.HolidayCollection;
import org.easysdi.monitor.biz.job.Sla;
import org.easysdi.monitor.biz.job.Period;
import org.easysdi.monitor.biz.job.Holiday;
import org.easysdi.monitor.biz.logging.AbstractAggregateHourLogEntry;
import org.easysdi.monitor.biz.logging.RawLogEntry;


/**
 * Helper class for sorting logs for a given sla
 * 
 * @author Thomas Bergstedt - arx iT
 * @version 1.0, 2011-05-30
 *
 */
public final class LogSlaHelper {

	private final List<Holiday> holidays =  new HolidayCollection().getHoliday();
	
	/**
	 * Default constructor
	 */
	public LogSlaHelper()
	{
	}
	
	/**
	 * 
	 * @param slaID
	 * @param agglog
	 */
	public static Map<Date, AbstractAggregateHourLogEntry> getAggHourLogForSla(String slaID, Map<Date, AbstractAggregateHourLogEntry> agglog)
	{
		LogSlaHelper helper = new LogSlaHelper();
		Sla sla = Sla.getFromIdString(slaID);
    	Collection<Period> periods = sla.getSlaPeriodList();
    	Map<Date, AbstractAggregateHourLogEntry> slaHourLogentries = new LinkedHashMap<Date, AbstractAggregateHourLogEntry>();

    	boolean includeLog = false;
    	Iterator<Entry<Date, AbstractAggregateHourLogEntry>> iter = agglog.entrySet().iterator();
    	while(iter.hasNext())
    	{
    		Map.Entry<Date, AbstractAggregateHourLogEntry>  entry = (Map.Entry<Date, AbstractAggregateHourLogEntry>) iter.next();
    		Date dateKey = entry.getKey();
    		Calendar logDate = DateUtil.dateToCalendar(dateKey);
    		AbstractAggregateHourLogEntry logObject = entry.getValue();
    		includeLog = false;
    		for(Period period: periods)
    		{
        		if(period.isInclude())
        		{
        			// Specific date rule
        			if(period.getDate() != null)
        			{
        				// Exclude date
        				if((DateUtil.compareWithoutTime(logDate, period.getDate()) == 0) &&
        					helper.checkInsideTimeIntervelHour(period.getSlaStartTime(),period.getSlaEndTime(),logDate))
        				{
        					includeLog = true;
        				}
        			}else if(helper.isweekDaychecked(logDate.get(Calendar.DAY_OF_WEEK), period) || (period.isHolidays() && helper.isHoliday(logDate)) )
    				{
        				if(helper.checkInsideTimeIntervelHour(period.getSlaStartTime(),period.getSlaEndTime(),logDate))
    					{		
    						includeLog = true;
    					}
    				}
        		}
        		else
        		{
        			// Specific date rule
        			if(period.getDate() != null)
        			{
        				// Exclude date
        				if((DateUtil.compareWithoutTime(logDate, period.getDate()) == 0) &&
        				helper.checkInsideTimeIntervelHour(period.getSlaStartTime(),period.getSlaEndTime(),logDate))
        				{
        					includeLog = false;
        					break;
        				}
        			}else if(helper.isweekDaychecked(logDate.get(Calendar.DAY_OF_WEEK), period) || (period.isHolidays() && helper.isHoliday(logDate)) )
        			{
    					if(helper.checkInsideTimeIntervelHour(period.getSlaStartTime(),period.getSlaEndTime(),logDate))
    					{	 							
    						includeLog = false;
    						break;
    					}
    				}			
        		}
    		}
        	if(includeLog)
        	{
        		slaHourLogentries.put(logDate.getTime(),logObject);
        	}
    	}	
    	return slaHourLogentries;
	}
	
	public static Set<RawLogEntry> getRawLogForDefault(Set<RawLogEntry> rawLogs,boolean createSummary)
	{
		float maxReqTime = 0.0F;
		float timeSummary = 0.0F;
		int avCount = 0;
		int fCount = 0;
		int unavCount = 0;
		int otherCount = 0;
		int count = 0;
		Set<RawLogEntry> summaryRawLogs = new LinkedHashSet<RawLogEntry>();
		ArrayList<Date> dates = new ArrayList<Date>();
		ArrayList<Long> list = new ArrayList<Long>();
		HashMap<Date,ArrayList<Long>> foundIds = new HashMap<Date,ArrayList<Long>>(); 
		
		for(RawLogEntry log : rawLogs)
		{
			count = 0;
			avCount = 0;
			fCount = 0;
			unavCount = 0;
			otherCount = 0;
			timeSummary = 0.0F;
			if(log.getResponseDelay() > 0)
			{
				maxReqTime = log.getResponseDelay();
			}else
			{
				maxReqTime = 0.0F;
			}
		
			if(log.getStatusValue().name().equals("AVAILABLE"))
			{
				avCount++;
			}else if(log.getStatusValue().name().equals("OUT_OF_ORDER")){
				fCount++;
			}else if(log.getStatusValue().name().equals("UNAVAILABLE"))
			{
				unavCount++;
			}else if(log.getStatusValue().name().equals("NOT_TESTED"))
			{
				otherCount++;
			}else
			{
				otherCount++;
			}
			list = new ArrayList<Long>();
			list.add(log.getQueryId());
			foundIds.put(log.getRequestTime().getTime(),list);
			for(RawLogEntry log2 : rawLogs)
			{
				// Same time and not same object;
				if(log.getRequestTime().getTime().equals(log2.getRequestTime().getTime()) && 
				   !foundIds.get(log.getRequestTime().getTime()).contains(log2.getQueryId()))
				{
				
					count++;
					if(maxReqTime < log2.getResponseDelay())
					{
						maxReqTime = log2.getResponseDelay();
					}
					if(log2.getResponseDelay() > 0)
					{
						timeSummary += log2.getResponseDelay();
					}
					if(log2.getStatusValue().name().equals("AVAILABLE"))
					{
						avCount++;
					}else if(log2.getStatusValue().name().equals("OUT_OF_ORDER")){
						fCount++;
					}else if(log2.getStatusValue().name().equals("UNAVAILABLE"))
					{
						unavCount++;
					}else if(log2.getStatusValue().name().equals("NOT_TESTED"))
					{
						otherCount++;
					}else
					{
						otherCount++;
					}
					foundIds.get(log.getRequestTime().getTime()).add(log2.getQueryId());
				}
			}
			
			if(count > 0)
			{
				timeSummary = timeSummary / count;
				log.setResponseDelay(timeSummary);
			}
			if(!dates.contains(log.getRequestTime().getTime()))
			{
				log.setAvCount(avCount);
				log.setOtherCount(otherCount);
				log.setfCount(fCount);
				log.setUnavCount(unavCount);
				log.setMaxTime(maxReqTime);
				log.setMessage("");
				summaryRawLogs.add(log);
			}
			dates.add(log.getRequestTime().getTime());
		}
		return summaryRawLogs;
	}
	
	/**
	 * Filters a raw log set for a given sla
	 * Can also be used to create a summary of a jobs queries
	 * @param slaID
	 * @param rawLogs
	 * @param createSummary
	 * Return Filtered raw log set
	*/
	public static Set<RawLogEntry> getRawlogForSla(String slaID, Set<RawLogEntry> rawLogs,boolean createSummary)
	{
		LogSlaHelper helper = new LogSlaHelper();
		Sla sla = Sla.getFromIdString(slaID);
    	Collection<Period> periods = sla.getSlaPeriodList();
    	Set<RawLogEntry> slaRawLogs = new LinkedHashSet<RawLogEntry>();
    	
    	Boolean useInspire = sla.isExcludeWorst();
    	/*if(useInspire)
    	{
    		rawLogs = helper.inspireRawLogs(rawLogs);
    	}*/
    	
    	boolean includeLog = false;
    	for(RawLogEntry log: rawLogs)
    	{
    		includeLog = false;
    		Calendar logDate = log.getRequestTime();
    		for(Period period: periods)
    		{
    			if(period.isInclude())
    			{
    				// Specific date rule
    				if(period.getDate() != null)
    				{
    					// Exclude date
    					if((DateUtil.compareWithoutTime(logDate, period.getDate()) == 0) &&
    						helper.checkInsideTimeIntervel(period.getSlaStartTime(),period.getSlaEndTime(),logDate))
    					{
    						includeLog = true;
    					}
    				}else if(helper.isweekDaychecked(logDate.get(Calendar.DAY_OF_WEEK), period) || (period.isHolidays() && helper.isHoliday(logDate)) )
					{
    					if(helper.checkInsideTimeIntervel(period.getSlaStartTime(),period.getSlaEndTime(),logDate))
						{		
							includeLog = true;
						}
					}
    			}
    			else
    			{
    				// Specific date rule
    				if(period.getDate() != null)
    				{
    					// Exclude date
    					if((DateUtil.compareWithoutTime(logDate, period.getDate()) == 0) &&
    					helper.checkInsideTimeIntervel(period.getSlaStartTime(),period.getSlaEndTime(),logDate))
    					{
    						includeLog = false;
    						break;
    					}
    				}else if(helper.isweekDaychecked(logDate.get(Calendar.DAY_OF_WEEK), period) || (period.isHolidays() && helper.isHoliday(logDate)) )
    				{
    					if(helper.checkInsideTimeIntervel(period.getSlaStartTime(),period.getSlaEndTime(),logDate))
						{	 							
							includeLog = false;
							break;
						}
					}			
    			}	
    		}	
    		if(includeLog)
    		{
    			slaRawLogs.add(log);
    		}
    	}
    	// Remove 10 %
    
    	
    	if(!createSummary)
    	{
    		if(useInspire)
        	{
        		slaRawLogs = helper.inspireRawLogs(slaRawLogs);
        	}
    		return slaRawLogs;
    	}
    	
		float maxReqTime = 0.0F;
		float timeSummary = 0.0F;
		int avCount = 0;
		int fCount = 0;
		int unavCount = 0;
		int otherCount = 0;
		int count = 0;
		Set<RawLogEntry> summaryRawLogs = new LinkedHashSet<RawLogEntry>();
		ArrayList<Date> dates = new ArrayList<Date>();
		ArrayList<Long> list = new ArrayList<Long>();
		HashMap<Date,ArrayList<Long>> foundIds = new HashMap<Date,ArrayList<Long>>(); 
		
		for(RawLogEntry log : slaRawLogs)
		{
			count = 0;
			avCount = 0;
			fCount = 0;
			unavCount = 0;
			otherCount = 0;
			timeSummary = 0.0F;
			if(log.getResponseDelay() > 0)
			{
				maxReqTime = log.getResponseDelay();
			}else
			{
				maxReqTime = 0.0F;
			}
		
			if(log.getStatusValue().name().equals("AVAILABLE"))
			{
				avCount++;
			}else if(log.getStatusValue().name().equals("OUT_OF_ORDER")){
				fCount++;
			}else if(log.getStatusValue().name().equals("UNAVAILABLE"))
			{
				unavCount++;
			}else if(log.getStatusValue().name().equals("NOT_TESTED"))
			{
				otherCount++;
			}else
			{
				otherCount++;
			}
			list = new ArrayList<Long>();
			list.add(log.getQueryId());
			foundIds.put(log.getRequestTime().getTime(),list);
			for(RawLogEntry log2 : slaRawLogs)
			{
				// Same time and not same object;
				if(log.getRequestTime().getTime().equals(log2.getRequestTime().getTime()) && 
				   !foundIds.get(log.getRequestTime().getTime()).contains(log2.getQueryId()))
				{
				
					count++;
					if(maxReqTime < log2.getResponseDelay())
					{
						maxReqTime = log2.getResponseDelay();
					}
					if(log2.getResponseDelay() > 0)
					{
						timeSummary += log2.getResponseDelay();
					}
					if(log2.getStatusValue().name().equals("AVAILABLE"))
					{
						avCount++;
					}else if(log2.getStatusValue().name().equals("OUT_OF_ORDER")){
						fCount++;
					}else if(log2.getStatusValue().name().equals("UNAVAILABLE"))
					{
						unavCount++;
					}else if(log2.getStatusValue().name().equals("NOT_TESTED"))
					{
						otherCount++;
					}else
					{
						otherCount++;
					}
					foundIds.get(log.getRequestTime().getTime()).add(log2.getQueryId());
				}
			}
			
			if(count > 0)
			{
				timeSummary = timeSummary / count;
				log.setResponseDelay(timeSummary);
			}
			if(!dates.contains(log.getRequestTime().getTime()))
			{
				log.setAvCount(avCount);
				log.setOtherCount(otherCount);
				log.setfCount(fCount);
				log.setUnavCount(unavCount);
				log.setMaxTime(maxReqTime);
				log.setMessage("");
			
				summaryRawLogs.add(log);
			}
			dates.add(log.getRequestTime().getTime());
		}
		if(useInspire)
    	{
			summaryRawLogs = helper.inspireRawLogs(summaryRawLogs);
    	}
		return summaryRawLogs;
	}
	

	
	/**
	 * Checks if log date is a holiday 
	 * @return
	 */
	private boolean isHoliday(Calendar logDate)
	{
		
		for(Holiday day : this.holidays)
		{
			if(DateUtil.compareWithoutTime(day.getDate(), logDate) == 0)
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Check if weekday is checked in period
	 * @param day
	 * @return
	 */
	private boolean isweekDaychecked(int day, Period period)
	{
		boolean checked = false;
		switch(day)
		{
			case Calendar.SUNDAY:
				checked = period.isSunday();
				break;
			case Calendar.MONDAY:
				checked = period.isMonday();
				break;
			case Calendar.TUESDAY:
				checked = period.isTuesday();
				break;
			case Calendar.WEDNESDAY:
				checked = period.isWednesday();
				break;
			case Calendar.THURSDAY:
				checked = period.isThursday();
				break;
			case Calendar.FRIDAY:
				checked = period.isFriday();
				break;
			case Calendar.SATURDAY:
				checked = period.isSaturday();
				break; 
		}
		return checked;
	}
	
	/**
     * Finds and remove the 10% worst delivery time record
     * 
     * @param logs
     * @return set with the 90% best result of the given set 
     */
	private Set<RawLogEntry> inspireRawLogs(Set<RawLogEntry> rawLogs) {

	  	final int procent = 10;      
    	int countToRemove = (rawLogs.size() * procent) / 100;
    	
    	List<RawLogEntry> sortLogs = new ArrayList<RawLogEntry>(rawLogs);
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
	 * Test if a given log time is in sla interval 
	 * @param slaStartTime
	 * @param slaEndTime
	 * @param logTime
	 * @return result if sla interval time compare
	 */
	private boolean checkInsideTimeIntervelHour(Calendar slaStartTime,Calendar slaEndTime, Calendar logTime)
	{
		boolean result = false;	
		// Test for time 00.00.00
		Calendar zeroTime = DateUtil.setTime(slaEndTime, "00:00:00");
		if(slaEndTime.getTime().equals(zeroTime.getTime()))
		{
			slaEndTime = DateUtil.setTime(slaEndTime, "23:59:59");
		}	
		Boolean minInterval = false;
		Boolean maxInterval = false;
		if( slaStartTime.get(Calendar.HOUR_OF_DAY) <= logTime.get(Calendar.HOUR_OF_DAY))
		{
			minInterval = true;
		}
		System.out.println("LogHour: "+logTime.get(Calendar.HOUR_OF_DAY) + " TimeHour: "+slaEndTime.get(Calendar.HOUR_OF_DAY));
		if(logTime.get(Calendar.HOUR_OF_DAY) < slaEndTime.get(Calendar.HOUR_OF_DAY))
		{
			maxInterval = true;
		}
		if(maxInterval && minInterval)
		{
			result = true;
		}
		return result;
	}
	
	/**
	 * Test if a given log time is in sla interval 
	 * @param slaStartTime
	 * @param slaEndTime
	 * @param logTime
	 * @return result if sla interval time compare
	 */
	private boolean checkInsideTimeIntervel(Calendar slaStartTime,Calendar slaEndTime, Calendar logTime)
	{
		boolean result = false;	
		// Test for time 00.00.00
		Calendar zeroTime = DateUtil.setTime(slaEndTime, "00:00:00");
		if(slaEndTime.getTime().equals(zeroTime.getTime()))
		{
			slaEndTime = DateUtil.setTime(slaEndTime, "23:59:59");
		}	
		Boolean minInterval = false;
		Boolean maxInterval = false;
		if( slaStartTime.get(Calendar.HOUR_OF_DAY) < logTime.get(Calendar.HOUR_OF_DAY))
		{
			minInterval = true;
		}
		else if (slaStartTime.get(Calendar.HOUR_OF_DAY) == logTime.get(Calendar.HOUR_OF_DAY))
		{
			if(slaStartTime.get(Calendar.MINUTE) < logTime.get(Calendar.MINUTE))
			{
				minInterval = true;
			}else
			{
				if(slaStartTime.get(Calendar.MINUTE) == logTime.get(Calendar.MINUTE))
				{
					if(slaStartTime.get(Calendar.SECOND) <= logTime.get(Calendar.SECOND))
					{
						minInterval = true;
					}
				}
			}
		}
		System.out.println("LogHour: "+logTime.get(Calendar.HOUR_OF_DAY) + " TimeHour: "+slaEndTime.get(Calendar.HOUR_OF_DAY));
		if(logTime.get(Calendar.HOUR_OF_DAY) < slaEndTime.get(Calendar.HOUR_OF_DAY))
		{
			maxInterval = true;
		}
		else if ( logTime.get(Calendar.HOUR_OF_DAY) == slaEndTime.get(Calendar.HOUR_OF_DAY))
		{
			if( logTime.get(Calendar.MINUTE) < slaEndTime.get(Calendar.MINUTE))
			{
				maxInterval = true;
			}else
			{
				if(slaEndTime.get(Calendar.MINUTE) == logTime.get(Calendar.MINUTE))
				{
					if(logTime.get(Calendar.SECOND) <= slaEndTime.get(Calendar.SECOND))
					{
						maxInterval = true;
					}
				}
			}
		}	
		if(maxInterval && minInterval)
		{
			result = true;
		}
		return result;
	}
}
