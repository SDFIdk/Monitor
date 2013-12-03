package org.easysdi.monitor.gui.webapp.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.easysdi.monitor.gui.webapp.AppContext;
import org.springframework.security.Authentication;
import org.springframework.security.AuthenticationException;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.context.SecurityContextHolder;
import 
    org.springframework.security.ui.basicauth.BasicProcessingFilterEntryPoint;

/**
 * Contains helper functions for security enforcement.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 * 
 */
public final class SecurityHelper {
    
    private static final Logger LOGGER = Logger.getLogger(SecurityHelper.class);
    
    /**
     * Dummy constructor used to prevent instantiation.
     */
    private SecurityHelper() {
        throw new UnsupportedOperationException(
                "This class can't be instantiated.");
    }
    

    
    /**
     * Checks if the current authenticated user (if any) has a certain role.
     * 
     * @param   role    the role to match by the user
     * @return          <code>true</code> if the user has the given role
     */
    public static boolean checkRoleAuthentication(String role) {
        final Authentication authentication 
            = SecurityContextHolder.getContext().getAuthentication();

        if (null != authentication) {

            for (GrantedAuthority authority : authentication.getAuthorities()) {
    
                if (authority.getAuthority().equals(role)) {
                    return true;
                }
            }
        }

        return false;

    }



    /**
     * Starts the process of authenticating the user.
     * 
     * @param   request     the request that asked for authentication
     * @param   response    the response to the request
     * @param   exception   the exception to throw if the user can't 
     *                      authenticate
     */
    public static void authenticate(HttpServletRequest request,
                                    HttpServletResponse response,
                                    AuthenticationException exception) {

        final BasicProcessingFilterEntryPoint authEntryPoint 
            = (BasicProcessingFilterEntryPoint) AppContext.getContext().getBean(
                   "authenticationEntryPoint");

        try {
            authEntryPoint.commence(request, response, exception);
        } catch (IOException e) {
            SecurityHelper.LOGGER.error(
                "An I/O error prevented the authentification.", e);
        } catch (ServletException e) {
            SecurityHelper.LOGGER.error(
                "A servlet error prevented the authentication.", e);
        }
    }
}
