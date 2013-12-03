package org.easysdi.monitor.dat.dao;

/**
 * Links to the <code>IServiceDao</code> implementation which must be used to 
 * access the service data.
 * 
 * @author  Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 * @see     IServiceDao
 */
public class ServiceDaoHelper {

    private static IServiceDao serviceDao;
    
    
    
    /**
     * Dummy constructor to prevent instantiation.
     */
    private ServiceDaoHelper() {
        
        throw new UnsupportedOperationException(
                "This class can't be instantiated.");
    }



    /**
     * Defines which service data access object must be used.
     * 
     * @param   newServiceDao   the <code>{@link IServiceDao}</code> 
     *                          implementation to use
     */
    public static void setServiceDao(IServiceDao newServiceDao) {
        ServiceDaoHelper.serviceDao = newServiceDao;
    }



    /**
     * Gets the service data access object which must be used.
     * 
     * @return  the <code>{@link IServiceDao}</code> implementation to use
     */
    public static IServiceDao getServiceDao() {
        return ServiceDaoHelper.serviceDao;
    }

}
