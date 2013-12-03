package org.easysdi.monitor.biz.security.springsecurity;

import org.easysdi.monitor.biz.security.Role;
import org.springframework.security.GrantedAuthority;

/**
 * Represents an authenticated user.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
public class Authority implements GrantedAuthority {

    private static final long serialVersionUID = 4821351164378202386L;

    private Role              role;



    /**
     * Creates a new object to hold informations about an authenticated user.
     * 
     * @param   newUserRole the user's role
     */
    public Authority(Role newUserRole) {
        this.setRole(newUserRole);
    }



    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.security.GrantedAuthority#getAuthority()
     */
    /**
     * Defines the user's role in the application.
     * 
     * @param   newRole the user's role
     */
    private void setRole(Role newRole) {

        if (null == newRole) {
            throw new IllegalArgumentException("Role can't be null");
        }

        this.role = newRole;
    }



    /**
     * Gets the user's role in the application.
     * 
     * @return  the user's role
     */
    private Role getRole() {
        return this.role;
    }

    

    /**
     * Gets the name by which the user is identified.
     * 
     * @return the user's name
     */
    public String getAuthority() {

        return this.getRole().getName();

    }



    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    /**
     * Compares users based on their role.
     * 
     * @param  thatObject   the other user object
     * @return              an integer showing how the passed user compares to 
     *                      this one
     */
    public int compareTo(Object thatObject) {

        if (thatObject instanceof Authority) {
            final Authority that = (Authority) thatObject;         
            final Integer thatUserRank = that.getRole().getRankValue();
            final Integer thisUserRank = this.getRole().getRankValue();

            return thatUserRank.compareTo(thisUserRank);
        }

        throw new UnsupportedOperationException(
                String.format("Can't compare %1$s to %2$s",
                              this.getClass().toString(), 
                              thatObject.getClass().toString()));

    }

}
