package org.easysdi.monitor.gui.webapp.controllers;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.easysdi.monitor.biz.job.Job;
import org.easysdi.monitor.biz.job.Query;
import org.easysdi.monitor.gui.webapp.MonitorInterfaceException;
import org.easysdi.monitor.gui.webapp.QueryInfo;
import org.easysdi.monitor.gui.webapp.security.SecurityConstants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Processes the requests concerning the query collection of a job.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
@Controller
@RequestMapping({ "/jobs/{jobIdString}/queries", 
                  "/adminJobs/{jobIdString}/queries" })
public class QueryCollectionController extends AbstractMonitorController {
    
    /**
     * Creates a new controller.
     */
    public QueryCollectionController() {
        
    }
    
    

    /**
     * Creates a new query for the job.
     * 
     * @param   request     the request that asked for the query creation 
     * @param   response    the response to the request
     * @param   jobIdString a string identifying the parent job of the new 
     *                      query. It can contain its identifier or its name
     * @return              an object containing the creation result and which
     *                      view must be used to display it
     * @throws  MonitorInterfaceException   <ul>
     *                                      <li>the job doesn't exist</li>
     *                                      <li>the job isn't public and the 
     *                                      user has insufficient rights to 
     *                                      view it</li>
     *                                      <li>an error occurred during the 
     *                                      creation of the query</li>
     *                                      </ul>
     */
    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView create(HttpServletRequest request,
                               HttpServletResponse response,
                               @PathVariable String jobIdString)
        throws MonitorInterfaceException {

        this.enforceRole(SecurityConstants.ADMIN_ROLE, request, response);
        final ModelAndView result = new ModelAndView("query");
        final Map<String, String> requestParams
            = this.getRequestParametersMap(request);

        try {
            final Job job = this.getJob(jobIdString);
            final QueryInfo queryInfo 
                = QueryInfo.createFromParametersMap(requestParams, job, true);
            final Query newQuery = queryInfo.createQuery();

            if (null != newQuery) {
                result.addObject("message", "query.create.success");
                result.addObject(newQuery);
                return result;
            }

        } catch (IllegalArgumentException e) {
            throw new MonitorInterfaceException(e.getMessage(),
                                                "query.argument.illegal");

        } catch (IllegalStateException e) {
            throw new MonitorInterfaceException(e.getMessage(),
                                                "query.persist.error");
        }
            
        throw new MonitorInterfaceException(
                "An error prevented the creation of the query", 
                "query.create.error");

    }



    /**
     * Shows the query collection for a job.
     * 
     * @param   request         the request that asked for the details of the 
     *                          collection
     * @param   response        the response to the request
     * @param   jobIdString     a string identifying the parent job of the new 
     *                          query. It can contain its identifier or its name
     * @return                  an object containing the details of the 
     *                          query collection and which view must be used to
     *                          display them
     * @throws  MonitorInterfaceException   <ul>
     *                                      <li>the job doesn't exist</li>
     *                                      <li>the job isn't public and the 
     *                                      user has insufficient rights to 
     *                                      view it</li>
     *                                      </ul>
     */
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView show(HttpServletRequest request,
                             HttpServletResponse response,
                             @PathVariable String jobIdString)
        throws MonitorInterfaceException {

        final ModelAndView result = new ModelAndView("queriesCollection");
        final Job job = this.getJobProtected(jobIdString, request, response);

        result.addObject("message", "queriesCollection.details.success");
        result.addObject("queryCollection", job.getQueriesList());

        return result;
    }

}
