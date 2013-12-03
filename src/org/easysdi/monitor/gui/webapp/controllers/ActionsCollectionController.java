package org.easysdi.monitor.gui.webapp.controllers;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.easysdi.monitor.biz.alert.AbstractAction;
import org.easysdi.monitor.biz.job.Job;
import org.easysdi.monitor.gui.webapp.ActionInfo;
import org.easysdi.monitor.gui.webapp.MonitorInterfaceException;
import org.easysdi.monitor.gui.webapp.security.SecurityConstants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Processes the requests concerning the actions collection for a job.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
@Controller
@RequestMapping({ "/jobs/{idString}/actions", "/adminJobs/{idString}/actions" })
public class ActionsCollectionController extends AbstractMonitorController {
    
    /**
     * Creates a new controller.
     */
    public ActionsCollectionController() {
        
    }
    
    

    /**
     * Creates a new action.
     * 
     * @param   request                     the servlet request that asked for
     *                                      creating the action
     * @param   response                    the response to the request
     * @param   idString                    the string that identifies the 
     *                                      new action's parent job. It can
     *                                      contain its identifier or its name
     * @return                              an object containing the new 
     *                                      action's information and which view
     *                                      must be used to display it
     * @throws  MonitorInterfaceException   <ul>
     *                                      <li>the parent job doesn't exist
     *                                      </li>
     *                                      <li>an argument has an illegal value
     *                                      </li>
     *                                      <li>the action couldn't be persisted
     *                                      </li>
     *                                      </ul>
     */
    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView create(HttpServletRequest request,
                               HttpServletResponse response,
                               @PathVariable String idString)
        throws MonitorInterfaceException {
        
        this.enforceRole(SecurityConstants.ADMIN_ROLE, request, response);

        final ModelAndView result = new ModelAndView("action");
        final Map<String, String> requestParameters 
            = this.getRequestParametersMap(request);

        try {
            final Job job = this.getJob(idString);
            final ActionInfo actionInfo 
                = ActionInfo.createFromParametersMap(
                        requestParameters, job, true);
            final AbstractAction newAction = actionInfo.createAction();

            if (null != newAction) {
                result.addObject("message", "action.create.success");
                result.addObject("action", newAction);

                return result;

            }
        } catch (IllegalArgumentException e) {
            throw new MonitorInterfaceException(e.getMessage(),
                                                "action.argument.illegal");
        }

        throw new MonitorInterfaceException(
                "Unable to persist the action", "action.persist.error");
    }



    /**
     * Shows information for all the job's actions.
     * 
     * @param   request                     the servlet request that asked for 
     *                                      the actions information
     * @param   response                    the response to the request
     * @param   idString                    the string that identifies the 
     *                                      parent job. It can contain either
     *                                      its identifier or its name
     * @return                              an object containing the actions
     *                                      collection and which view must be
     *                                      used to display it
     * @throws  MonitorInterfaceException   the job doesn't exist
     */
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView show(HttpServletRequest request,
                             HttpServletResponse response, 
                             @PathVariable String idString)
        throws MonitorInterfaceException {

        final ModelAndView result = new ModelAndView("actionsCollection");
        final Job job = this.getJob(idString);

        result.addObject("message", "actionsCollection.details.success");
        result.addObject("actionsCollection", job.getActions());

        return result;
    }

}
