/**
 * 
 */
package org.easysdi.monitor.dat.dao.hibernate;

import java.util.LinkedList;
import java.util.List;

import org.easysdi.monitor.biz.job.Overview;
import org.easysdi.monitor.dat.dao.IOverviewDao;
import org.easysdi.monitor.dat.dao.OverviewDaoHelper;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
//import org.hibernate.criterion.DetachedCriteria;
//import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * @author BERG3428
 *
 */
public class OverviewDao extends HibernateDaoSupport implements IOverviewDao {

	private TransactionTemplate txTemplate;
		
	/**
     * Creates a new job persistance data access object.
     * 
     * @param   sessionFactory  the Hibernate session factory object
     */
    public OverviewDao(SessionFactory sessionFactory) {
        this.setSessionFactory(sessionFactory);
        OverviewDaoHelper.setOverviewDao(this);
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
    public List<Overview> getAllOverviews() {
        final HibernateTemplate hibernateTemplate = this.getHibernateTemplate();        
        return this.typeOverviewResultList(hibernateTemplate.loadAll(Overview.class));
    }
    
    /**
     * Converts an Hibernate result into a strongly-typed Overview results list.
     * 
     * @param   resultList  the Overview results list returned by Hibernate
     * @return              the strongly-typed result list
     */
    private List<Overview> typeOverviewResultList(List<?> resultList) {
        final List<Overview> overviewsFound = new LinkedList<Overview>();

        for (Object overviewObject : resultList) {

            if (overviewObject instanceof Overview) {
            	overviewsFound.add((Overview) overviewObject);
            }
        }

        return overviewsFound;
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean delete(Overview aPage) {

        try {
            this.getHibernateTemplate().delete(aPage);

            return true;

        } catch (DataAccessException e) {
            return false;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean persistOverview(Overview overviewp) {

        if (null == overviewp) {
            throw new IllegalArgumentException("Overview can't be null");
        }

        /*
        if (!job.isValid(true)) {
            throw new IllegalStateException("Can't persist an invalid job");
        }*/

        try {
            this.getHibernateTemplate().saveOrUpdate(overviewp);
            return true;

        } catch (DataAccessException e) {
            return false;
        }
    }

	@SuppressWarnings("unchecked")
	public Overview getOverviewByName(String searchedOverviewName) {

        if (null == searchedOverviewName || searchedOverviewName.equals("")) {
            throw new IllegalArgumentException(
                                             "Overview name can't be null or empty");
        }

        final DetachedCriteria search 
        	= DetachedCriteria.forClass(Overview.class);
        search.add(Restrictions.eq("name", searchedOverviewName));
        final List<Overview> result 
        	= this.getHibernateTemplate().findByCriteria(search);

        if (null == result || 1 > result.size()) {
        	return null;
        }

        return result.get(0);
	}

	public Overview getOverviewById(long searchedOverviewId) {
        if (1 > searchedOverviewId) {
            throw new IllegalArgumentException("Invalid overview identifier");
        }

        // return (Overview) SessionUtil.getCurrentSession().load(Overview.class, searchedOverviewId);
        return this.getHibernateTemplate().get(Overview.class, searchedOverviewId);
	}

	public Overview getOverviewFromIdString(String idString) {
		//return this.getAllOverviews().get(0);
        try {
            final long overviewId = Long.parseLong(idString);

            return this.getOverviewById(overviewId);

        } catch (NumberFormatException e) {

            return this.getOverviewByName(idString);

        }
        
	}
	
}
