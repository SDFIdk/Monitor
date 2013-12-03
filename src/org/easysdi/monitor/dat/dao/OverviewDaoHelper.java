/**
 * 
 */
package org.easysdi.monitor.dat.dao;

/**
 * @author BERG3428
 *
 */
public class OverviewDaoHelper {

	private static IOverviewDao overviewDao;
	
	 /**
     * Dummy constructor to prevent instantiation.
     */
    private OverviewDaoHelper() {
        
        throw new UnsupportedOperationException(
                "This class can't be instantiated.");
        
    }



    /**
     * Defines which overviewpage data access object must be used.
     * 
     * @param   newOverviewDao   the <code>{@link IOverviewDao}</code> implementation to 
     *                      use
     */
    public static void setOverviewDao(IOverviewDao newOverviewDao) {
    	OverviewDaoHelper.overviewDao = newOverviewDao;
    }



    /**
     * Gets the overviewpage data access object which must be used.
     * 
     * @return  the <code>{@link IOverviewDao}</code> implementation to use
     */
    public static IOverviewDao getOverviewDao() {
        return OverviewDaoHelper.overviewDao;
    }

}
