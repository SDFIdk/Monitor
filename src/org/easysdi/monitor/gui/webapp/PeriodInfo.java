/**
 * 
 */
package org.easysdi.monitor.gui.webapp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.deegree.framework.util.BooleanUtil;
import org.deegree.framework.util.DateUtil;
import org.deegree.framework.util.StringTools;
import org.easysdi.monitor.biz.job.Sla;
import org.easysdi.monitor.biz.job.Period;

/**
 * @author berg3428
 *
 */
public class PeriodInfo {

	private Sla parentSla;
	private String name;
	private boolean monday;
	private boolean tuesday;
	private boolean wednesday;
	private boolean thursday;
	private boolean friday;
	private boolean saturday;
	private boolean sunday;
	private boolean holidays;
	private Calendar slaStartTime;
	private Calendar slaEndTime;
	private boolean include;
	private Calendar date;
	   
	/**
     * Format used for time strings (HH:mm:ss).
     */
    private static final String TIME_FORMAT = "HH:mm:ss";
    private final String[] acceptedDateFormats = new String[] {"yyyy-MM-dd"};
    
	
	/**
	 * Default constructor
	 */
	private PeriodInfo() {
	}
	
	
	
	/**
	 * @return the parentSla
	 */
	private Sla getParentSla() {
		return this.parentSla;
	}



	/**
	 * @param parentSla the parentSla to set
	 */
	private void setParentSla(Sla parentSla) {
	   if (null == parentSla){
            throw new IllegalArgumentException("Sla must be a valid one");
        }
		this.parentSla = parentSla;
	}

	/**
	 * @return the name
	 */
	private String getName() {
		return this.name;
	}

	/**
	 * @param name the name to set
	 */
	private void setName(String newName) {
		if (StringTools.isNullOrEmpty(newName)) {
            throw new IllegalArgumentException("Name can't be null or empty");
        }
        this.name = newName;
	}



	/**
	 * @return the monday
	 */
	private boolean isMonday() {
		return this.monday;
	}



	/**
	 * @param monday the monday to set
	 */
	private void setMonday(String mondayStr) {
		this.monday = BooleanUtil.parseBooleanStringWithNull(mondayStr);
	}



	/**
	 * @return the tuesday
	 */
	private boolean isTuesday() {
		return this.tuesday;
	}



	/**
	 * @param tuesday the tuesday to set
	 */
	private void setTuesday(String tuesdayStr) {
		this.tuesday = BooleanUtil.parseBooleanStringWithNull(tuesdayStr);
	}



	/**
	 * @return the wednesday
	 */
	private boolean isWednesday() {
		return this.wednesday;
	}

	/**
	 * @param wednesday the wednesday to set
	 */
	private void setWednesday(String wednesdayStr) {
		this.wednesday = BooleanUtil.parseBooleanStringWithNull(wednesdayStr);
	}

	/**
	 * @return the thursday
	 */
	private boolean isThursday() {
		return this.thursday;
	}



	/**
	 * @param thursday the thursday to set
	 */
	private void setThursday(String thursdayStr) {
		this.thursday = BooleanUtil.parseBooleanStringWithNull(thursdayStr);
	}



	/**
	 * @return the friday
	 */
	private boolean isFriday() {
		return this.friday;
	}



	/**
	 * @param friday the friday to set
	 */
	private void setFriday(String fridayStr) {
		this.friday = BooleanUtil.parseBooleanStringWithNull(fridayStr);
	}



	/**
	 * @return the saturday
	 */
	private boolean isSaturday() {
		return this.saturday;
	}



	/**
	 * @param saturday the saturday to set
	 */
	private void setSaturday(String saturdayStr) {
		this.saturday = BooleanUtil.parseBooleanStringWithNull(saturdayStr);
	}



	/**
	 * @return the sunday
	 */
	private boolean isSunday() {
		return this.sunday;
	}



	/**
	 * @param sunday the sunday to set
	 */
	private void setSunday(String sundayStr) {
		this.sunday = BooleanUtil.parseBooleanStringWithNull(sundayStr);
	}



	/**
	 * @return the holidays
	 */
	private boolean isHolidays() {
		return this.holidays;
	}



	/**
	 * @param holidays the holidays to set
	 */
	private void setHolidays(String holidaysStr) {
		this.holidays = BooleanUtil.parseBooleanStringWithNull(holidaysStr);
	}



	/**
	 * @return the slaStartTime
	 */
	private Calendar getSlaStartTime() {
		return this.slaStartTime;
	}



	/**
	 * @param slaStartTime the slaStartTime to set
	 */
	private void setSlaStartTime(String slaStartTimeStr) {
		 try {
	            this.slaStartTime = this.parseTimeString(slaStartTimeStr);
	        } catch (ParseException e) {
	            throw new IllegalArgumentException(
	                   "Invalid or badly formatted SLA start time.");
	        }
	}



