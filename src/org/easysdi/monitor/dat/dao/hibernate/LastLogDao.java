/**
 * 
 */
package org.easysdi.monitor.dat.dao.hibernate;


import java.util.LinkedList;
import java.util.List;

import org.easysdi.monitor.biz.job.OverviewLastQueryResult;

import org.easysdi.monitor.dat.dao.ILastLogDao;
import org.easysdi.monitor.dat.dao.LastLogDaoHelper;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateTemplate;


/**
 * @author berg3428
 *
 */

public class LastLogDao extends AbstractDao implements ILastLogDao {

	/**
     * Creates a new last log data access object. 
     * 
     * @param   sessionFactory  the Hibernate session factory
     */
    public LastLogDao(SessionFactory sessionFactory) {
        this.setSessionFactory(sessionFactory);
        LastLogDaoHelper.setLastLogDao(this);
    }
    
    /**
    * Deletes a last_query_request
    * @param lastresult
    * @return
    */
    public boolean delete(OverviewLastQueryResult lastresult)
    {
    	 return this.deleteObject(lastresult);
    }
    
    /**
    * Creates a new last_query_result
    * @param lastresult
    * @return
    */
    public boolean create(OverviewLastQueryResult lastresult)
    {
	       if (null == lastresult) {
	            throw new IllegalArgumentException("Query lastresult can't be null");
	        }

	        try {
	        	this.getHibernateTemplate().saveOrUpdate(lastresult);
	            return true;
	        } catch (DataAccessException e) {
	            return false;
	        }catch(Exception e)
	        {
	        	return false;
	        }
    }
    
    /**
     *  Checks that a request already have a lastqueryrecord
     * @param lastresult
     * @return
     */
    public OverviewLastQueryResult exist(long queryID)
    {
    	final DetachedCriteria search = DetachedCriteria.forClass(OverviewLastQueryResult.class);
    	search.add(Restrictions.eq("queryid",queryID));
    	
    	 final HibernateTemplate hibernateTemplate = this.getHibernateTemplate();
    	 final List<OverviewLastQueryResult> result = this.typeLastQueryResultList(hibernateTemplate.findByCriteria(search)); 
    	 if(result.isEmpty())
    	 {
    		 return null;
    	 }
    	return result.get(0);
    }
    
    /**
     * Converts an Hibernate result into a strongly-typed job results list.
     * 
     * @param   resultList  the job results list returned by Hibernate
     * @return              the strongly-typed result list
     */
    private List<OverviewLastQueryResult> typeLastQueryResultList(List<?> resultList) {
        final List<OverviewLastQueryResult> lastQueryFound = new LinkedList<OverviewLastQueryResult>();

        for (Object lastQueryObject : resultList) {

            if (lastQueryObject instanceof OverviewLastQueryResult) {
                lastQueryFound.add((OverviewLastQueryResult) lastQueryObject);
            }
        }
        return lastQueryFound;
    }
}
