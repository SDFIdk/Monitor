package org.easysdi.monitor.gui.webapp.controllers;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.deegree.framework.util.BooleanUtil;
import org.easysdi.monitor.biz.job.Job;
import org.easysdi.monitor.biz.job.JobsCollection;
import org.easysdi.monitor.gui.webapp.JobInfo;
import org.easysdi.monitor.gui.webapp.MandatoryParameterException;
import org.easysdi.monitor.gui.webapp.MonitorInterfaceException;
import org.easysdi.monitor.gui.webapp.security.SecurityConstants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Processes the requests concerning the jobs collection.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
@Controller
@RequestMapping({ "/jobs", "adminJobs" })
public class JobsCollectionController extends AbstractMonitorController {

    /**
     * Creates a new controller.
     */
    public JobsCollectionController() {

    }



    /**
     * Creates a new job.
     * 
     * @param   request                     the request that asked for the job's
     *                                      creation
     * @param   response                    the response to the request
     * @return                              an object containing the new job's
     *                                      information and which view must be
     *                                      used to display it
     * @throws  MonitorInterfaceException   <ul>
     *                                      <li>one of the job's parameters has
     *                                      an invalid value</li>
     *                                      <li>a lower-level error prevented 
     *                                      saving the new job</li>
     *                                      </ul>   
     */
    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView create(HttpServletRequest request,
                               HttpServletResponse response)
        throws MonitorInterfaceException {

        this.enforceRole(SecurityConstants.ADMIN_ROLE, request, response);
        final ModelAndView result = new ModelAndView("job");
        final Map<String, String> requestParams = this.getRequestParametersMap(request);
        Job newJob;

        try {
            final JobInfo jobInfo = JobInfo.createFromParametersMap(requestParams, true);
            newJob = jobInfo.createJob();
            
        } catch (MandatoryParameterException e) {
            throw new MonitorInterfaceException(e.getMessage(),"job.argument.illegal", e);
        } catch (IllegalArgumentException e) {
            throw new MonitorInterfaceException(e.getMessage(),"job.argument.illegal", e); 
        } catch (IllegalStateException e) {
            throw new MonitorInterfaceException(e.getMessage(),"job.persist.error", e);
        }
        result.addObject("message", "job.create.success");
        result.addObject(newJob);
        return result;
    }



    /**
     * Shows the public jobs collection.
     * 
     * @param   request             the request that asked for displaying the 
     *                              jobs collection
     * @param   response            the response to the request
     * @return                      an object containing the public jobs 
     *                              collection and which view must be used to 
     *                              display it
     * @throws MonitorInterfaceException    <ul>
     *                                      <li>The user has insufficient rights
     *                                      to view the private jobs collection
     *                                      </li>
     *                                      <li>An internal error occurred while
     *                                      the job list was retrieved</li>
     *                                      </ul>
     */
    @RequestMapping(method = RequestMethod.GET) 
    public ModelAndView show(HttpServletRequest request,
                             HttpServletResponse response) 
        throws MonitorInterfaceException {
        
        final ModelAndView result = new ModelAndView("jobsCollection");
        final Map<String, String> requestParams
            = this.getRequestParametersMap(request);

        final Boolean automatic 
            = BooleanUtil.parseBooleanStringWithNull(
                     requestParams.get("isAutomatic"));
        
        final Boolean realTime 
            = BooleanUtil.parseBooleanStringWithNull(
                     requestParams.get("allowsRealTime"));
        
        final Boolean alert 
            = BooleanUtil.parseBooleanStringWithNull(
                     requestParams.get("triggersAlerts"));
        
        
        Integer start =null;
		Integer limit =null;
		String sortField ="";;
		String direction="";
		try {
			start = Integer.parseInt(requestParams.get("start"));
			limit = Integer.parseInt(requestParams.get("limit"));
			sortField = requestParams.get("sort");
			direction = requestParams.get("dir");
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			start = null;
			limit = null;
			sortField = "";
			direction = "";
			//e.printStackTrace();
		}
        
		if(null== sortField)
			sortField = "";
		if(null== direction)
			direction = "";
        
        final boolean useAdminCollection 
            = this.isAdminRequest(request); 
        
        if (useAdminCollection) {
            this.enforceRole(SecurityConstants.ADMIN_ROLE, request, response);
        }
        
        final Boolean onlyPublic = (useAdminCollection) ? null : true;

        // the if section is for carrying out the pagination queries.
        if((start !=null) &&(limit!=null)){
	        result.addObject("jobList", 
	                         new JobsCollection().findJobs(automatic, realTime,
	                                                       onlyPublic, alert, start, limit, sortField, direction));
	        result.addObject("count",  new JobsCollection().findJobs(automatic, realTime,
	                onlyPublic, alert).size());
        }
        else{
        	 result.addObject("jobList", 
                     new JobsCollection().findJobs(automatic, realTime,
                                                   onlyPublic, alert));
        }
        result.addObject("message", "jobsCollection.details.success");

        return result;
    }
}
