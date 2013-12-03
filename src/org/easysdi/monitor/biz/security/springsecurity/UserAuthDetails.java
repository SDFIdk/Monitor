/**
 * 
 */
package org.easysdi.monitor.biz.security.springsecurity;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import org.deegree.framework.util.StringTools;
import org.easysdi.monitor.biz.security.Role;
import org.easysdi.monitor.biz.security.User;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;

/**
 * Provides information about a user's authentication.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
public class UserAuthDetails implements
                org.springframework.security.userdetails.UserDetails {

    private static final long  serialVersionUID = 972868770399117547L;
    private User               user;
    private GrantedAuthority[] authorities;



    /**
     * Creates a new authentication object.
     * 
     * @param   authenticatedUser   the user whose authentication details
     *                              shall be provided
     */
    public UserAuthDetails(User authenticatedUser) {
        this.setUser(authenticatedUser);
    }



    /**
     * Defines the authenticated user.
     * 
     * @param   authenticatedUser   the authenticated user
     */
    private void setUser(User authenticatedUser) {

        if (null == authenticatedUser) {
            throw new IllegalArgumentException("User can't be null");
        }

        this.user = authenticatedUser;
    }



    /**
     * Gets the authenticated user.
     * 
     * @return  the authenticated user
     */
    private User getUser() {
        return this.user;
    }



    /**
     * Defines the user's granted roles.
     * 
     * @param   grantedAuthorities  the roles granted to the user
     */
    private void setAuthorities(GrantedAuthority[] grantedAuthorities) {
        this.authorities = grantedAuthorities;
    }



    /**
     * {@inheritDoc}
     */
    public GrantedAuthority[] getAuthorities() {

        if (null == this.authorities) {
            final Set<GrantedAuthority> authoritiesSet
                = new HashSet<GrantedAuthority>();
            final Role role = this.getUser().getRole();

            if (null != role) {
                authoritiesSet.add(new GrantedAuthorityImpl(role.getName()));
            }
            
            final GrantedAuthority[] authoritiesArrayTemplate 
                = new GrantedAuthority[authoritiesSet.size()]; 
            final GrantedAuthority[] authoritiesArray =
                authoritiesSet.toArray(authoritiesArrayTemplate);

            this.setAuthorities(authoritiesArray);
        }

        return this.authorities;
    }



    /**
     * {@inheritDoc}
     */
    public String getPassword() {
        return this.getUser().getPassword();
    }



    /**
     * {@inheritDoc}
     */
    public String getUsername() {
        return this.getUser().getLogin();
    }



    /**
     * {@inheritDoc}
     */
    public boolean isAccountNonExpired() {
        final Calendar expirationDate = this.getUser().getExpirationDate();

        return (null == expirationDate 
                     || 1 > Calendar.getInstance().compareTo(expirationDate));
    }



    /**
     * {@inheritDoc}
     */
    public boolean isAccountNonLocked() {

        return (!this.getUser().isLocked());
    }



    /**
     * {@inheritDoc}
     */
    public boolean isCredentialsNonExpired() {

        return (!StringTools.isNullOrEmpty(this.getPassword()));

    }



    /**
     * {@inheritDoc}
     */
    public boolean isEnabled() {

        return this.getUser().isEnabled();
    }

}
