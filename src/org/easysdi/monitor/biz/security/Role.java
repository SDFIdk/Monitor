package org.easysdi.monitor.biz.security;

/**
 * Defines a user role for the Monitor application.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
public class Role {

    private long   roleId;
    private String name;
    private int    rankValue;



    /**
     * No-argument constructor used by the persistence mechanism.
     */
    private Role() {

    }

    

    /**
     * Defines the identifier of this role.
     * <p>
     * <i><b>Note:</b> This method is intended for internal use only. The 
     * identifier definition is best left to the persistance mechanism.</i> 
     * 
     * @param   newId   the long uniquely identifying this role
     */
    @SuppressWarnings("unused")
    private void setRoleId(long newId) {
        this.roleId = newId;
    }



    /**
     * Gets the identifier of this role.
     * 
     * @return  the long uniquely identifying this role
     */
    @SuppressWarnings("unused")
    private long getRoleId() {
        return this.roleId;
    }



    /**
     * Defines the name of this role.
     * <p>
     * To be considered by the security mechanism, the role name must start with
     * <code>ROLE_</code>
     * 
     * @param   newName the role name
     */
    @SuppressWarnings("unused")
    private void setName(String newName) {
        this.name = newName;
    }



    /**
     * Gets the name of this role.
     * 
     * @return  the role name
     */
    public String getName() {
        return this.name;
    }



    /**
     * Defines the value allowing to determine how this role compares to the 
     * others in terms of hierarchy.
     * <p>
     * This feature isn't used in the current version.
     * 
     * @param   newRankValue    the rank value for this role
     */
    @SuppressWarnings("unused")
    private void setRankValue(int newRankValue) {
        this.rankValue = newRankValue;
    }



    /**
     * Gets the value allowing to determine how this role compares to the others
     * in terms of hierarchy.
     * <p>
     * This feature isn't used in the current version.
     * 
     * @return the rank value for this role
     */
    public int getRankValue() {
        return this.rankValue;
    }
}
