/**
 * 
 */
package org.easysdi.monitor.dat.dao;

/**
 * @author berg3428
 *
 */
public class LastLogDaoHelper {

	  private static ILastLogDao lastlogDao;
	  
	  /**
	     * Dummy constructor to prevent instantiation.
	     */
	 private LastLogDaoHelper() {
	        throw new UnsupportedOperationException(
	                "This class can't be instantiated.");
	        
	 }
	 
	    /**
	     * Defines which last log data access object must be used. 
	     * 
	     * @param newLastLogDao the <code>{@link ILastLogDao}</code> implementation to use
	     */
	    public static void setLastLogDao(ILastLogDao newLastLogDao) {
	        LastLogDaoHelper.lastlogDao = newLastLogDao;
	    }



	    /**
	     * Gets the last log data access object which must be used.
	     * 
	     * @return  the <code>{@link ILastLogDao}</code> implementation to use
	     */
	    public static ILastLogDao getLastLogDao() {
	        return LastLogDaoHelper.lastlogDao;
	    }
}
