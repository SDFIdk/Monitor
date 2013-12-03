package org.easysdi.monitor.gui.webapp.controllers;

import java.util.ArrayList;
import java.util.HashSet;
//import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.easysdi.monitor.biz.job.Query;
import org.easysdi.monitor.biz.job.QueryConfiguration;
import org.easysdi.monitor.biz.job.QueryParam;
import org.easysdi.monitor.biz.job.QueryParameterNotFoundException;
import org.easysdi.monitor.gui.webapp.MonitorInterfaceException;
import org.easysdi.monitor.gui.webapp.security.SecurityConstants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Processes HTTP requests concerning the parameters of a query. 
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
@Controller
@RequestMapping({ "/jobs/{jobIdString}/queries/{queryIdString}/params", 
                  "/adminJobs/{jobIdString}/queries/{queryIdString}/params" })
public class QueryParamsController extends AbstractMonitorController {

    private final Logger logger = Logger.getLogger(QueryParamsController.class);
    
    
    /**
     * Creates a new query parameter controller.
     */
    public QueryParamsController() {
        
    }
    
    

    /**
     * Creates a new set of parameters for the query.
     * <p>
     * The parameters of the request will be used as query parameters. Any
     * existing parameter for the given query will be deleted.
     * 
     * @param   request                     the request that asked to define the
     *                                      parameters of a query
     * @param   response                    the response to the request
     * @param   jobIdString                 a string identifying the parent job
     *                                      of the query. It can contain its 
     *                                      identifier or its name
     * @param   queryIdString               a string identifying the query whose
     *                                      parameters must be defined. It can 
     *                                      contain its identifier or its name
     * @return                              an object containing the model data
     *                                      produced by the request and a view 
     *                                      identifier
     * @throws  MonitorInterfaceException   <ul>
     *                                      <li>the query couldn't be opened
     *                                      </li> 
     *                                      <li>a lower-level error prevented 
     *                                      saving the query parameters</li>
     *                                      </ul>   
     */
    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView create(HttpServletRequest request,
                               HttpServletResponse response,
                               @PathVariable String jobIdString,
                               @PathVariable String queryIdString)
        throws MonitorInterfaceException {
        
        this.enforceRole(SecurityConstants.ADMIN_ROLE, request, response);
        final ModelAndView result = new ModelAndView("queryParams");

        
        
        /*Map map = request.getParameterMap() ;
        Set entries = map.entrySet() ;
        Iterator iter = entries.iterator() ;
        
        
        boolean found =false;
        while ( iter.hasNext() )
        {
           Map.Entry entry = (Map.Entry)iter.next() ;
           String   name   = (String)entry.getKey() ;
           String[] params = (String[])entry.getValue() ;
          for(int i=0; i<params.length; i++)
              System.out.println("name:"+name+", value:"+params[i]);
           found = true;
        }
        if(!found)
        	System.out.println("no parameter found!");
        */
                
        final Query query = this.getQuery(jobIdString, queryIdString);
        final QueryConfiguration config = query.getConfig();
        final Map<String, String> requestParams 
            = this.getRequestParametersMap(request);
        final Set<QueryParam> deletedParams 
            = new HashSet<QueryParam>(query.getConfig().getParams());

        if (null != requestParams) {
            String paramValue;
            QueryParam queryParam;
            ArrayList<String> paramsFound = new ArrayList<String>();
            
            for (String paramName : requestParams.keySet()) {

                if (!StringUtils.isBlank(paramName) && !paramsFound.contains(paramName.toLowerCase())) {
                	paramsFound.add(paramName.toLowerCase());
                	paramValue = requestParams.get(paramName);
                	queryParam = config.findParam(paramName);

                    if (null == queryParam) {
                        config.addParam(new QueryParam(query, paramName,
                                                       paramValue));
                    } else {
                        queryParam.setValue(paramValue);
                        deletedParams.remove(queryParam);
                    }
                }
            }
        }

        this.deleteParameters(deletedParams, config);

        if (query.persist()) {
            result.addObject("message", "queryParams.define.success");
            result.addObject("queryParamsCollection", config.getParams());

            return result;

        }
        
        throw new MonitorInterfaceException(
                "An error prevented persisting the query",
                "query.persist.error");
    }



