package org.easysdi.monitor.gui.webapp.controllers;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.easysdi.monitor.biz.job.Job;
import org.easysdi.monitor.gui.webapp.JobInfo;
import org.easysdi.monitor.gui.webapp.MandatoryParameterException;
import org.easysdi.monitor.gui.webapp.MonitorInterfaceException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Process the requests concerning a job.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.2, 2010-08-06
 *
 */
@Controller
@RequestMapping({ "/jobs/{identifyString}", "adminJobs/{identifyString}" })
public class JobController extends AbstractMonitorController {

    /**
     * Creates a new controller.
     */
    public JobController() {
        
    }
    
    
    
    /**
     * Deletes a job.
     * 
     * @param   request                     the request that asked for the job's
     *                                      deletion
     * @param   response                    the response to the request
     * @param   identifyString              the string that identifies the job.
     *                                      It can contain its identifier or its
     *                                      name 
     * @return                              an object containing a success 
     *                                      message and which view must be used
     *                                      to display it
     * @throws  MonitorInterfaceException   <ul>
     *                                      <li>the job doesn't exist</li>
     *                                      <li>the job couldn't be deleted</li>
     *                                      </ul>
     */
    @RequestMapping(method = RequestMethod.DELETE)
    public ModelAndView delete(HttpServletRequest request,
                               HttpServletResponse response,
                               @PathVariable String identifyString)
        throws MonitorInterfaceException {

        final ModelAndView result = new ModelAndView("message");
        final Job job = this.getJob(identifyString);

        if (job.delete()) {
            result.addObject("message", "job.delete.success");
            
            return result;
            
        }
        
        throw new MonitorInterfaceException("Unable to delete the job",
                                            "job.delete.error");
    }
    


    /**
     * Alters a job's configuration.
     * 
     * @param   request                     the request that asked for the job's
     *                                      modification
     * @param   response                    the response to the job
     * @param   identifyString              the string that identifies the job.
     *                                      It can contain its identifier or its
     *                                      name
     * @return                              an object containing the updated 
     *                                      job's information and which view 
     *                                      must bew used to display it
     * @throws  MonitorInterfaceException   <ul>
     *                                      <li>the job doesn't exist</li>
     *                                      <li>an argument contains an invalid
     *                                      value</li>
     *                                      <li>a lower-level error prevented
     *                                      saving the modifications</li>
     *                                      </ul>   
     */
    @RequestMapping(method = RequestMethod.PUT)
    public ModelAndView modify(HttpServletRequest request,
                               HttpServletResponse response,
                               @PathVariable String identifyString)
        throws MonitorInterfaceException {
        
        final ModelAndView result = new ModelAndView("job");
        final Map<String, String> requestParams 
            = this.getRequestParametersMap(request);
        Job job;
        JobInfo jobInfo;

        try {
            job = this.getJob(identifyString);
            jobInfo = JobInfo.createFromParametersMap(requestParams, false);


            if (jobInfo.modifyJobParams(job, false)) {
                result.addObject("message", "job.modify.success");
                result.addObject(job);
    
                return result;
    
            }
        } catch (MandatoryParameterException e) {
            throw new MonitorInterfaceException(e.getMessage(),
                                                "job.argument.illegal", e);
        } catch (IllegalArgumentException e) {
            throw new MonitorInterfaceException(e.getMessage(),
                                                "job.argument.illegal", e);
        }
            
        throw new MonitorInterfaceException("Unable to modify the job",
                                            "job.modify.error");
    }



    /**
     * Shows information about a job.
     * 
     * @param   request                     the servlet request that asked for 
     *                                      the job's information
     * @param   response                    the response to the request 
     * @param   identifyString              the string that identifies the job.
     *                                      It can contain its identifier or its
     *                                      name
     * @return                              an object containing the job's
     *                                      information and which view must be
     *                                      used to display it
     * @throws  MonitorInterfaceException   <ul>
     *                                      <li>the job doesn't exist</li>
     *                                      <li>the job isn't public and the 
     *                                      user has insufficient rights to
     *                                      display its information</li>
     *                                      </ul>
     */
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView show(HttpServletRequest request,
                             HttpServletResponse response,
                             @PathVariable String identifyString)
        throws MonitorInterfaceException {

        final ModelAndView result = new ModelAndView("job");
        final Job job = this.getJobProtected(identifyString, request, response);

        result.addObject("message", "job.details.success");
        result.addObject(job);

        return result;
    }
}
