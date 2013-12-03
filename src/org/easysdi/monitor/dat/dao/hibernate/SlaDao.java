/**
 * 
 */
package org.easysdi.monitor.dat.dao.hibernate;

import java.util.LinkedList;
import java.util.List;

import org.easysdi.monitor.biz.job.Sla;
import org.easysdi.monitor.dat.dao.ISlaDao;
import org.easysdi.monitor.dat.dao.SlaDaoHelper;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * @author berg3428
 *
 */
public class SlaDao extends HibernateDaoSupport implements ISlaDao{
	
	// TODO
	private TransactionTemplate txTemplate;
	
	/**
     * Creates a new job persistance data access object.
     * 
     * @param   sessionFactory  the Hibernate session factory object
     */
    public SlaDao(SessionFactory sessionFactory) {
        this.setSessionFactory(sessionFactory);
        SlaDaoHelper.setSlaDao(this);
    }
	
	/**
     * Defines the transaction manager for the persistence operations. 
     * 
     * @param   txManager  the Spring transaction manager for Hibernate
     */
    public void setTxManager(PlatformTransactionManager txManager) {
        this.txTemplate = new TransactionTemplate(txManager);
    }
    
    /**
     * Gets the transaction template for the persistence operations. 
     * 
     * @return   txTemplate  the Spring transaction template for Hibernate
     */
    public TransactionTemplate getTxTemplate() {
        return this.txTemplate;
    }
	
    /**
    * {@inheritDoc}
    */
    public boolean persistSla(Sla sla) {
        if (null == sla) {
            throw new IllegalArgumentException("Sla can't be null");
        }
        try {
            this.getHibernateTemplate().saveOrUpdate(sla);
            return true;
        } catch (DataAccessException e) {
            return false;
        }
    }
	
    /**
    * {@inheritDoc}
    */
    public boolean delete(Sla sla) {

        try {
            this.getHibernateTemplate().delete(sla);
            return true;

        } catch (DataAccessException e) {
            return false;
        }
    }

	
    /**
     * {@inheritDoc}
     */
    public List<Sla> getAllSla() {
        final HibernateTemplate hibernateTemplate = this.getHibernateTemplate();        
        return this.typeSlaResultList(hibernateTemplate.loadAll(Sla.class));
    }
    
    /**
     * Converts an Hibernate result into a strongly-typed Overview results list.
     * 
     * @param   resultList  the Overview results list returned by Hibernate
     * @return              the strongly-typed result list
     */
    private List<Sla> typeSlaResultList(List<?> resultList) {
        final List<Sla> slaList = new LinkedList<Sla>();
        for (Object slaObject : resultList) {
            if (slaObject instanceof Sla) {
            	slaList.add((Sla) slaObject);
            }
        }
        return slaList;
    }
    
    /**
     *  Finds Sla identified by name or id
     */
	public Sla getSlaFromIdString(String idString) {
		 try {
	            final long slaId = Long.parseLong(idString);
	            return this.getSlaById(slaId);
	        } catch (NumberFormatException e) {

	            return this.getSlaByName(idString);
	        }
	}

	public Sla getSlaById(long searchedSlaId) {
	  if (1 > searchedSlaId) {
		  throw new IllegalArgumentException("Invalid sla identifier");
	  }
      return this.getHibernateTemplate().get(Sla.class, searchedSlaId);
	}
	
	@SuppressWarnings("unchecked")
	public Sla getSlaByName(String searchedSlaName) {		
		if (null == searchedSlaName || searchedSlaName.equals("")) {
            throw new IllegalArgumentException("Sla name can't be null or empty");
        }

        final DetachedCriteria search = DetachedCriteria.forClass(Sla.class);
        search.add(Restrictions.eq("name", searchedSlaName));
        final List<Sla> result = this.getHibernateTemplate().findByCriteria(search);

        if (null == result || 1 > result.size()) {
        	throw new IllegalArgumentException("Found zero or more then one Sla with the name");
        }
        return result.get(0);
	}
	
}
