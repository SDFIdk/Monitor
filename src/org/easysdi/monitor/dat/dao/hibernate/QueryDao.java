package org.easysdi.monitor.dat.dao.hibernate;

import org.easysdi.monitor.biz.job.Query;
import org.easysdi.monitor.biz.job.QueryParam;
import org.easysdi.monitor.dat.dao.IQueryDao;
import org.easysdi.monitor.dat.dao.QueryDaoHelper;
import org.hibernate.SessionFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Provides query persistance operations through Hibernate.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
public class QueryDao extends HibernateDaoSupport implements IQueryDao {

    /**
     * Creates a new query data access object.
     * 
     * @param   sessionFactory  the Hibernate session factory object
     */
    public QueryDao(SessionFactory sessionFactory) {
        this.setSessionFactory(sessionFactory);
        QueryDaoHelper.setQueryDao(this);
    }



    /**
     * {@inheritDoc}
     */
    public Query getQuery(long queryId) {

        if (1 > queryId) {
            throw new IllegalArgumentException("Invalid query identifier");
        }
        try
        {
        	return this.getHibernateTemplate().load(Query.class, queryId);
        }catch(Exception ex)
        {
        	System.out.println("Fejl: "+ex.getMessage());
        }
        return null;
    }



    /**
     * {@inheritDoc}
     */
    public boolean persistQuery(Query query) {

        if (null == query) {
            throw new IllegalArgumentException("Query can't be null");
        }

        if (!query.isValid()) {
            throw new IllegalStateException("Can't persist an invalid query");
        }

        try {
            this.getHibernateTemplate().saveOrUpdate(query);
            return true;

        } catch (DataAccessException e) {
            return false;
        }
    }



    /**
     * {@inheritDoc}
     */
    public boolean deleteQuery(Query query) {

        if (null == query) {
            throw new IllegalArgumentException("Query can't be null");
        }

        try {
            this.getHibernateTemplate().delete(query);
            return true;
        } catch (DataAccessException e) {
            return false;
        }
    }



    /**
     * {@inheritDoc}
     */
    public boolean deleteQueryParam(QueryParam queryParam) {

        if (null == queryParam) {
            throw new IllegalArgumentException("Query param can't be null");
        }

        try {
            this.getHibernateTemplate().delete(queryParam);
            return true;
        } catch (DataAccessException e) {
            return false;
        }

    }
}
