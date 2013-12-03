package org.easysdi.monitor.dat.dao;

public class OverviewQueryDaoHelper {

	private static IOverviewQueryDao overviewQueryDao;
	
	 /**
    * Dummy constructor to prevent instantiation.
    */
   private OverviewQueryDaoHelper() {
       
       throw new UnsupportedOperationException(
               "This class can't be instantiated.");
       
   }



   /**
    * Defines which overviewQuery data access object must be used.
    * 
    * @param   newOverviewQueryDao   the <code>{@link IOverviewDao}</code> implementation to 
    *                      use
    */
   public static void setOverviewQueryDao(IOverviewQueryDao newOverviewQueryDao) {
   	OverviewQueryDaoHelper.overviewQueryDao = newOverviewQueryDao;
   }



   /**
    * Gets the overviewQuery data access object which must be used.
    * 
    * @return  the <code>{@link IOverviewQueryDao}</code> implementation to use
    */
   public static IOverviewQueryDao getOverviewQueryDao() {
       return OverviewQueryDaoHelper.overviewQueryDao;
   }
}
