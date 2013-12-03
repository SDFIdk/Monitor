package org.easysdi.monitor.dat.dao;

/**
 * Links to the <code>IValueObjectDao</code> implementation which must be used 
 * to access the value objects data.
 * 
 * @author  Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 * @see     IValueObjectDao
 */
public class ValueObjectDaoHelper {

    private static IValueObjectDao valueObjectDao;
    
    
    
    /**
     * Dummy constructor to prevent instantiation.
     */
    private ValueObjectDaoHelper() {
        
        throw new UnsupportedOperationException(
                "This class can't be instantiated.");
        
    }



    /**
     * Defines which value object data access object must be used.
     * 
     * @param   newValueObjectDao   the <code>{@link IValueObjectDao}</code> 
     *                              implementation to use
     */
    public static void setValueObjectDao(IValueObjectDao newValueObjectDao) {
        ValueObjectDaoHelper.valueObjectDao = newValueObjectDao;
    }



    /**
     * Gets the value object data access object which must be used.
     * 
     * @return  the <code>{@link IValueObjectDao}</code> implementatation to use
     */
    public static IValueObjectDao getValueObjectDao() {
        return ValueObjectDaoHelper.valueObjectDao;
    }

}
