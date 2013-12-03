package org.easysdi.monitor.dat.dao.hibernate;

import org.easysdi.monitor.biz.job.AbstractValueObject;
import org.easysdi.monitor.biz.job.Status;
import org.easysdi.monitor.biz.job.Status.StatusValue;
import org.easysdi.monitor.dat.dao.IValueObjectDao;
import org.easysdi.monitor.dat.dao.ResultUtil;
import org.easysdi.monitor.dat.dao.ValueObjectDaoHelper;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Provides an access to Hibernate for value objects (that is, objects which
 * hold only a value, as an item of a coding table, for instance).
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
public class ValueObjectDao extends HibernateDaoSupport implements
                IValueObjectDao {

    /**
     * Creates a new value object data access object.
     * 
     * @param   sessionFactory  the Hibernate session factory 
     */
    public ValueObjectDao(SessionFactory sessionFactory) {
        this.setSessionFactory(sessionFactory);
        ValueObjectDaoHelper.setValueObjectDao(this);
    }



    /**
     * Retrieves a value object from the database.
     * 
     * @param   valueName       the name of the value object to fetch
     * @param   classMapName    the class name of the value object, as it is
     *                          mapped in the Hibernate configuration
     * @return                  the value object if it has been found, or<br>
     *                          <code>null</code> otherwise
     */
    public AbstractValueObject getValueObject(String valueName, 
                                              String classMapName) {

        try {
            final DetachedCriteria criteria 
                = DetachedCriteria.forEntityName(classMapName).add(
                     Restrictions.eq("name", valueName));

            return (AbstractValueObject) ResultUtil.uniqueResult(
                    this.getHibernateTemplate().findByCriteria(criteria));

        } catch (DataAccessException e) {
            this.logger.error(
                  "An exception was thrown while the object was fetched.", e);
            return null;
        }
    }



    /**
     * Retrieves a status object in the database.
     * 
     * @param   statusValue the value of the status to fetch
     * @return  the status object if it has been found, or<br>
     *          <code>null</code> otherwise
     */
    public Status getStatusObject(StatusValue statusValue) {

        try {
            return (Status) ResultUtil.uniqueResult(
                    this.getHibernateTemplate().findByNamedParam(
                             "from Status where :name = name", "name",
                             statusValue.name()));

        } catch (DataAccessException ex) {
            this.logger.error(
                    "An exception was thrown while the status was fetched.");
            return null;
        }

    }
}
