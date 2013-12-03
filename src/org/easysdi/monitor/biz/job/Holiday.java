/**
 * 
 */
package org.easysdi.monitor.biz.job;

import java.util.Calendar;

import org.deegree.framework.util.StringTools;
import org.easysdi.monitor.dat.dao.HolidayDaoHelper;


/**
 * Class for Holiday  
 * <p>
 * Holds the dates for holidays   
 * 
 * @author Thomas Berstedt - arx iT
 * @version 1.0, 2011-04-14

 */
public class Holiday {

	private long holidayId;
	private String name;
	private Calendar date;
	
	/**
	 * 
	 */
	public Holiday() {
	}

	public Holiday(String holidayName, Calendar holidayDate) {
		this.name = holidayName;
		this.date = holidayDate;
	}

	/**
	 * Gets the unique holidayID
	 * @return
	 */
	public long getHolidayId() {
		return holidayId;
	}
	
	/**
	 * Sets the unique holidayID
	 * @param holidayId
	 */
	public void setHolidayId(long holidayId) {
		this.holidayId = holidayId;
	}
	
	/**
	 * Get the name of the holiday
	 * @return
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Sets the name of the holiday
	 * @param holidayname
	 */
	public void setName(String holidayname)
	{
		this.name = holidayname;
	}
	
	/**
	 * Gets the date of the holiday
	 * @return
	 */
	public Calendar getDate() {
		return date;
	}
	
	/**
	 * Sets the date of the holiday
	 * @param date
	 */
	public void setDate(Calendar date) {
		this.date = date;
	}
	
	
	 /**
     * Erases this holiday.
     * 
     * @return <code>true</code> if this holiday has been successfully deleted
     */
	public boolean delete()
	{
		return HolidayDaoHelper.getHolidayDao().delete(this);
	}
	
	/**
     * Saves this holiday.
     * 
     * @return <code>true</code> if this holiday has successfully been saved
    */
	public boolean persist()
	{
		return HolidayDaoHelper.getHolidayDao().persistHoliday(this);
	}
	
	/**
     * Gets a holiday from an identifying string.
     * 
     * @param   idString    a string containing either the holiday's identifier or 
     *                      name
     * @return              the holiday, if it's been found or<br>
     *                      <code>null</code> otherwise
     */
    public static Holiday getFromIdString(String idString) {

        if (StringTools.isNullOrEmpty(idString)) {
            throw new IllegalArgumentException(
                   "Holiday identifier string can't be null or empty.");
        }
        return HolidayDaoHelper.getHolidayDao().getHolidayFromIdString(idString);
    }

}