	/**
	 * @return the slaEndTime
	 */
	private Calendar getSlaEndTime() {
		
		return this.slaEndTime;
	}



	/**
	 * @param slaEndTime the slaEndTime to set
	 */
	private void setSlaEndTime(String slaEndTimeStr) {
		  try {
	            this.slaEndTime = this.parseTimeString(slaEndTimeStr);
	        } catch (ParseException e) {
	            throw new IllegalArgumentException(
	                   "Invalid or badly formatted SLA end time.");
	        }
	}



	/**
	 * @return the include
	 */
	private boolean isInclude() {
		return this.include;
	}



	/**
	 * @param include the include to set
	 */
	private void setInclude(String includeStr) {
		this.include = BooleanUtil.parseBooleanStringWithNull(includeStr);
	}



	/**
	 * @return the date
	 */
	private Calendar getDate() {
		return this.date;
	}



	/**
	 * @param date the date to set
	 */
	private void setDate(String dateStr)
	    throws MonitorInterfaceException {
	        try {
	            this.date = this.parseDateString(dateStr);

	        } catch (IllegalArgumentException e) {
	            throw new MonitorInterfaceException(
	              "Specific date format is invalid", "period.date.invalid");
	        }

	}



	public static PeriodInfo createFromParametersMap(
           Map<String, String> requestParams, Sla parentSla, 
           boolean enforceMandatory) throws MonitorInterfaceException {
		PeriodInfo newPeriodInfo = new PeriodInfo();
		newPeriodInfo.setParentSla(parentSla);
		
		final String nameString = requestParams.get("name");
        if (enforceMandatory || !StringTools.isNullOrEmpty(nameString)) {
            newPeriodInfo.setName(nameString);
        }
        
        newPeriodInfo.setMonday(requestParams.get("isMonday"));
        newPeriodInfo.setTuesday(requestParams.get("isTuesday"));
        newPeriodInfo.setWednesday(requestParams.get("isWednesday"));
        newPeriodInfo.setThursday(requestParams.get("isThursday"));
        newPeriodInfo.setFriday(requestParams.get("isFriday"));
        newPeriodInfo.setSaturday(requestParams.get("isSaturday"));
        newPeriodInfo.setSunday(requestParams.get("isSunday"));
        newPeriodInfo.setHolidays(requestParams.get("isHolidays"));       
        newPeriodInfo.setInclude(requestParams.get("isInclude"));
        
        final String slaStartTime = requestParams.get("slaStartTime");
        if(enforceMandatory || !StringTools.isNullOrEmpty(slaStartTime))
        {
        	newPeriodInfo.setSlaStartTime(slaStartTime);
        }
        
        final String slaEndTime = requestParams.get("slaEndTime");
        if(enforceMandatory || !StringTools.isNullOrEmpty(slaEndTime))
        {
        	newPeriodInfo.setSlaEndTime(slaEndTime);
        }
        
        final String dateStr = requestParams.get("date");
        if(!StringTools.isNullOrEmpty(dateStr))
        {
        	newPeriodInfo.setDate(dateStr);
        }
		return newPeriodInfo;
	}
	
