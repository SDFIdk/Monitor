/**
 * 
 */
package org.easysdi.monitor.dat.dao;

/**
 * @author berg3428
 *
 */
public class QueryTestResultDaoHelper {

	private static IQueryTestResultDao queryTestResultDao;
	
	/**
	 * Dummy constructor
	 */
	private QueryTestResultDaoHelper() {
		  throw new UnsupportedOperationException(
          "This class can't be instantiated.");
	}

	public static IQueryTestResultDao getQueryTestResultDao() {
		return queryTestResultDao;
	}

	public static void setQueryTestResultDao(IQueryTestResultDao queryTestResultDao) {
		QueryTestResultDaoHelper.queryTestResultDao = queryTestResultDao;
	}
	
	
}
