package org.easysdi.monitor.dat.dao.hibernate;

import java.util.List;

import org.easysdi.monitor.biz.alert.Alert;
import org.easysdi.monitor.dat.dao.AlertDaoHelper;
import org.easysdi.monitor.dat.dao.IAlertDao;


import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;


import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;


/**
 * Provides alert persistance operations through Hibernate.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
public class AlertDao extends HibernateDaoSupport implements IAlertDao {

    /**
     * Creates a new alert data access object.
     * 
     * @param   sessionFactory  the Hibernate session factory object
     */
    public AlertDao(SessionFactory sessionFactory) {
        this.setSessionFactory(sessionFactory);
        AlertDaoHelper.setAlertDao(this);
    }



    /**
     * {@inheritDoc}
     */
    public void persist(Alert alert) {
    	try
    	{
    		if (null == alert) {
               throw new IllegalArgumentException(
                       "Null object can't be persisted.");
    		}
    		//this.getHibernateTemplate().setAlwaysUseNewSession(true);
    		this.getHibernateTemplate().saveOrUpdate(alert);
    		//this.getHibernateTemplate().flush();

    	} catch (DataAccessException e) {
    		this.logger.error(
    				"An error occurred while an alert was persisted.", 
    				e);
    		System.out.println(e.getMessage());
    	}catch(Exception e)
    	{
    		System.out.println(e.getMessage());
    	}    
    }



    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<Alert> getAlertsForJob(long jobId, boolean onlyRss) {

        final DetachedCriteria search = DetachedCriteria.forClass(Alert.class);

        search.add(Restrictions.eq("parentJob.jobId", jobId));

        if (onlyRss) {
            search.add(Restrictions.eq("exposedToRss", true));
        }

        return this.getHibernateTemplate().findByCriteria(search);
    }


    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
	public List<Alert> getAlertsForJob(long jobId, boolean onlyRss,
			Integer start, Integer limit, String sortField, String direction) {
    	final DetachedCriteria search = DetachedCriteria.forClass(Alert.class);

        search.add(Restrictions.eq("parentJob.jobId", jobId));

        if (onlyRss) {
            search.add(Restrictions.eq("exposedToRss", true));
        }
        if((!sortField.equals("")) && (!direction.equals(""))){
        	
        	if(sortField.equalsIgnoreCase("dateTime"))
        		sortField = "time";
        	if(sortField.equalsIgnoreCase("jobId"))
        		sortField = "parentJob.jobId";
        		
        	if(direction.equals("ASC"))
        		search.addOrder(Order.asc(sortField)); 
        	else if(direction.equals("DESC"))
        		search.addOrder(Order.desc(sortField)); 
        	else
        	{}
        	
        }
        
        search.getExecutableCriteria(this.getSession()).setMaxResults(limit).setFirstResult(start);
        return this.getHibernateTemplate().findByCriteria(search);
	}
    
    
    /**
     * {@inheritDoc}
     */
    public Alert getAlertById(long alertId) {

        if (1 > alertId) {
            throw new IllegalArgumentException("Invalid alert identifier");
        }

        return this.getHibernateTemplate().get(Alert.class, alertId);
    }
    
    /**
     * {@inheritDoc}
     */
    public Alert getAlertFromIdString(String identifyString) {

        try {
            final long alertId = Long.parseLong(identifyString);

            return this.getAlertById(alertId);

        } catch (NumberFormatException e) {

            return null;
        }
    }
    



	@SuppressWarnings("unchecked")
	public List<Alert> getAlertsForAllJobs(boolean onlyRss, Integer start,
			Integer limit, String sortField, String direction) {
		final DetachedCriteria search = DetachedCriteria.forClass(Alert.class);



        if (onlyRss) {
            search.add(Restrictions.eq("exposedToRss", true));
        }
        if((!sortField.equals("")) && (!direction.equals(""))){
        	
        	if(sortField.equalsIgnoreCase("dateTime"))
        		sortField = "time";
        	if(sortField.equalsIgnoreCase("jobId"))
        		sortField = "parentJob.jobId";
        		
        	if(direction.equals("ASC"))
        		search.addOrder(Order.asc(sortField)); 
        	else if(direction.equals("DESC"))
        		search.addOrder(Order.desc(sortField)); 
        	else
        	{}
        	
        }
        
        search.getExecutableCriteria(this.getSession()).setMaxResults(limit).setFirstResult(start);
        return this.getHibernateTemplate().findByCriteria(search);
	}



	@SuppressWarnings("unchecked")
	public List<Alert> getAlertsForAllJobs(boolean onlyRss) {
		 final DetachedCriteria search = DetachedCriteria.forClass(Alert.class);
	     

	        if (onlyRss) {
	            search.add(Restrictions.eq("exposedToRss", true));
	        }

	        return this.getHibernateTemplate().findByCriteria(search);
	}
}
