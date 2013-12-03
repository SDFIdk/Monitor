/**
 * 
 */
package org.easysdi.monitor.dat.dao.hibernate;

import org.easysdi.monitor.biz.job.QueryTestResult;
import org.easysdi.monitor.dat.dao.IQueryTestResultDao;
import org.easysdi.monitor.dat.dao.QueryTestResultDaoHelper;

import org.hibernate.SessionFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * @author berg3428
 *
 */
public class QueryTestResultDao extends HibernateDaoSupport implements IQueryTestResultDao {

	/**
	 * 
	 */
	public QueryTestResultDao(SessionFactory sessionFactory) {
		this.setSessionFactory(sessionFactory);
		QueryTestResultDaoHelper.setQueryTestResultDao(this);
	}

	public boolean delete(QueryTestResult result) {
		
		if (null == result) {
            throw new IllegalArgumentException("Querytestresult can't be null");
        }

        try {
            this.getHibernateTemplate().delete(result);
            return true;
        } catch (DataAccessException e) {
            return false;
        }
	}
	
	private QueryTestResult getqueryTestResultById(long searchedQueryTestResultId) {
		 if (1 > searchedQueryTestResultId) {
			  throw new IllegalArgumentException("Invalid period identifier");
		  }
	      return this.getHibernateTemplate().get(QueryTestResult.class, searchedQueryTestResultId);
		
	}
	
	public QueryTestResult getFromIdString(String idString) {
		 try {
	            final long queryId = Long.parseLong(idString);
	            return this.getqueryTestResultById(queryId);
	        } catch (NumberFormatException e) {
				  throw new IllegalArgumentException("Invalid queryid identifier only ID search allowed");
	        }
	}

	public boolean persistResult(QueryTestResult result) {
		
	    if (null == result) {
            throw new IllegalArgumentException("Query test result can't be null");
        }
        
        try {
        	//this.getSession().setFlushMode(FlushMode.COMMIT);
            this.getHibernateTemplate().saveOrUpdate(result);
            return true;

        } catch (DataAccessException e) {
            return false;
        }
	}
}
