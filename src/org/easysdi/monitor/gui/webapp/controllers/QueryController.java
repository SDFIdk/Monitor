package org.easysdi.monitor.gui.webapp.controllers;

import java.util.Map;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
 * Processes the requests concerning a query.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
@Controller
@RequestMapping({ "/jobs/{jobIdString}/queries/{queryIdString}",
                  "/adminJobs/{jobIdString}/queries/{queryIdString}"}
                  )
public class QueryController extends AbstractMonitorController {

    /**
     * Creates a new controller.
     */
    public QueryController() {
        
    }
    
    
    
    /**
     * Erases a query.
     * 
     * @param   request                     the request that asked for the 
     *                                      deletion
     * @param   response                    the response to the request
     * @param   jobIdString                 the string that identifies the
     *                                      parent job. It can contain its 
     *                                      identifier or its name
     * @param   queryIdString               the string that identifies the
     *                                      parent query. It can contain its 
     *                                      identifier or its name
     * @return                              an object containing the result of
     *                                      the deletion and which view must
     *                                      be used to display it
     * @throws MonitorInterfaceException    <ul>
     *                                      <li>the query doesn't exist</li>
     *                                      <li>the parent job isn't public and
     *                                      the user has insufficient rights to
     *                                      consult the query's aggregate logs
     *                                      </li>
     *                                      <li>an error occurred during the 
     *                                      deletion of the query</li>
     *                                      </ul>
     */
    @RequestMapping(method = RequestMethod.DELETE)
    public ModelAndView delete(HttpServletRequest request,
                               HttpServletResponse response,
                               @PathVariable String jobIdString,
                               @PathVariable String queryIdString)
        throws MonitorInterfaceException {

        this.enforceRole(SecurityConstants.ADMIN_ROLE, request, response);
        final ModelAndView result = new ModelAndView("message");
        boolean success = false;

        try {
            final Query query = this.getQuery(jobIdString, queryIdString);
            success = query.delete();

        } catch (IllegalArgumentException e) {
            throw new MonitorInterfaceException(e.getMessage(),
                                                "query.argument.illegal");
        }

        if (success) {
            result.addObject("message", "query.delete.success");
        } else {
            throw new MonitorInterfaceException("Unable to delete the query",
                                                "query.delete.error");
        }

        return result;
    }



    /**
     * Changes the details of a request.
     * 
     * @param   request                     the request that asked for the query
     *                                      modification
     * @param   response                    the response to the request
     * @param   jobIdString                 the string that identifies the
     *                                      parent job. It can contain its 
     *                                      identifier or its name
     * @param   queryIdString               the string that identifies the
     *                                      parent query. It can contain its 
     *                                      identifier or its name
     * @return                              an object containing the result of
     *                                      the modification and which view must
     *                                      be used to display it
     * @throws MonitorInterfaceException    <ul>
     *                                      <li>the query doesn't exist</li>
     *                                      <li>the parent job isn't public and
     *                                      the user has insufficient rights to
     *                                      consult the query's aggregate logs
     *                                      </li>
     *                                      <li>an error occurred during the 
     *                                      modification of the query</li>
     *                                      </ul>
     */
    @RequestMapping(method = RequestMethod.PUT)
    public ModelAndView modify(HttpServletRequest request,
                               HttpServletResponse response,
                               @PathVariable String jobIdString,
                               @PathVariable String queryIdString
                              )
        throws MonitorInterfaceException {

        this.enforceRole(SecurityConstants.ADMIN_ROLE, request, response);
        final ModelAndView result = new ModelAndView("query");
        final Map<String, String> requestParams = this.getRequestParametersMap(request);

        try {
            final Query query = this.getQuery(jobIdString, queryIdString);
            final QueryInfo queryInfo 
                = QueryInfo.createFromParametersMap(
                        requestParams, query.getConfig().getParentJob(), false);
      
            if (queryInfo.modifyQueryConfig(query)) {
                result.addObject("message", "query.modify.success");
                result.addObject(query);
                return result;

            }

        } catch (IllegalArgumentException e) {
            throw new MonitorInterfaceException(e.getMessage(),
                                                "query.argument.illegal");
        }

        throw new MonitorInterfaceException("Unable to modify the query",
                                            "query.modify.error");
    }


  

    /**
     * Displays the details of a query.
     * 
     * @param   request                     the request that asked for the 
     *                                      details of the query
     * @param   response                    the response to the request
     * @param   jobIdString                 the string that identifies the
     *                                      parent job. It can contain its 
     *                                      identifier or its name
     * @param   queryIdString               the string that identifies the
     *                                      parent query. It can contain its 
     *                                      identifier or its name
     * @return                              an object containing the details of
     *                                      the query and which view must be
     *                                      used to display them
     * @throws MonitorInterfaceException    <ul>
     *                                      <li>the query doesn't exist</li>
     *                                      <li>the parent job isn't public and
     *                                      the user has insufficient rights to
     *                                      consult the details of the query
     *                                      </li>
     *                                      </ul>
     */
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView show(HttpServletRequest request,
                             HttpServletResponse response,
                             @PathVariable String jobIdString,
                             @PathVariable String queryIdString
                            
                           )
        throws MonitorInterfaceException {

        final ModelAndView result = new ModelAndView("query");
        
        final Query query = this.getQueryProtected(jobIdString, queryIdString,
                                                   request, response);

        result.addObject("message", "query.details.success");
        result.addObject(query);  
       

        return result;
    }
    
  
    
    
}
