package org.easysdi.monitor.dat.dao.hibernate;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Provides low-level data access functions through Hibernate.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 */
abstract class AbstractDao extends HibernateDaoSupport {
    
    private final Logger logger = Logger.getLogger(AbstractDao.class);
    
    /**
     * Saves or updates an object.
     * 
     * @param   object  the bound object
     * @return          <code>true</code> if the object was successfully 
     *                  persisted
     */
    protected boolean persistObject(Object object) {
        
        if (null == object) {
            throw new IllegalArgumentException(
                    "Null object can't be persisted.");
        }
        
        try {
        		//this.getHibernateTemplate().setAlwaysUseNewSession(true);
        		this.getHibernateTemplate().saveOrUpdate(object);
        		this.getHibernateTemplate().flush();
     
        	return true;

        } catch (DataAccessException e) {
            this.logger.error(
                  "An error occurred while an object was persisted.", 
                  e);
            return false;
        }catch(Exception e)
        {
        	return false;
        }
    }
    
    
    
    /**
     * Erases an object.
     * 
     * @param   object  the bound object
     * @return          <code>true</code> if the object was successfully 
     *                  deleted
     */
    protected boolean deleteObject(Object object) {

        if (null == object) {
            throw new IllegalArgumentException(
                    "Null object can't be deleted.");
        }

        try {
            this.getHibernateTemplate().delete(object);
            
            return true;

        } catch (DataAccessException e) {
            this.logger.error(
                 "An error occurred while an object was deleted.",
                 e);
            return false;
        }
    }
    
    
    
    /**
     * Fetches an object from its identifier.
     * 
     * @param   <T>             the class of the object to fetch
     * @param   objectClass     the class of the object to fetch
     * @param   id              the identifier for the object        
     * @return                  the object if it's been found or<br>
     *                          <code>null</code> otherwise
     */
    protected <T> T loadObjectFromId(Class<T> objectClass, Serializable id) {
        
        try {
            return this.getHibernateTemplate().load(objectClass, id);

        } catch (DataAccessException e) {
            this.logger.error(
                 "An error occurred while an object was loaded.", 
                 e);
            return null;
        }
    }

}
