/**
 * 
 */
package org.easysdi.monitor.dat.dao;

/**
 * @author berg3428
 *
 */
public class SlaDaoHelper {

	private static ISlaDao slaDao;
	
	/**
	 *  Dummy constructor to prevent instantiation.
	 */
	private SlaDaoHelper() {
		  throw new UnsupportedOperationException(
          "This class can't be instantiated.");
	}
	
	public static ISlaDao getSlaDao() {
		return slaDao;
	}

	public static void setSlaDao(ISlaDao slaDao) {
		SlaDaoHelper.slaDao = slaDao;
	}
}
