package org.easysdi.monitor.dat.dao.hibernate;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.easysdi.monitor.biz.job.Job;
import org.easysdi.monitor.biz.job.JobDefaultParameter;
import org.easysdi.monitor.dat.dao.IJobDao;
import org.easysdi.monitor.dat.dao.JobDaoHelper;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * Provides job persistance operations through Hibernate.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
public class JobDao extends HibernateDaoSupport implements IJobDao {

    private TransactionTemplate txTemplate;



    /**
     * Creates a new job persistance data access object.
     * 
     * @param   sessionFactory  the Hibernate session factory object
     */
    public JobDao(SessionFactory sessionFactory) {
        this.setSessionFactory(sessionFactory);
        JobDaoHelper.setJobDao(this);
    }



    /**
     * Defines the transaction manager for the persistance operations. 
     * 
     * @param   txManager  the Spring transaction manager for Hibernate
     */
    public void setTxManager(PlatformTransactionManager txManager) {
        this.txTemplate = new TransactionTemplate(txManager);
    }



    /**
     * Gets the transaction template for the persistance operations. 
     * 
     * @return   txTemplate  the Spring transaction template for Hibernate
     */
    public TransactionTemplate getTxTemplate() {
        return this.txTemplate;
    }

 

    /**
     * {@inheritDoc}
     */
    public List<Job> findJobs(Boolean automatic, Boolean realTimeAllowed,
                    Boolean published, Boolean alertsEnabled) {

        // Criteria search =
        // SessionUtil.getCurrentSession().createCriteria(Job.class);
        final DetachedCriteria search = DetachedCriteria.forClass(Job.class);

        if (null != automatic) {
            search.add(Restrictions.eq("config.automatic", automatic));
        }

        if (null != realTimeAllowed) {
            search.add(Restrictions.eq("config.realTimeAllowed",
                                       realTimeAllowed));
        }

        if (null != published) {
            search.add(Restrictions.eq("config.published", published));
        }

        if (null != alertsEnabled) {
            search.add(Restrictions.eq("config.alertsActivated", 
                                       alertsEnabled));
        }       

     
        final HibernateTemplate hibernateTemplate = this.getHibernateTemplate();
  
        final List<Job> result 
            = this.typeJobResultList(hibernateTemplate.findByCriteria(search));

        if (null == result) {
            return new LinkedList<Job>();
        }         

        return result;
    }



    /**
     * {@inheritDoc}
     */
    public List<Job> findJobs(Boolean automatic, Boolean realTimeAllowed,
                    Boolean published, Boolean alertsEnabled, Integer pageStart, Integer pageLimit , String sortField, String direction) {

        // Criteria search =
        // SessionUtil.getCurrentSession().createCriteria(Job.class);
        final DetachedCriteria search = DetachedCriteria.forClass(Job.class);

        if (null != automatic) {
            search.add(Restrictions.eq("config.automatic", automatic));
        }

        if (null != realTimeAllowed) {
            search.add(Restrictions.eq("config.realTimeAllowed",
                                       realTimeAllowed));
        }

        if (null != published) {
            search.add(Restrictions.eq("config.published", published));
        }

        if (null != alertsEnabled) {
            search.add(Restrictions.eq("config.alertsActivated", 
                                       alertsEnabled));
        }
        
        
        
        if(sortField.equalsIgnoreCase("name"))
    		sortField = "config.jobName";
        else if(sortField.equalsIgnoreCase("lastStatusUpdate"))
    		sortField = "statusUpdateTime";
        else if(sortField.equalsIgnoreCase("serviceType"))
    		sortField = "config.serviceTypeId";
        else
        	sortField = "config."+sortField;
        
        if((!sortField.equals("")) && (!direction.equals(""))){
        	if(direction.equals("ASC"))
        		search.addOrder(Order.asc(sortField)); 
        	else if(direction.equals("DESC"))
        		search.addOrder(Order.desc(sortField)); 
        	else
        	{}
        	
        }
        	
        search.getExecutableCriteria(this.getSession()).setMaxResults(pageLimit).setFirstResult(pageStart);
     
        final HibernateTemplate hibernateTemplate = this.getHibernateTemplate();
  
        final List<Job> result 
            = this.typeJobResultList(hibernateTemplate.findByCriteria(search));

        if (null == result) {
            return new LinkedList<Job>();
        }         

        return result;
    }
    /**
     * {@inheritDoc}
     */
    public Job getJobById(long jobId) {

        if (1 > jobId) {
            throw new IllegalArgumentException("Invalid job identifier");
        }

        // return (Job) SessionUtil.getCurrentSession().load(Job.class, jobId);
        return this.getHibernateTemplate().get(Job.class, jobId);
    }



