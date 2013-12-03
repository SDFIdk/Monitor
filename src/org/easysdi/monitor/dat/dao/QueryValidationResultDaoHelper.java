package org.easysdi.monitor.dat.dao;

public class QueryValidationResultDaoHelper {

	private static IQueryValidationResultDao dao;

    private QueryValidationResultDaoHelper() {  
        throw new UnsupportedOperationException(
            "This class can't be instantiated."); 
    }
	
	public static void setDao(IQueryValidationResultDao dao) {
		QueryValidationResultDaoHelper.dao = dao;
	}

	public static IQueryValidationResultDao getDao() {
		return dao;
	}
	
}
