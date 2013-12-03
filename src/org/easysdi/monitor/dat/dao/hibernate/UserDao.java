/**
 * 
 */
package org.easysdi.monitor.dat.dao.hibernate;

import org.easysdi.monitor.biz.security.User;
import org.easysdi.monitor.dat.dao.IUserDao;
import org.easysdi.monitor.dat.dao.UserDaoHelper;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * Provides user persistance operations through Hibernate.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
public class UserDao extends HibernateDaoSupport implements IUserDao {

    private TransactionTemplate txTemplate;



    /**
     * Creates a new user data access object.
     * 
     * @param   sessionFactory  the Hibernate session factory object
     */
    public UserDao(SessionFactory sessionFactory) {
        this.setSessionFactory(sessionFactory);
        UserDaoHelper.setUserDao(this);
    }



    /**
     * {@inheritDoc}
     */
    public User getUser(String aName) {

        return this.getHibernateTemplate().load(User.class, aName);

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

}
