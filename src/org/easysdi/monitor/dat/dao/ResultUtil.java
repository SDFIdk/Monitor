package org.easysdi.monitor.dat.dao;

import java.util.List;

/**
 * Provides helper methods to process data search results.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
public class ResultUtil {

    /**
     * Dummy constructor to prevent instantiation.
     */
    private ResultUtil() {
        throw new UnsupportedOperationException(
                "This class can't be instantiated.");
    }
    
    
    
    /**
     * Ensures that a search returns only one result.
     * 
     * @param   resultList  a list containing the found objects
     * @return              the object found if there was only one or the first
     *                      one if there were more
     */
    public static Object uniqueResult(List<?> resultList) {

        if (null != resultList && 0 < resultList.size()) {
            return resultList.get(0);
        }

        return null;
    }
}
