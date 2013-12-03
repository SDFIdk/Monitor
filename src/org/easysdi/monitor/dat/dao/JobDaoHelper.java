package org.easysdi.monitor.dat.dao;

/**
 * Links to the <code>IJobDao</code> implementation which must be used to access
 * the job data.
 * 
 * @author  Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 * @see     IJobDao
 */
public class JobDaoHelper {

    private static IJobDao jobDao;
    
    
    
    /**
     * Dummy constructor to prevent instantiation.
     */
    private JobDaoHelper() {
        
        throw new UnsupportedOperationException(
                "This class can't be instantiated.");
        
    }



    /**
     * Defines which job data access object must be used.
     * 
     * @param   newJobDao   the <code>{@link IJobDao}</code> implementation to 
     *                      use
     */
    public static void setJobDao(IJobDao newJobDao) {
        JobDaoHelper.jobDao = newJobDao;
    }



    /**
     * Gets the job data access object which must be used.
     * 
     * @return  the <code>{@link IJobDao}</code> implementation to use
     */
    public static IJobDao getJobDao() {
        return JobDaoHelper.jobDao;
    }

}
