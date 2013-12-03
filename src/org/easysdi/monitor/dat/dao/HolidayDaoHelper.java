/**
 * 
 */
package org.easysdi.monitor.dat.dao;

/**
 * @author berg3428
 *
 */
public class HolidayDaoHelper {
	
	private static IHolidayDao holidayDao;
	
	/**
    * Dummy constructor to prevent instantiation.
    */
	public HolidayDaoHelper() {
		  throw new UnsupportedOperationException(
          "This class can't be instantiated.");
	}

	public static IHolidayDao getHolidayDao() {
		return holidayDao;
	}

	public static void setHolidayDao(IHolidayDao holidayDao) {
		HolidayDaoHelper.holidayDao = holidayDao;
	}
	
}
