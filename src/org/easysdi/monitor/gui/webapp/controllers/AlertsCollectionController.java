package org.easysdi.monitor.gui.webapp.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.easysdi.monitor.biz.job.Job;
import org.easysdi.monitor.dat.dao.AlertDaoHelper;
import org.easysdi.monitor.gui.webapp.MonitorInterfaceException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Processes the requests concerning the alerts collection for a job.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
@Controller
@RequestMapping({ "/jobs/{idString}/alerts", "/adminJobs/{idString}/alerts" })
public class AlertsCollectionController extends AbstractMonitorController {
    
    /**
     * Creates a new controller.
     */
    public AlertsCollectionController() {
        
    }
    
    

    /**
     * Show the alerts for a given job.
     * 
     * @param   request                     the request that asked for 
     *                                      displaying the alerts
     * @param   response                    the response to the request
     * @param   idString                    the string that identifies the
     *                                      parent job. It can contain its
     *                                      identifier or its name
     * @return                              an object containing the alerts
     *                                      collection and which view must be
     *                                      used to display it
     * @throws  MonitorInterfaceException   <ul>
     *                                      <li>the job doesn't exist</li>
     *                                      <li>the job isn't public and the
     *                                      user hasn't sufficient rights to
     *                                      view its alerts</li>
     *                                      </ul>
     */
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView show(HttpServletRequest request,
                             HttpServletResponse response, 
                             @PathVariable String idString)
        throws MonitorInterfaceException {
        
    	Job job = null;
    	final String getAll = request.getParameter("all");
    	
        final ModelAndView result = new ModelAndView("alertsCollectionJson");
        final String altValue = request.getParameter("alt");
        boolean onlyRss = false;
        
        if(null == getAll) {   	
        	 job = this.getJobProtected(idString, request, response);
        }

        if (null != altValue && altValue.equals("rss")) {
            result.setViewName("alertsCollectionRss");
            if(null == getAll) 
            	result.addObject("jobId", job.getJobId());
            
            onlyRss = true;
        }
        Integer start =null;
		Integer limit =null;
		String sortField ="";;
		String direction="";
		try {
			start = Integer.parseInt(request.getParameter("start"));
			limit = Integer.parseInt(request.getParameter("limit"));
			sortField = request.getParameter("sort");
			direction = request.getParameter("dir");
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			start = null;
			limit = null;
			sortField = "";
			direction = "";
			e.printStackTrace();
		}
        
		if(null== sortField)
			sortField = "";
		if(null== direction)
			direction = "";

        result.addObject("message", "alertsCollection.details.success");
        if(null == getAll){
	        if((start !=null) &&(limit!=null)){
	        	result.addObject("alertsCollection", job.getAlerts(onlyRss, start, limit, sortField, direction));
	        	 result.addObject("count",  job.getAlerts(onlyRss).size());
	        }
	        else{
	        	result.addObject("alertsCollection", job.getAlerts(onlyRss));
	        }
        }else{
        	if((start !=null) &&(limit!=null)){
	        	result.addObject("alertsCollection", AlertDaoHelper.getDaoObject().getAlertsForAllJobs(onlyRss, start, limit, sortField, direction));
	        	 result.addObject("count", AlertDaoHelper.getDaoObject().getAlertsForAllJobs(onlyRss).size());
	        }
	        else{
	        	result.addObject("alertsCollection", AlertDaoHelper.getDaoObject().getAlertsForAllJobs(onlyRss, 0, 250, "dateTime", "DESC")); 
	        	result.addObject("count", "250"); // we are sending at most 250 latest alerts, this allows us not to load all alerts in memory, which could cause an out of memory exception
	        }
        	
        }

        return result;
    }
}
