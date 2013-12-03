/**
 * 
 */
package org.easysdi.monitor.biz.job;

import java.util.Calendar;

import org.deegree.framework.util.StringTools;
import org.easysdi.monitor.dat.dao.PeriodDaoHelper;
import java.text.SimpleDateFormat;

/**
 * A time period 
 * <p>
 * A period can be time specification
 * where log data should be included or excluded   
 * 
 * @author Thomas Berstedt - arx iT
 * @version 1.0, 2011-04-14

 */
public class Period {

	private long periodId;
	private Sla parentSla;
	private long slaId;
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
	 * 
	 */
	public Period() {
		
	}
	
	/**
	 * @param parentSla
	 * @param name
	 * @param monday
	 * @param tuesday
	 * @param wednesday
	 * @param thursday
	 * @param friday
	 * @param saturday
	 * @param sunday
	 * @param holidays
	 * @param slaStartTime
	 * @param slaEndTime
	 * @param include
	 * @param date
	 */
	public Period(long slaId, String name, boolean monday, boolean tuesday,
			boolean wednesday, boolean thursday, boolean friday,
			boolean saturday, boolean sunday, boolean holidays,
			Calendar slaStartTime, Calendar slaEndTime, boolean include,
			Calendar date) {
		this.slaId = slaId;
		this.name = name;
		this.monday = monday;
		this.tuesday = tuesday;
		this.wednesday = wednesday;
		this.thursday = thursday;
		this.friday = friday;
		this.saturday = saturday;
		this.sunday = sunday;
		this.holidays = holidays;
		this.slaStartTime = slaStartTime;
		this.slaEndTime = slaEndTime;
		this.include = include;
		this.date = date;
	}

	/**
     * Gets this period's identifier.
     * 
     * @return the long that uniquely identify this period
     */
	public long getPeriodId() {
		return periodId;
	}
	
	/**
     * Defines this period's identifier.
     * <p>
     * <i><b>Note:</b> This method shouldn't be used directly. The identifier is
     * usually assigned by the persistence mechanism.</i>
     * 
     * @param   periodId  the long that uniquely identify this period
     */
	public void setPeriodId(long periodId) {
		this.periodId = periodId;
	}
	
	
	public Sla getParentSla() {
		return parentSla;
	}

	public void setParentSla(Sla parentSla) {
		this.parentSla = parentSla;
	}

	public long getSlaId() {
		return slaId;
	}

	public void setSlaId(long slaId) {
		this.slaId = slaId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isMonday() {
		return monday;
	}

	public void setMonday(boolean monday) {
		this.monday = monday;
	}

	public boolean isTuesday() {
		return tuesday;
	}

	public void setTuesday(boolean tuesday) {
		this.tuesday = tuesday;
	}

	public boolean isWednesday() {
		return wednesday;
	}

	public void setWednesday(boolean wednesday) {
		this.wednesday = wednesday;
	}

	public boolean isThursday() {
		return thursday;
	}

	public void setThursday(boolean thursday) {
		this.thursday = thursday;
	}

	public boolean isFriday() {
		return friday;
	}

	public void setFriday(boolean friday) {
		this.friday = friday;
	}

	public boolean isSaturday() {
		return saturday;
	}

	public void setSaturday(boolean saturday) {
		this.saturday = saturday;
	}

	public boolean isSunday() {
		return sunday;
	}

	public void setSunday(boolean sunday) {
		this.sunday = sunday;
	}

	public boolean isHolidays() {
		return holidays;
	}

	public void setHolidays(boolean holidays) {
		this.holidays = holidays;
	}

	public Calendar getSlaStartTime() {
		return slaStartTime;
	}

	public void setSlaStartTime(Calendar slaStartTime) {
		this.slaStartTime = slaStartTime;
	}

	public Calendar getSlaEndTime() {
		return slaEndTime;
	}

	public void setSlaEndTime(Calendar slaEndTime) {
		this.slaEndTime = slaEndTime;
	}

	public boolean isInclude() {
		return include;
	}

	public void setInclude(boolean include) {
		this.include = include;
	}

	public Calendar getDate() {
		return date;
	}

	public void setDate(Calendar date) {
		this.date = date;
	}
	
	/**
	 * Test if the current period is valid
	 * @return if the period is valid
	 */
	 public boolean isValid() {
		 SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

		 boolean valid = ((sdf.format(this.slaStartTime.getTime()).equals("00:00:00") && 
				 sdf.format(this.slaStartTime.getTime()).equals("00:00:00")) ||
				 this.slaStartTime.before(this.slaEndTime) || sdf.format(this.slaEndTime.getTime()).equals("00:00:00")) && (this.slaId > 0);
		 return valid; 
		 
	 }
	 
	 public void changeDateType(boolean isSpecificDate)
	 {
		 if(isSpecificDate)
		 {
			 this.monday = false;
			 this.tuesday = false;
			 this.wednesday = false;
			 this.thursday = false;
			 this.friday = false;
			 this.saturday = false;
			 this.sunday = false;
			 this.holidays = false;
		 }else
		 {
			this.date = null; 
		 }
	 }
	
	 /**
     * Erases this period.
     * 
     * @return <code>true</code> if this period has been successfully deleted
     */
	public boolean delete()
	{
		return PeriodDaoHelper.getPeriodDao().delete(this);
	}
	
	/**
     * Saves this period.
     * 
     * @return <code>true</code> if this period has successfully been saved
    */
	public boolean persist()
	{
		return PeriodDaoHelper.getPeriodDao().persistPeriod(this);
	}
	
	/**
     * Gets a period from an identifying string.
     * 
     * @param   idString    a string containing either the sla's identifier or 
     *                      name
     * @return              the sla, if it's been found or<br>
     *                      <code>null</code> otherwise
     */
    public static Period getFromIdString(String idString) {
        if (StringTools.isNullOrEmpty(idString)) {
            throw new IllegalArgumentException(
                   "Period identifier string can't be null or empty.");
        }
        return PeriodDaoHelper.getPeriodDao().getPeriodFromIdString(idString);
    }
	
}
