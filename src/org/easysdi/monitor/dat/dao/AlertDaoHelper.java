package org.easysdi.monitor.dat.dao;

/**
 * Links to the <code>IAlertDao</code> implementation which must be used to 
 * access the alerts data.
 * 
 * @author  Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 * @see     IAlertDao
 */
public class AlertDaoHelper {

    private static IAlertDao alertDao;
    
    
    
    /**
     * Dummy constructor to prevent instantiation.
     */
    private AlertDaoHelper() {
        
        throw new UnsupportedOperationException(
                "This class can't be instantiated.");
        
    }



    /**
     * Defines which alert data access object must be used.
     * 
     * @param   newAlertDao the <code>{@link IAlertDao}</code> implementation 
     *                      to use
     */
    public static void setAlertDao(IAlertDao newAlertDao) {
        AlertDaoHelper.alertDao = newAlertDao;
    }



    /**
     * Gets the alert data access object which must be used.
     * 
     * @return  the <code>{@link IAlertDao}</code> implementation to use
     */
    public static IAlertDao getDaoObject() {
        return AlertDaoHelper.alertDao;
    }

}
