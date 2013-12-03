package org.easysdi.monitor.dat.dao;

/**
 * Links to the <code>IUserDao</code> implementation which must be used to 
 * access the user data.
 * 
 * @author  Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 * @see     IUserDao
 */
public class UserDaoHelper {

    private static IUserDao userDao;
    
    
    
    /**
     * Dummy constructor to prevent instantiation.
     */
    private UserDaoHelper() {
        throw new UnsupportedOperationException(
                "This class can't be instantiated.");
    }



    /**
     * Defines which user data access object must be used.
     * 
     * @param   newUserDao  the <code>{@link IUserDao}</code> implementation to 
     *                      use
     */
    public static void setUserDao(IUserDao newUserDao) {
        UserDaoHelper.userDao = newUserDao;
    }



    /**
     * Gets the user data access object which must be used.
     * 
     * @return  the <code>{@link IUserDao}</code> implementation to use
     */
    public static IUserDao getUserDao() {
        return UserDaoHelper.userDao;
    }

}
