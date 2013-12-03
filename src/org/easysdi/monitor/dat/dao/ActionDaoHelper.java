package org.easysdi.monitor.dat.dao;

/**
 * Links to the <code>IActionDao</code> implementation which must be used to 
 * access the actions data.
 * 
 * @author  Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 * @see     IActionDao
 */
public class ActionDaoHelper {

    private static IActionDao actionDao;
    
    
    
    /**
     * Dummy constructor to prevent instantiation.
     */
    private ActionDaoHelper() {

        throw new UnsupportedOperationException(
                "This class can't be instantiated.");
        
    }



    /**
     * Defines the action data access object to be used.
     * 
     * @param   newActionDao    the <code>{@link IActionDao}</code> 
     *                          implementation to use
     */
    public static void setActionDao(IActionDao newActionDao) {
        ActionDaoHelper.actionDao = newActionDao;
    }



    /**
     * Gets the action data access object to use.
     * 
     * @return  the <code>{@link IActionDao}</code> implementation
     */
    public static IActionDao getActionDao() {
        return ActionDaoHelper.actionDao;
    }

}
