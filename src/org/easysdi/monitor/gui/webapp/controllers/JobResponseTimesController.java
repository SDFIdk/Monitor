package org.easysdi.monitor.gui.webapp.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.easysdi.monitor.gui.webapp.LogSearchParams;
import org.easysdi.monitor.gui.webapp.MonitorInterfaceException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Processes the requests concerning the response times for a job.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
@Controller
@RequestMapping({ "/jobs/{idString}/respTime", 
                  "/adminJobs/{idString}/respTime" })
public class JobResponseTimesController extends AbstractMonitorController {

    /**
     * Creates a new controller.
     */
    public JobResponseTimesController() {
        
    }
    
    
    
    /**
     * Shows the response times for a given job.
     * <p>
     * Request parameters can be used to refine which response times are 
     * fetched. See {@link 
     * LogSearchParams#createFromRequest(HttpServletRequest)} for more 
     * information.
     *     
     * @param   request                     the request that asked for 
     *                                      displaying the response times
     * @param   response                    the response to the request
     * @param   idString                    the string that identifies the 
     *                                      parent job. It can contain its
     *                                      identifier or its name
     * @return                              an object containing the requested
     *                                      response times and which view must
     *                                      be used to display them
     * @throws  MonitorInterfaceException   <ul>
     *                                      <li>the parent job doesn't exist
     *                                      </li>
     *                                      <li>the parent job isn't public and
     *                                      the user has insufficient rights to
     *                                      view its response times</li>
     *                                      <li>one of the parameters' value is
     *                                      invalid</li>
     *                                      </ul>
     * @see     {@link LogSearchParams#createFromRequest(HttpServletRequest)}
     */
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView show(HttpServletRequest request,
                             HttpServletResponse response, 
                             @PathVariable String idString)
        throws MonitorInterfaceException {

        return this.generateJobRawLogResult(request, response, idString);
    }
}
