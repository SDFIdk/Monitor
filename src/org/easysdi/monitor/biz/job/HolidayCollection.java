/**
 * 
 */
package org.easysdi.monitor.biz.job;

import java.util.List;

import org.easysdi.monitor.dat.dao.HolidayDaoHelper;


/**
 * Class for HolidayCollection  
 * <p>   
 * 
 * @author Thomas Berstedt - arx iT
 * @version 1.0, 2011-04-14

 */
public class HolidayCollection {

	/**
	 * 
	 */
	public HolidayCollection() {
		// TODO Auto-generated constructor stub
	}
	
	/**
     * Gets list of all sla.
     * <p>
     * @return a list containing the sla
     */
    public List<Holiday> getHoliday() {
       return HolidayDaoHelper.getHolidayDao().getAllHoliday();
    }
}
