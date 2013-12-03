package org.easysdi.monitor.gui.webapp.controllers;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.easysdi.monitor.biz.alert.AbstractAction;
import org.easysdi.monitor.gui.webapp.ActionInfo;
import org.easysdi.monitor.gui.webapp.MonitorInterfaceException;
import org.easysdi.monitor.gui.webapp.security.SecurityConstants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Processes request concerning an action.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.2, 2010-08-06
 *
 */
@Controller
@RequestMapping({ "/jobs/{jobIdString}/actions/{actionIdString}", 
                  "/adminJobs/{jobIdString}/actions/{actionIdString}" })
public class ActionController extends AbstractMonitorController {

    /**
     * Creates a new controller.
     */
    public ActionController() {
        
    }
    
    
    
    /**
     * Deletes an action.
     * 
     * @param   request                     the servlet request that asked for
     *                                      the action's deletion
     * @param   response                    the response to the request
     * @param   jobIdString                 the string identifying the action's
     *                                      parent job. It can be its identifier
     *                                      or its name
     * @param   actionIdString              a string containing the action's
     *                                      identifier 
     * @return                              an object containing a success 
     *                                      message and which view to use to 
     *                                      display it
     * @throws  MonitorInterfaceException   <ul>
     *                                      <li>the action doesn't exist</li>
     *                                      <li>a lower-level error prevented 
     *                                      the action's deletion</li>
     *                                      </ul>
     */
    @RequestMapping(method = RequestMethod.DELETE)
    public ModelAndView delete(HttpServletRequest request,
                               HttpServletResponse response,
                               @PathVariable String jobIdString,
                               @PathVariable String actionIdString)
        throws MonitorInterfaceException {

        this.enforceRole(SecurityConstants.ADMIN_ROLE, request, response);
        final ModelAndView result = new ModelAndView("message");
        final AbstractAction action 
            = this.getAction(jobIdString, actionIdString);

        if (action.delete()) {
            result.addObject("message", "action.delete.success");
            return result;

        }
        
        throw new MonitorInterfaceException(
                "Unable to delete the action",
                "action.delete.error");
    }



    /**
     * Alters an action's parameters.
     * 
     * @param   request                     the servlet request that asked for
     *                                      modifying the action
     * @param   response                    the response to the request
     * @param   jobIdString                 the string identifying the action's
     *                                      parent job. It can be its identifier
     *                                      or its name
     * @param   actionIdString              a string containing the action's
     *                                      identifier 
     * @return                              an object containing the updated 
     *                                      action's information and which view 
     *                                      to use to display it
     * @throws  MonitorInterfaceException   <ul>
     *                                      <li>the action doesn't exist</li>
     *                                      <li>a request parameter contains an
     *                                      invalid value</li>
     *                                      <li>a lower-level error prevented 
     *                                      the action's modification</li>
     *                                      </ul>
     */
    @RequestMapping(method = RequestMethod.PUT)
    public ModelAndView modify(HttpServletRequest request,
                               HttpServletResponse response,
                               @PathVariable String jobIdString, 
                               @PathVariable String actionIdString)
        throws MonitorInterfaceException {
        
        this.enforceRole(SecurityConstants.ADMIN_ROLE, request, response);
        final ModelAndView result = new ModelAndView("action");

        final AbstractAction action 
            = this.getAction(jobIdString, actionIdString);

        try {
            final Map<String, String> requestParams 
                = this.getRequestParametersMap(request);
            final ActionInfo actionInfo 
                = ActionInfo.createFromParametersMap(requestParams,
                                                     action.getParentJob(), 
                                                     false);

            if (actionInfo.modifyAction(action)) {
                result.addObject("message", "result.modify.success");
                result.addObject("action", action);
                return result;

            }
        } catch (IllegalArgumentException e) {
            throw new MonitorInterfaceException(e.getMessage(),
                                                "action.argument.illegal");
        }
                
        throw new MonitorInterfaceException("Unable to modify the action",
                                            "action.modify.error");

    }



    /**
     * Displays information about an action.
     * 
     * @param   request                     the servlet request that asked for
     *                                      the action's information
     * @param   response                    the response to the request
     * @param   jobIdString                 the string identifying the action's
     *                                      parent job. It can be its identifier
     *                                      or its name
     * @param   actionIdString              a string containing the action's
     *                                      identifier 
     * @return                              an object containing the action's
     *                                      information and which view to use
     *                                      to display it
     * @throws  MonitorInterfaceException   the action doesn't exist
     */
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView show(HttpServletRequest request, 
                             HttpServletResponse response,
                             @PathVariable String jobIdString,
                             @PathVariable String actionIdString)
        throws MonitorInterfaceException {

        final ModelAndView result = new ModelAndView("action");
        final AbstractAction action 
            = this.getAction(jobIdString, actionIdString);

        result.addObject("message", "action.details.success");
        result.addObject("action", action);

        return result;
    }

}
