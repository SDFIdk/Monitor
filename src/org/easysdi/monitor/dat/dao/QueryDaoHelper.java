package org.easysdi.monitor.dat.dao;

/**
 * Links to the <code>IQueryDao</code> implementation which must be used to 
 * access the query data.
 * 
 * @author  Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 * @see     IQueryDao
 */
public class QueryDaoHelper {

    private static IQueryDao queryDao;
   
    /**
     * Dummy constructor to prevent instantiation.
     */
    private QueryDaoHelper() {    
        throw new UnsupportedOperationException(
            "This class can't be instantiated.");
    }



    /**
     * Defines which query data access object must be used.
     * 
     * @param   newQueryDao the <code>{@link IQueryDao}</code> implementation 
     *                      to use
     */
    public static void setQueryDao(IQueryDao newQueryDao) {
        QueryDaoHelper.queryDao = newQueryDao;
    }



    /**
     * Gets the query data access object which must be used.
     * 
     * @return  the <code>{@link IQueryDao}</code> implementation to use
     */
    public static IQueryDao getQueryDao() {
        return QueryDaoHelper.queryDao;
    }

}
