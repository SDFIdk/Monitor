package org.easysdi.monitor.biz.security.springsecurity;

import java.util.Calendar;

import org.easysdi.monitor.biz.security.User;
import org.easysdi.monitor.dat.dao.IUserDao;
import org.easysdi.monitor.dat.dao.UserDaoHelper;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.security.userdetails.UsernameNotFoundException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

/**
 * Gets user details for the authentification process. 
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
public class UserAuthDetailsService implements
                org.springframework.security.userdetails.UserDetailsService {
    
    
    /**
     * Creates a new user search service.
     */
    public UserAuthDetailsService() {
        
    }
    
    

    /*
     * (non-Javadoc)
     * 
     * @seeorg.springframework.security.userdetails.UserDetailsService#
     * loadUserByUsername(java.lang.String)
     */
    /**
     * Looks for a user matching the given name.
     * 
     * @param   userName    the name of the user to find
     * @return              the user's details
     */
    public UserDetails loadUserByUsername(final String userName) {

        final IUserDao userDao = UserDaoHelper.getUserDao();

        final User user = (User) userDao.getTxTemplate().execute(
            new TransactionCallback<Object>() {
                public Object doInTransaction(TransactionStatus aArg0) {
                    return User.getUser(userName);
                }
            }
        );

        if (null == user) {
            throw new UsernameNotFoundException("User name not found");
        }

        return new org.springframework.security.userdetails.User(
                 user.getLogin(), user.getPassword(), user.isEnabled(),
                 null == user.getExpirationDate() 
                          || 0 < user.getExpirationDate().compareTo(
                                Calendar.getInstance()),
                 true, !user.isLocked(), 
                 new GrantedAuthority[] {
                     new GrantedAuthorityImpl(user.getRole().getName()) 
                 }
        );
    }

}
