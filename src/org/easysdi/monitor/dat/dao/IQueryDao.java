package org.easysdi.monitor.dat.dao;

import org.easysdi.monitor.biz.job.Query;
import org.easysdi.monitor.biz.job.QueryParam;

/**
 * Provides query persistance operations.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
public interface IQueryDao {

    /**
     * Erases a query.
     * 
     * @param   query   the query
     * @return          <code>true</code> if the query was successfully deleted
     */
    boolean deleteQuery(Query query);



    /**
     * Fetches a query from its identifier.
     * 
     * @param   queryId the long identifying a query
     * @return          the query if it's been found, or<br>
     *                  <code>null</code> otherwise
     */
    Query getQuery(long queryId);



    /**
     * Saves a query.
     * 
     * @param   query   the query
     * @return          <code>true</code> if the query has been successfully 
     *                  persisted
     */
    boolean persistQuery(Query query);



    /**
     * Erases a query parameter.
     * 
     * @param   queryParam  the query parameter
     * @return              <code>true</code> if the query parameter was 
     *                      successfully deleted
     */
    boolean deleteQueryParam(QueryParam queryParam);

}
