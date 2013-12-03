package org.easysdi.monitor.dat.dao;

public class QueryValidationSettingsDaoHelper {
	private static IQueryValidationSettingsDao dao;

    private QueryValidationSettingsDaoHelper() {
        
        throw new UnsupportedOperationException(
            "This class can't be instantiated.");
        
    }

	public static void setDao(IQueryValidationSettingsDao dao) {
		QueryValidationSettingsDaoHelper.dao = dao;
	}

	public static IQueryValidationSettingsDao getDao() {
		return dao;
	}
}
