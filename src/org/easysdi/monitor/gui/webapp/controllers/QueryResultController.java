/**
 * 
 */
package org.easysdi.monitor.gui.webapp.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.easysdi.monitor.biz.job.Job;
import org.easysdi.monitor.biz.job.Query;
import org.easysdi.monitor.biz.job.QueryResult;
import org.easysdi.monitor.biz.job.QueryTestResult;
import org.easysdi.monitor.gui.webapp.MonitorInterfaceException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author BERG3428
 *
 */
@Controller
@RequestMapping({ "/jobs/{idString}/queries/{idQuery}/preview", "/adminJobs/{idString}/queries/{idQuery}/preview" })
public class QueryResultController extends AbstractMonitorController {

	public QueryResultController()
	{
		
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
    public ModelAndView create(HttpServletRequest request,
                             HttpServletResponse response, 
                             @PathVariable String idString,
                             @PathVariable String idQuery)
        throws MonitorInterfaceException {

        final ModelAndView result = new ModelAndView("queryTestResult");
        final Job job = this.getJobProtected(idString, request, response);
        final Query query = this.getQueryProtected(idString, idQuery, request, response); 
    
        QueryResult rs = job.executeSingleQuery(query);
        if(rs == null)
        {
        	System.out.println("Failed");
        }
        // Maybe wait a few seconds
        QueryTestResult queryTest = QueryTestResult.getFromIdString(idQuery);
       // System.out.println("Name: "+rs.getQueryName()+" Size "+rs.getSize()+ " Time: "+rs.getResponseDelay()+ " Xpath: ");
        result.addObject("queryTestResult",queryTest);
        result.addObject("message", "queryResult.execution.success");  
        return result;
    }
}