	public static Period createModifyParametersMap(
	           Map<String, String> requestParams, Period period) 
	throws MonitorInterfaceException {
		
		PeriodInfo newPeriodInfo = new PeriodInfo();
		
		final String nameString = requestParams.get("name");
        if (!StringTools.isNullOrEmpty(nameString)) {
             period.setName(nameString);
        }
        
        if(requestParams.get("isMonday") != null && StringUtils.isNotBlank(requestParams.get("isMonday")))
        {
        	newPeriodInfo.setMonday(requestParams.get("isMonday"));
        	period.setMonday(newPeriodInfo.isMonday());
        }
        
        if(requestParams.get("isTuesday") != null && StringUtils.isNotBlank(requestParams.get("isTuesday")))
        {
        	newPeriodInfo.setTuesday(requestParams.get("isTuesday"));
        	period.setTuesday(newPeriodInfo.isTuesday());
        }
        
        if(requestParams.get("isWednesday") != null && StringUtils.isNotBlank(requestParams.get("isWednesday")))
        {
        	newPeriodInfo.setWednesday(requestParams.get("isWednesday"));
        	period.setWednesday(newPeriodInfo.isWednesday());
        }
        
        if(requestParams.get("isThursday") != null && StringUtils.isNotBlank(requestParams.get("isThursday")))
        {
        	newPeriodInfo.setThursday(requestParams.get("isThursday"));
        	period.setThursday(newPeriodInfo.isThursday());
        }
        
        if(requestParams.get("isFriday")!= null && StringUtils.isNotBlank(requestParams.get("isFriday")))
        {
        	newPeriodInfo.setFriday(requestParams.get("isFriday"));
        	period.setFriday(newPeriodInfo.isFriday());
        }
        
        if(requestParams.get("isSaturday") != null && StringUtils.isNotBlank(requestParams.get("isSaturday")))
        {
          	newPeriodInfo.setSaturday(requestParams.get("isSaturday"));
        	period.setSaturday(newPeriodInfo.isSaturday());
        }
        
        if(requestParams.get("isSunday") != null && StringUtils.isNotBlank(requestParams.get("isSunday")))
        {
          	newPeriodInfo.setSunday(requestParams.get("isSunday"));
        	period.setSunday(newPeriodInfo.isSunday());
        }
        
        if(requestParams.get("isHolidays") != null && StringUtils.isNotBlank(requestParams.get("isHolidays")))
        {
           	newPeriodInfo.setHolidays(requestParams.get("isHolidays"));
        	period.setHolidays(newPeriodInfo.isHolidays());
        }
        
        if(requestParams.get("isInclude") != null && StringUtils.isNotBlank(requestParams.get("isInclude")))
        {
           	newPeriodInfo.setInclude(requestParams.get("isInclude"));
        	period.setInclude(newPeriodInfo.isInclude());
        }
        
        final String slaStartTime = requestParams.get("slaStartTime");
        if(!StringTools.isNullOrEmpty(slaStartTime))
        {
        	newPeriodInfo.setSlaStartTime(slaStartTime);
        	period.setSlaStartTime(newPeriodInfo.getSlaStartTime());
        }
        
        final String slaEndTime = requestParams.get("slaEndTime");
        if(!StringTools.isNullOrEmpty(slaEndTime))
        {
        	newPeriodInfo.setSlaEndTime(slaEndTime);
        	period.setSlaEndTime(newPeriodInfo.getSlaEndTime());
        }
        
        final String dateStr = requestParams.get("date");
        if(dateStr != null)
        {
        	if(!StringUtils.isBlank(dateStr))
        	{
        		newPeriodInfo.setDate(dateStr);
            	period.setDate(newPeriodInfo.getDate());
            	period.changeDateType(true);
        	}else
        	{
        		period.changeDateType(false);
        	}
        }
		return period;
	}
		
	
	
	/**
     * Updates a query with the informations contained in this object.
     * 
     * @param   query   the query to update
     * @return          <code>true</code> if the update succeeded
     */
    public static boolean modifyPeriod(Period period) {

        if (null == period) {
            throw new IllegalArgumentException("period can't be null");
        } 
        return period.persist();
    }

	
	
	 /**
     * Parses a string for a time value.
     * 
     * @param   timeString      a string containing a time value
     * @return                  <ul>
     *                          <li>a calendar containing the time found in the 
     *                          string. (The date part should be ignored.)</li>
     *                          <li><code>null</code> if the string was null or
     *                          empty</li>
     *                          </ul>
     * @throws  ParseException  no time could be made out of the string
     * @see                     JobInfo#TIME_FORMAT
     */
    private Calendar parseTimeString(String timeString) throws ParseException {

        if (StringTools.isNullOrEmpty(timeString)) {
            return null;
        }
        final SimpleDateFormat timeFormat = new SimpleDateFormat(PeriodInfo.TIME_FORMAT);
        return DateUtil.dateToCalendar(timeFormat.parse(timeString));
    }
    
    /**
     * Parses a string looking for a YYYY-MM-DD date.
     * 
     * @param   dateString  the string containing the date to extract
     * @return              the date if the string matches the format or<br>
     *                      <code>null</code> otherwise
     */
    private Calendar parseDateString(String dateString) {

        if (!StringUtils.isBlank(dateString)) {
            final Calendar parsedDate = Calendar.getInstance();
            try {
                parsedDate.setTime(
                    DateUtils.parseDate(dateString, this.acceptedDateFormats));
                return parsedDate;

            } catch (ParseException e) {
                throw new IllegalArgumentException("Invalid date format");
            }
        }
        return null;
    }
    
    /**
     * Creates a new period based on the informations contained in this object.
     * 
     * @return  the new period
     */
    public Period createPeriod() {
        final Period newPeriod = new Period(this.getParentSla().getSlaId(), this.getName(), this.isMonday(), this.isTuesday(),this.isWednesday(),this.isThursday(),this.isFriday(), this.isSaturday(),this.isSunday(),
        		this.isHolidays(),this.getSlaStartTime(),this.getSlaEndTime(),this.isInclude(), this.getDate());
        if(newPeriod.getDate() != null)
        {
        	newPeriod.changeDateType(true);
        }
        
        if (newPeriod.isValid() && newPeriod.persist()) {
            return newPeriod;
        }
        return null;
    }
}
