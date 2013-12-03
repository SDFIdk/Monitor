package org.easysdi.monitor.biz.security;

import java.util.Calendar;

import org.easysdi.monitor.dat.dao.UserDaoHelper;

/**
 * Represents an identified user of the application.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
public class User {

    private boolean  enabled;
    private Calendar expirationDate;
    private boolean  locked;
    private String   login;
    private String   password;
    private Role     role;



    /**
     * No-argument constructor used by the persistence mechanism.
     */
    private User() {

    }



    /**
     * Defines if this user account is active.
     * <p>
     * If you need to temporarily disable an account for extraordinary reasons,
     * you should rather use {@link #setLocked(boolean)}.
     * 
     * @param   isEnabled   <code>true</code> to enable this user to log in
     */
    @SuppressWarnings("unused")
    private void setEnabled(boolean isEnabled) {
        this.enabled = isEnabled;
    }



    /**
     * Gets whether this user account is active. 
     * 
     * @return  <code>true</code> if this user can log in
     */
    public boolean isEnabled() {
        return this.enabled;
    }



    /**
     * Defines when this user account expires.
     * 
     * @param   date    the expiration date or
     *                  <code>null</code> if this user account shall not expire
     */
    @SuppressWarnings("unused")
    private void setExpirationDate(Calendar date) {
        this.expirationDate = date;
    }



    /**
     * Gets when this user account expires.
     * 
     * @return  the expiration date or
     *          <code>null</code> if this user account shall not expire
     */
    public Calendar getExpirationDate() {
        return this.expirationDate;
    }



    /**
     * Defines whether this user account shall be locked.
     * <p>
     * This can be use to prevent this user to log in, for extraordinary reasons
     * such as a compromised account, a suspected brute force attack under way, 
     * etc.
     * <p>
     * If the account shall be disallowed durably for non-extraordinary reasons,
     * you should rather use {@link #setEnabled(boolean)}.
     * 
     * @param   isLocked    <code>true</code> to prevent this user to log in
     */
    @SuppressWarnings("unused")
    private void setLocked(boolean isLocked) {
        this.locked = isLocked;
    }



    /**
     * Gets whether this account has been disallowed for extraordinary reasons.
     * 
     * @return  <code>true</code> if the account has been locked
     */
    public boolean isLocked() {
        return this.locked;
    }



    /**
     * Defines this user's login name.
     * 
     * @param   newLogin    the name under which this user logs in
     */
    @SuppressWarnings("unused")
    private void setLogin(String newLogin) {
        this.login = newLogin;
    }



    /**
     * Gets this user's login name.
     * 
     * @return  the name under which this user logs in
     */
    public String getLogin() {
        return this.login;
    }



    /**
     * Defines this user's password.
     * 
     * @param   newPassword this user's new password 
     */
    @SuppressWarnings("unused")
    private void setPassword(String newPassword) {
        this.password = newPassword;
    }



    /**
     * Gets this user's password.
     * 
     * @return  this user's password
     */
    public String getPassword() {
        //FIXME Paramétrer Spring Security pour utiliser un hâchage et éviter
        //      ainsi d'avoir à récupérer le mot de passe.
        return this.password;
    }



    /**
     * Defines this user's role.
     * 
     * @param   newRole this user's application role
     */
    @SuppressWarnings("unused")
    private void setRole(Role newRole) {
        this.role = newRole;
    }



    /**
     * Gets this user's role.
     * 
     * @return  this user's application role
     */
    public Role getRole() {
        return this.role;
    }


    /**
     * Gets the object representing a given user.
     * 
     * @param   userName    the desired user's login name
     * @return              the user if it's been found, or
     *                      <code>null</code> otherwise
     */
    public static User getUser(String userName) {
        return UserDaoHelper.getUserDao().getUser(userName);
    }
}
