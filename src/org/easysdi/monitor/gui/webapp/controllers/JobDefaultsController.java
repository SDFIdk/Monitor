package org.easysdi.monitor.gui.webapp.controllers;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.easysdi.monitor.biz.job.JobConfiguration;
import org.easysdi.monitor.biz.job.JobDefaultParameter;
import org.easysdi.monitor.gui.webapp.JobDefaultsInfo;
import org.easysdi.monitor.gui.webapp.MonitorInterfaceException;
import org.easysdi.monitor.gui.webapp.security.SecurityConstants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Processes the requests concerning the job default parameters.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
@Controller
@RequestMapping("/jobDefaults")
public class JobDefaultsController extends AbstractMonitorController {
    
    /**
     * Creates a new controller.
     */
    public JobDefaultsController() {
        
    }
    
    

    /**
     * Changes some parameters' value.
     * 
     * @param   request                     the request that asked for the 
     *                                      defaults' modification
     * @param   response                    the response to the request
     * @return                              an object containing the updated
     *                                      defaults and which view must be used
     *                                      to display them
     * @throws  MonitorInterfaceException   <ul>
     *                                      <li>one of the new parameter values
     *                                      is invalid</li>
     *                                      <li>a lower-level error prevented
     *                                      saving the new value for at least
     *                                      one default parameter</li>
     *                                      </ul>
     */
    @RequestMapping(method = RequestMethod.PUT)
    public ModelAndView modify(HttpServletRequest request,
                               HttpServletResponse response)
        throws MonitorInterfaceException {
        
        this.enforceRole(SecurityConstants.ADMIN_ROLE, request, response);
        final ModelAndView result = new ModelAndView("jobDefaultsCollection");
        Map<String, JobDefaultParameter> jobDefaultsCollection;
        final Map<String, String> requestParams
            = this.getRequestParametersMap(request);
        JobDefaultsInfo jobDefaultsInfo;

        try {
            jobDefaultsCollection = JobConfiguration.getDefaultParams();
            jobDefaultsInfo 
                = JobDefaultsInfo.createFromParametersMap(requestParams);

        } catch (IllegalArgumentException e) {
            throw new MonitorInterfaceException(e.getMessage(),
                                                "jobDefault.argument.illegal");
        }
        
        if (jobDefaultsInfo.updateDefaults(jobDefaultsCollection)) {
            result.addObject("message", "jobDefaultsCollection.modify.success");
            result.addObject("jobDefaultsCollection",
                             JobConfiguration.getDefaultParams());

            return result;

        }
        
        throw new MonitorInterfaceException(
                "Not all job defaults could be modified",
                "jobDefaultsCollection.modify.error");
    }



    /**
     * Shows the default job parameters.
     * 
     * @return  an object containing the job defaults and which view must be 
     *          used to display them
     */
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView show() {

        final ModelAndView result = new ModelAndView("jobDefaultsCollection");

        result.addObject("message", "jobDefaultsCollection.details.success");
        result.addObject("jobDefaultsCollection",
                         JobConfiguration.getDefaultParams());

        return result;
    }
}
