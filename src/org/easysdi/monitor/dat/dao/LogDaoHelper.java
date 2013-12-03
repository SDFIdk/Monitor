package org.easysdi.monitor.dat.dao;

/**
 * Links to the <code>ILogDao</code> implementation which must be used to access
 * the logs data.
 * 
 * @author  Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 * @see     ILogDao
 */
public class LogDaoHelper {

    private static ILogDao logDao;
    
    
    
    /**
     * Dummy constructor to prevent instantiation.
     */
    private LogDaoHelper() {
        
        throw new UnsupportedOperationException(
                "This class can't be instantiated.");
        
    }



    /**
     * Defines which log data access object must be used. 
     * 
     * @param   newLogDao   the <code>{@link ILogDao}</code> implementation to 
     *                      use
     */
    public static void setLogDao(ILogDao newLogDao) {
        LogDaoHelper.logDao = newLogDao;
    }



    /**
     * Gets the log data access object which must be used.
     * 
     * @return  the <code>{@link ILogDao}</code> implementation to use
     */
    public static ILogDao getLogDao() {
        return LogDaoHelper.logDao;
    }

}
