package org.easysdi.monitor.gui.webapp.security;

/**
 * Provides constants for security operations.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
public class SecurityConstants {

    public static final String ADMIN_ROLE    = "ROLE_ADMIN";
    public static final String USER_ROLE     = "ROLE_USER";
    public static final String PROVIDER_ROLE = "ROLE_PROVIDER";
    
    
    
    /**
     * Dummy constructor used to prevent instantiation.
     */
    private SecurityConstants() {
        throw new UnsupportedOperationException(
                                           "This class can't be instantiated.");
    }

}
