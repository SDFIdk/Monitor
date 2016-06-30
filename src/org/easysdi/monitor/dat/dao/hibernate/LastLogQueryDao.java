package org.easysdi.monitor.dat.dao.hibernate;

import org.easysdi.monitor.biz.logging.LastLog;
import org.easysdi.monitor.dat.dao.ILastLogQueryDao;
import org.easysdi.monitor.dat.dao.LastLogQueryDaoHelper;
import org.hibernate.SessionFactory;
import org.springframework.dao.DataAccessException;

public class LastLogQueryDao extends AbstractDao implements ILastLogQueryDao{

	/**
     * Creates a new last log data access object. 
     * 
     * @param   sessionFactory  the Hibernate session factory
     */
    public LastLogQueryDao(SessionFactory sessionFactory) {
        this.setSessionFactory(sessionFactory);
        LastLogQueryDaoHelper.setLastLogQuery(this);
    }
	
	public boolean delete(LastLog lastlog) {
		return this.deleteObject(lastlog);
	}
	
    /**
     * {@inheritDoc}
     */
    public LastLog getLastlogByQueryId(long queryId) {

        if (1 > queryId) {
            throw new IllegalArgumentException("Invalid query identifier");
        }
        try
        {
        	return this.getHibernateTemplate().get(LastLog.class, queryId);
        }catch(Exception ex)
        {
        	System.out.println("Fejl: "+ex.getMessage());
        }
        return null;
    }

	public boolean create(LastLog lastlog) {
	    if (null == lastlog) {
            throw new IllegalArgumentException("Lastlog can't be null");
        }
	    
        try {
        	this.getHibernateTemplate().saveOrUpdate(lastlog);
            return true;
        } catch (DataAccessException e) {
            return false;
        }catch(Exception e)
        {
        	return false;
        }
	}
	
}
