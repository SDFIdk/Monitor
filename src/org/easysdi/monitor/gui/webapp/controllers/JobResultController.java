package org.easysdi.monitor.gui.webapp.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.easysdi.monitor.biz.job.Job;
import org.easysdi.monitor.gui.webapp.MonitorInterfaceException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Processes the requests concerning a job's real-time result.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
@Controller
@RequestMapping({ "/jobs/{idString}/status", "/adminJobs/{idString}/status" })
public class JobResultController extends AbstractMonitorController {
    
    /**
     * Creates a new controller.
     */
    public JobResultController() {
        
    }
    
    

    /**
     * Shows the result of a given job's real-time execution.
     * 
     * @param   request                     the request asking for the job's
     *                                      real-time execution
     * @param   response                    the response to the request
     * @param   idString                    the string identifying the job to
     *                                      execute. It can contain its 
     *                                      identifier or its name 
     * @return                              an object containing the execution
     *                                      result and which view must be used 
     *                                      to display it
     * @throws  MonitorInterfaceException   <ul>
     *                                      <li>the job doesn't exist</li>
     *                                      <li>the job isn't public and the 
     *                                      user has insufficient rights to 
     *                                      execute it</li>
     *                                      <li>the job's real-time execution
     *                                      is disallowed</li>
     *                                      </ul>
     */
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView show(HttpServletRequest request,
                             HttpServletResponse response, 
                             @PathVariable String idString)
        throws MonitorInterfaceException {

        final ModelAndView result = new ModelAndView("jobResult");
        final Job job = this.getJobProtected(idString, request, response);

        if (job.getConfig().isRealTimeAllowed()) {
            result.addObject("jobResult", job.executeAllQueries(false));
            result.addObject("message", "jobResult.execution.success");
            
        } else {
            throw new MonitorInterfaceException(
                    "This job doesn't allow real-time execution",
                    "job.realtime.notAllowed");
        }

        return result;
    }

}