    /**
     * Modify some query parameters.
     * 
     * @param   request                     the request that asked to change the
     *                                      parameters of a query
     * @param   response                    the response to the request
     * @param   jobIdString                 a string identifying the parent job
     *                                      of the query. It can contain its 
     *                                      identifier or its name
     * @param   queryIdString               a string identifying the query whose
     *                                      parameters must be defined. It can 
     *                                      contain its identifier or its name
     * @return                              an object containing the model data
     *                                      produced by the request and a view 
     *                                      identifier
     * @throws  MonitorInterfaceException   <ul>
     *                                      <li>the query couldn't be opened
     *                                      </li> 
     *                                      <li>a parameter doesn't exist</li>
     *                                      <li>a lower-level error prevented 
     *                                      saving the query parameters</li>
     *                                      </ul>   
     */
    @RequestMapping(method = RequestMethod.PUT)
    public ModelAndView modify(HttpServletRequest request,
                               HttpServletResponse response,
                               @PathVariable String jobIdString,
                               @PathVariable String queryIdString)
        throws MonitorInterfaceException {

        this.enforceRole(SecurityConstants.ADMIN_ROLE, request, response);
        final ModelAndView result = new ModelAndView("queryParams");

        final Query query = this.getQuery(jobIdString, queryIdString);
        final Map<String, String> requestParams 
            = this.getRequestParametersMap(request);

        for (String paramName : requestParams.keySet()) {

            if (!StringUtils.isBlank(paramName)) {

                try {
                    query.getConfig().setParam(paramName,
                                               request.getParameter(paramName));

                } catch (QueryParameterNotFoundException e) {
                    throw new MonitorInterfaceException(
                            "Request contains an inexistant parameter",
                            "queryParams.modify.notExist", e);
                }
            }
        }

        if (query.persist()) {
            result.addObject("message", "queryParams.modify.success");
            result.addObject("queryParamsCollection",
                             query.getConfig().getParams());

            return result;
            
        }
        
        throw new MonitorInterfaceException(
                "An error occurred while modifying the query parameters",
                "queryParams.modify.error");
    }



    /**
     * Displays the parameters for a given query.
     * 
     * @param   request                     the request that called this 
     *                                      controller
     * @param   response                    the response to the request
     * @param   jobIdString                 a string identifying the parent job
     *                                      of the query. It can contain its 
     *                                      identifier or its name
     * @param   queryIdString               a string identifying the query whose
     *                                      parameters must be defined. It can 
     *                                      contain its identifier or its name
     * @return                              an object containing the model data
     *                                      produced by the request and a view 
     *                                      identifier
     * @throws  MonitorInterfaceException   <ul>
     *                                      <li>the query couldn't be opened
     *                                      </li> 
     *                                      <li>a lower-level error prevented 
     *                                      saving the query parameters</li>
     *                                      </ul>   
     */
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView show(HttpServletRequest request,
                             HttpServletResponse response,
                             @PathVariable String jobIdString,
                             @PathVariable String queryIdString)
        throws MonitorInterfaceException {

        final ModelAndView result = new ModelAndView("queryParams");

        final Query query = this.getQueryProtected(jobIdString, queryIdString,
                                                   request, response);

        result.addObject("message", "queryParams.details.success");
        result.addObject("queryParamsCollection", 
                         query.getConfig().getParams());

        return result;
    }
    
    

    /**
     * Deletes a set of query parameters.
     * 
     * @param   parametersToDelete  a set containing the names of the parameters
     *                              to delete
     * @param   config              the query configuration
     */
    private void deleteParameters(Set<QueryParam> parametersToDelete, 
                                  QueryConfiguration config) {

        if (null != parametersToDelete) { 
            for (QueryParam oldParam : parametersToDelete) {
    
                try {
                    config.removeParam(oldParam.getName());
    
                } catch (QueryParameterNotFoundException e) {
                    this.logger.error(String.format(
                         "The controller attempted to delete parameter '%1$s'" 
                         + "which doesn't exist for query %2$d.", 
                         e.getParameterName(), e.getQueryId()), e);
                }
            }
        }
    }

}
