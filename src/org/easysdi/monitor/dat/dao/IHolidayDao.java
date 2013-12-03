/**
 * 
 */
package org.easysdi.monitor.dat.dao;

import java.util.List;

import org.easysdi.monitor.biz.job.Holiday;


/**
 * @author berg3428
 *
 */
public interface IHolidayDao {

	List<Holiday> getAllHoliday();
	
	boolean delete(Holiday holiday);
	
	boolean persistHoliday(Holiday holiday);
	
	Holiday getHolidayByName(String searchedHolidayName);

	Holiday getHolidayById(long searchedHolidayId);

	Holiday getHolidayFromIdString(String idString);
}
