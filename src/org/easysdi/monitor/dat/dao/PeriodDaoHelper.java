/**
 * 
 */
package org.easysdi.monitor.dat.dao;

/**
 * @author berg3428
 *
 */
public class PeriodDaoHelper {

	private static IPeriodDao periodDao;
	
	/**
	* Dummy constructor to prevent instantiation.
    */
	public PeriodDaoHelper() {
		  throw new UnsupportedOperationException(
          "This class can't be instantiated.");
	}

	public static IPeriodDao getPeriodDao() {
		return periodDao;
	}

	public static void setPeriodDao(IPeriodDao periodDao) {
		PeriodDaoHelper.periodDao = periodDao;
	}
	
}
