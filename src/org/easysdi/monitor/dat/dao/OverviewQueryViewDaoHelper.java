package org.easysdi.monitor.dat.dao;

public class OverviewQueryViewDaoHelper {
	private static IOverviewQueryViewDao overviewQueryViewDao;
	
  private OverviewQueryViewDaoHelper() {
      
      throw new UnsupportedOperationException(
              "This class can't be instantiated.");
      
  }

  public static void setOverviewQueryViewDao(IOverviewQueryViewDao newOverviewQueryViewDao) {
  	OverviewQueryViewDaoHelper.overviewQueryViewDao = newOverviewQueryViewDao;
  }

  public static IOverviewQueryViewDao getOverviewQueryViewDao() {
      return OverviewQueryViewDaoHelper.overviewQueryViewDao;
  }

}