    /**
     * {@inheritDoc}
     */
    public Job getJobByName(String name) {

        if (null == name || name.equals("")) {
            throw new IllegalArgumentException(
                                             "Job name can't be null or empty");
        }

        final List<?> result 
            = this.getHibernateTemplate().findByNamedParam(
                  "from Job where config.jobName = :name", "name", name);

        if (null != result && 0 < result.size()) {
            return (Job) result.get(0);
        }

        return null;
    }



    /**
     * {@inheritDoc}
     */
    public List<Job> getAllJobs() {
        final HibernateTemplate hibernateTemplate = this.getHibernateTemplate();
        
        return this.typeJobResultList(hibernateTemplate.loadAll(Job.class));

    }
    

    /**
     * Converts an Hibernate result into a strongly-typed job results list.
     * 
     * @param   resultList  the job results list returned by Hibernate
     * @return              the strongly-typed result list
     */
    private List<Job> typeJobResultList(List<?> resultList) {
        final List<Job> jobsFound = new LinkedList<Job>();

        for (Object jobObject : resultList) {

            if (jobObject instanceof Job) {
                jobsFound.add((Job) jobObject);
            }
        }

        return jobsFound;
    }
    
  
    
    /**
     * {@inheritDoc}
     */
    public boolean persistJob(Job job) {

        if (null == job) {
            throw new IllegalArgumentException("Job can't be null");
        }

        if (!job.isValid(true)) {
            throw new IllegalStateException("Can't persist an invalid job");
        }

        try {
            this.getHibernateTemplate().saveOrUpdate(job);
            return true;

        } catch (DataAccessException e) {
            return false;
        }
    }


    /**
     * {@inheritDoc}
     */
    public boolean delete(Job aJob) {

        try {
            this.getHibernateTemplate().delete(aJob);

            return true;

        } catch (DataAccessException e) {
            return false;
        }
    }


    /**
     * {@inheritDoc}
     */
    public Map<String, JobDefaultParameter> getDefaultParams() {
        final List<?> queryResult 
            = this.getHibernateTemplate().find("from JobDefaultParameter");

        final Map<String, JobDefaultParameter> defaultParams 
            = new HashMap<String, JobDefaultParameter>();

        if (null != queryResult) {

            for (Object resultObject : queryResult) {

                if (resultObject instanceof JobDefaultParameter) {
                    final JobDefaultParameter param 
                        = (JobDefaultParameter) resultObject;
                    defaultParams.put(param.getParamName(), param);
                }
            }

        }

        return defaultParams;

    }



    /**
     * {@inheritDoc}
     */
    public Job getJobFromIdString(String identifyString) {

        try {
            final long jobId = Long.parseLong(identifyString);

            return this.getJobById(jobId);

        } catch (NumberFormatException e) {

            return this.getJobByName(identifyString);

        }
    }


    /**
     * {@inheritDoc}
     */
    public boolean persistJobDefault(JobDefaultParameter defaultParam) {

        try {
            this.getHibernateTemplate().saveOrUpdate(defaultParam);
            return true;
        } catch (DataAccessException e) {
            return false;
        }
    }
}
