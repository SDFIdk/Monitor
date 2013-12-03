package org.easysdi.monitor.dat.dao.hibernate;

import java.util.List;

import org.easysdi.monitor.biz.job.ServiceMethod;
import org.easysdi.monitor.biz.job.ServiceType;
import org.easysdi.monitor.biz.job.ServiceTypeMethod;
import org.easysdi.monitor.dat.dao.IServiceDao;
import org.easysdi.monitor.dat.dao.ServiceDaoHelper;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Provides OGC service information persistance operations through Hibernate.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
public class ServiceDao extends HibernateDaoSupport implements IServiceDao {

    /**
     * Creates a new OGC service data access object.
     * 
     * @param   sessionFactory  the Hibernate session factory object
     */
    public ServiceDao(SessionFactory sessionFactory) {
        this.setSessionFactory(sessionFactory);
        ServiceDaoHelper.setServiceDao(this);
    }



    /**
     * {@inheritDoc}
     */
    public ServiceType getServiceType(long id) {

        if (1 > id) {
            throw new IllegalArgumentException("Invalid identifier");
        }

        return this.getHibernateTemplate().load(ServiceType.class, id);
    }



    /**
     * {@inheritDoc}
     */
    public ServiceMethod getServiceMethod(long id) {

        if (1 > id) {
            throw new IllegalArgumentException("Invalid identifier");
        }

        return this.getHibernateTemplate().load(ServiceMethod.class, id);

    }



    /**
     * {@inheritDoc}
     */
    public boolean isMethodValidForType(long methodId, long typeId) {
        final DetachedCriteria criteria 
            = DetachedCriteria.forClass(ServiceTypeMethod.class);
        criteria.add(Restrictions.eq("serviceTypeId", typeId));
        criteria.add(Restrictions.eq("serviceMethodId", methodId));

        final List<?> result 
            = this.getHibernateTemplate().findByCriteria(criteria);

        return (null != result && 1 == result.size());

    }

}
