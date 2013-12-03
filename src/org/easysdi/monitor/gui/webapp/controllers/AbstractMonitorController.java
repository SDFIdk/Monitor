package org.easysdi.monitor.gui.webapp.controllers;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Set;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.easysdi.monitor.biz.alert.AbstractAction;
import org.easysdi.monitor.biz.job.Job;
import org.easysdi.monitor.biz.job.Overview;
import org.easysdi.monitor.biz.job.Period;
import org.easysdi.monitor.biz.job.Sla;
import org.easysdi.monitor.biz.job.Holiday;
import org.easysdi.monitor.biz.job.Query;
import org.easysdi.monitor.biz.logging.LogManager;
import org.easysdi.monitor.biz.logging.RawLogEntry;
import org.easysdi.monitor.gui.i18n.Messages;
import org.easysdi.monitor.gui.webapp.AppContext;
import org.easysdi.monitor.gui.webapp.LogSearchParams;
import org.easysdi.monitor.gui.webapp.LogSlaHelper;
import org.easysdi.monitor.gui.webapp.MonitorInterfaceException;
import org.easysdi.monitor.gui.webapp.security.SecurityConstants;
import org.easysdi.monitor.gui.webapp.security.SecurityHelper;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import 
    org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 * Defines a controller outline for use with Spring MVC.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
@Controller
public abstract class AbstractMonitorController {
    
    private static final Pattern ADMIN_REQUEST_PATTERN 
        = Pattern.compile("\\/adminJobs(?:\\/.*|$)");
    private static final Logger LOGGER 
        = Logger.getLogger(AbstractMonitorController.class); 
    
    
    

    /**
     * Processes any exception thrown by the controller.
     * 
     * @param request       the request that originally called this controller
     * @param response      the response to the request
     * @param exception     the exception thrown by a controller method
     * @return              a model and view object to be treated by the
     *                      {@link ErrorView}.
     */
    @ExceptionHandler
    public ModelAndView processExceptions(HttpServletRequest request,
                                          HttpServletResponse response, 
                                          Exception exception) {

        final ModelAndView result = new ModelAndView("error");

        if (exception instanceof MonitorInterfaceException) {
            result.addObject("monitorInterfaceException", exception);
        } else if (exception instanceof MissingResourceException) {
            result.addObject("monitorInterfaceException",
                new MonitorInterfaceException(
                     "Unable to display informations in the requested language",
                     "language.resource.missing", exception));
        } else {
            result.addObject("exception", exception);
        }

        return result;
    }



    /**
     * Ensures that the user has a certain role and provides the opportunity to
     * log otherwise.
     * 
     * @param   role                        the role that the user must have to 
     *                                      be able to proceed
     * @param   request                     the original HTTP request
     * @param   response                    the response to the request
     * @throws  MonitorInterfaceException   the user has insufficient rights 
     *                                      even after logging on
     */
    protected void enforceRole(String role, HttpServletRequest request,
                               HttpServletResponse response)
        throws MonitorInterfaceException {
        
        final Messages i18n 
            = new Messages(RequestContextUtils.getLocale(request));

        if (!SecurityHelper.checkRoleAuthentication(role)) {
            
            SecurityHelper.authenticate(
                    request, response,
                    new AuthenticationCredentialsNotFoundException(
                           i18n.getMessage("resource.protected")));

            if (!SecurityHelper.checkRoleAuthentication(role)) {
                throw new MonitorInterfaceException(
                    "You don't have sufficient rights to access this resource",
                    "insufficient.rights");
            }
        }
    }



    /**
     * Gets a job from an identifying string (without role enforcing).
     * 
     * @param   idString                    a string containing either the job's
     *                                      identifier or its name
     * @return                              the job
     * @throws  MonitorInterfaceException   no job matches the identifying 
     *                                      string
     */
    protected Job getJob(String idString) throws MonitorInterfaceException {

        final Job job = Job.getFromIdString(idString);

        if (null == job) {
            throw new MonitorInterfaceException("Job doesn't exist",
                                                "job.notExist");
        }

        return job;
    }



    /**
     * Gets a job with role enforcing if it isn't public.
     * 
     * @param   idString                    a string containing either the job's
     *                                      identifier or its name
     * @param   request                     the original HTTP request
     * @param   response                    the response to the request
     * @return                              the job
     * @throws  MonitorInterfaceException   <ul>
     *                                      <li>no job matches the identifying 
     *                                      string</li>
     *                                      <li>the job isn't public and the 
     *                                      user has insufficient rights to view
     *                                      it</li>
     *                                      </ul>
     */
    protected Job getJobProtected(String idString, HttpServletRequest request,
                                  HttpServletResponse response)
        throws MonitorInterfaceException {

        final Job job = this.getJob(idString);
        
        this.enforceJobProtection(job, request, response);

        return job;
    }
    
    
    
    /**
     * Allows access to private jobs only to authorized users.
     * 
     * @param   job             the requested job
     * @param   request         the request that asked for the job
     * @param   response        the response to the request
     * @throws  MonitorInterfaceException   the job isn't available to the user
     */
    private void enforceJobProtection(Job job, HttpServletRequest request, 
                                      HttpServletResponse response)
        throws MonitorInterfaceException {

        if (!job.getConfig().isPublished()) {
            
            if (!this.isAdminRequest(request)) {
                throw new MonitorInterfaceException("Job doesn't exist",
                                                    "job.notExist");
            }
            
            this.enforceRole(SecurityConstants.ADMIN_ROLE, request, response);
        }
    }

    /**
     * Gets a overview from an identifying string (without role enforcing).
     * 
     * @param   idString                    a string containing either the overview's
     *                                      identifier or its name
     * @return                              the overview
     * @throws  MonitorInterfaceException   no overview matches the identifying 
     *                                      string
     */
    protected Overview getOverview(String idString) throws MonitorInterfaceException {

        final Overview overview = Overview.getFromIdString(idString);

        if (null == overview) {
            throw new MonitorInterfaceException("Overview doesn't exist",
                                                "overview.notExist");
        }

        return overview;
    }
    
    /**
     * Gets a sla from an identifying string (without role enforcing).
     * 
     * @param   idString                    a string containing either the sla's
     *                                      identifier or its name
     * @return                              the sla
     * @throws  MonitorInterfaceException   no overview matches the identifying 
     *                                      string
     */
    protected Sla getSla(String idString) throws MonitorInterfaceException {
        final Sla sla = Sla.getFromIdString(idString);
        if (null == sla) {
            throw new MonitorInterfaceException("sla doesn't exist",
                                                "sla.notExist");
        }
        return sla;
    }
    
    protected Holiday getHoliday(String idString) throws MonitorInterfaceException{
    	final Holiday holiday = Holiday.getFromIdString(idString);
    	if(holiday == null)
    	{
    		throw new MonitorInterfaceException("Holiday doesn't exist",
            "holiday.notExist");
    	}
    	return holiday;
    }
    
    protected Period getPeriod(String idString) throws MonitorInterfaceException
    {
    	final Period period = Period.getFromIdString(idString);
    	if(period == null)
    	{
    		   throw new MonitorInterfaceException("period doesn't exist",
               "period.notExist");
    	}
    	return period;
    }

    /**
     * Gets a query from an identifying string (without role enforcing).
     * 
     * @param   jobIdString                 a string containing either the 
     *                                      parent job's identifier or its name
     * @param   queryIdString               a string containing either the 
     *                                      query's identifier or its name
     * @return                              the query
     * @throws  MonitorInterfaceException   no query matches the identifying 
     *                                      strings
     */    
    protected Query getQuery(String jobIdString, String queryIdString)
        throws MonitorInterfaceException {

        final Query query = Query.getFromIdStrings(jobIdString, queryIdString);

        if (null == query) {
            throw new MonitorInterfaceException("Query doesn't exist",
                                                "query.notExist");
        }

        return query;
    }

    

    /**
     * Gets a query with role enforcing if the parent job isn't public.
     * 
     * @param   jobIdString                 a string containing either the 
     *                                      parent job's identifier or its name
     * @param   queryIdString               a string containing either the 
     *                                      query's identifier or its name
     * @param   request                     the original HTTP request
     * @param   response                    the response to the request
     * @return                              the query
     * @throws  MonitorInterfaceException   <ul>
     *                                      <li>no query matches the identifying
     *                                      strings</li>
     *                                      <li>the parent job isn't public and 
     *                                      the user has insufficient rights to 
     *                                      view it</li>
     *                                      </ul>
     */
    protected Query getQueryProtected(String jobIdString, String queryIdString,
                                      HttpServletRequest request, 
                                      HttpServletResponse response)
        throws MonitorInterfaceException {

        final Query query = this.getQuery(jobIdString, queryIdString);

        this.enforceJobProtection(query.getConfig().getParentJob(), request, 
                                  response);

        return query;
    }



    /**
     * Gets an action from an identifying string (without role enforcing).
     * 
     * @param   jobIdString                 a string containing either the 
     *                                      parent job's identifier or its name
     * @param   actionIdString              a string containing the action's 
     *                                      identifier
     * @return                              the action
     * @throws  MonitorInterfaceException   no action matches the identifying 
     *                                      strings
     */    
    protected AbstractAction getAction(String jobIdString, 
                                       String actionIdString)
        throws MonitorInterfaceException {
        
        final AbstractAction action 
            = AbstractAction.getFromIdStrings(jobIdString, actionIdString);

        if (null == action) {
            throw new MonitorInterfaceException("Action doesn't exist",
                                                "action.notExist");
        }

        return action;
    }



    /**
     * Gets an action with role enforcing if the parent job isn't public.
     * 
     * @param   jobIdString                 a string containing either the 
     *                                      parent job's identifier or its name
     * @param   actionIdString              a string containing the action's 
     *                                      identifier or its name
     * @param   request                     the original HTTP request
     * @param   response                    the response to the request
     * @return                              the action
     * @throws  MonitorInterfaceException   <ul>
     *                                      <li>no action matches the 
     *                                      identifying strings</li>
     *                                      <li>the parent job isn't public and 
     *                                      the user has insufficient rights to 
     *                                      view it</li>
     *                                      </ul>
     */
    protected AbstractAction getActionProtected(String jobIdString,
                                                String actionIdString,
                                                HttpServletRequest request,
                                                HttpServletResponse response)
        throws MonitorInterfaceException {

        final AbstractAction action
            = this.getAction(jobIdString, actionIdString);

        this.enforceJobProtection(action.getParentJob(), request, response);

        return action;
    }
    
    
    
    /**
     * Gets a set of raw log entries for a job, checking if it is protected.
     * <p>
     * Request parameters can be used to refine which response times are 
     * fetched. See {@link 
     * LogSearchParams#createFromRequest(HttpServletRequest)} for more 
     * information.
     * 
     * @param   job                         the parent job of the searched logs
     * @param   searchParams                the object containing the search
     *                                      parameters for the desired subset
     * @return                              a set containing the requested raw 
     *                                      log entries
     * @throws  MonitorInterfaceException   <ul>
     *                                      <li>the job doesn't exist</li>
     *                                      <li>the job isn't public and the
     *                                      user has insufficient rights to 
     *                                      consult its raw logs</li>
     *                                      </ul>
     */
    protected Set<RawLogEntry> getJobRawLogs(
              Job job, LogSearchParams searchParams,Map<String, String> requestParams) 
        throws MonitorInterfaceException {
        
        final LogManager logManager = new LogManager(job);
        Set<RawLogEntry> rawLog;
        
        if(requestParams.get("useSla") != null)
        {
        	rawLog = logManager.getRawLogsSubset(searchParams.getMinDate(), 
                    searchParams.getMaxDate(),searchParams.getMaxResults(), 
                    searchParams.getStartIndex());
        	rawLog = LogSlaHelper.getRawlogForSla(requestParams.get("useSla"), rawLog,true);
        }else
        {
        	rawLog = logManager.getRawLogsSubset(
                    searchParams.getMinDate(), searchParams.getMaxDate(),
                    searchParams.getMaxResults(), searchParams.getStartIndex());
        	rawLog = LogSlaHelper.getRawLogForDefault(rawLog, true);
        }
        return rawLog;
    }
    
    
    
    /**
     * Gets the number of raw log entries that would have been returned without
     * any paging parameter.
     * <p>
     * Request parameters can be used to refine which response times are 
     * fetched. See {@link 
     * LogSearchParams#createFromRequest(HttpServletRequest)} for more 
     * information. Parameters <code>startIndex</code> and 
     * <code>maxResults</code> have obviously no effect.
     * 
     * @param   job                         the parent job of the searched logs
     * @param   searchParams                the object containing the search
     *                                      parameters for the desired subset. 
     *                                      Maximum number of items and index of
     *                                      the first item aren't considered
     * @return                              a long indicating how many items are
     *                                      contained in the total subset, 
     *                                      disregarding any paging parameter
     * @throws  MonitorInterfaceException   <ul>
     *                                      <li>the job doesn't exist</li>
     *                                      <li>the job isn't public and the
     *                                      user has insufficient rights to 
     *                                      consult its raw logs</li>
     *                                      </ul>
     */    
    protected Long getJobRawLogsNoPagingCount(
            Job job, LogSearchParams searchParams)
        throws MonitorInterfaceException {
        
        final LogManager logManager = new LogManager(job);
       
    	return logManager.getRawLogsItemsNumber(searchParams.getMinDate(),
                searchParams.getMaxDate(),
                null, null);
       
    }
                
    
    
    // FIXME Sortir la gestion des logs dans une sous-classe distincte    
    /**
     * Determines which view must be used to display a collection of raw logs.
     * <p>
     * It branches to the CSV view if:
     * <ol>
     * <li>the <code>alt</code> request parameter has the value <code>csv
     * </code></li>
     * <li>the <code>Content-Type</code> request header is set to <code>
     * text/csv</code></li>
     * </ol>
     * <p>
     * In other cases, the JSON view is used.
     * 
     * @param   request the servlet request that asked for raw log entries
     * @return  a string containing the identifier for the view to use
     */
    protected String getRawLogViewName(HttpServletRequest request) {
        final String altParam = request.getParameter("alt");
        final String contentType = request.getHeader("Content-Type");
        String viewName = "rawLogsJson";
    
        if ((null != altParam && altParam.equals("csv"))
            || (null != contentType && contentType.equals("text/csv"))) {
    
            viewName = "rawLogsCsv";
        }
        
        return viewName;
    }



    /**
     * Generate a result for a request demanding a job's raw log entries. 
     * 
     * @param   request                     the request that asked for 
     *                                      a set of raw logs
     * @param   response                    the response to the request
     * @param   idString                    the string that identifies the 
     *                                      parent job. It can contain its 
     *                                      identifier or its name
     * @return                              an object containing the requested
     *                                      raw logs collection and which view
     *                                      must be used to display it
     * @throws  MonitorInterfaceException   the parent job doesn't exist
     */
    protected ModelAndView generateJobRawLogResult(HttpServletRequest request,
                                                   HttpServletResponse response,
                                                   String idString)
        throws MonitorInterfaceException {
        
        final String viewName = this.getRawLogViewName(request);
        final ModelAndView result = new ModelAndView(viewName);
        final Job job 
            = this.getJobProtected(idString, request, response);
        final Map<String, String> requestParams
            = this.getRequestParametersMap(request);
        final LogSearchParams searchParams 
            = LogSearchParams.createFromParametersMap(requestParams);
        
        // Needs fix with paging for sla
        long totalPagingCount = this.getJobRawLogsNoPagingCount(job, searchParams);
        
        Set<RawLogEntry> rawLog = this.getJobRawLogs(job, searchParams,requestParams);
        String slaName = "Default";
        if(requestParams.get("useSla") != null)
        {
        	slaName = Sla.getFromIdString(requestParams.get("useSla")).getName();
        }
        if(requestParams.get("export") != null)
        {
        	result.addObject("getExport", true);
        	result.addObject("Jobname",job.getConfig().getJobName());
        	result.addObject("Queryname","");
        	result.addObject("Slaname",slaName);
        }
        
        result.addObject("message", "log.details.success");
        result.addObject("rawLogsCollection", rawLog);
        result.addObject("noPagingCount", totalPagingCount);
        result.addObject("addQueryId", true);
        result.addObject("isSummary",true);

        return result;
    }
    
    
    
    /**
     * Determines if the request asks for the full jobs collection.
     * 
     * @param   request the request to be examined
     * @return  <code>true</code> if the full jobs collection is requested
     */
    protected boolean isAdminRequest(HttpServletRequest request) {
        return AbstractMonitorController.ADMIN_REQUEST_PATTERN.matcher(
                                               request.getPathInfo()).matches();
    }
    
    
    
    /**
     * Gets the parameters of an incoming request, whether they are specified as
     * form parameters or in JSON.
     * 
     * @param   request the request to process
     * @return          a map containing the parameters of the request
     */
    @SuppressWarnings("unchecked")
	protected Map<String, String> getRequestParametersMap(
                                                  HttpServletRequest request) {
        
        if ("GET".equals(request.getMethod())) {
            return this.getSingleValueRequestParametersMap(request);
        }
        
        final ServletServerHttpRequest springRequest 
            = new ServletServerHttpRequest(request);
        final String contentType 
            = this.getContentTypeWithoutEnconding(springRequest);
        Map<String, String> requestParameters = null;

        if ("application/json".equalsIgnoreCase(contentType)) {
            requestParameters = this.getRequestJsonParameters(springRequest);
        }
        //added this
        else if("application/x-www-form-urlencoded".equalsIgnoreCase(contentType)){
        	requestParameters = new HashMap<String, String>();
        	Map map = request.getParameterMap();
            Set entries = map.entrySet();
            Iterator iter = entries.iterator();
            while ( iter.hasNext() )
            {
               Map.Entry entry = (Map.Entry)iter.next() ;
               String   name   = (String)entry.getKey() ;
        	   String[] params = (String[])entry.getValue() ;
        	  if(!name.equals("Itemid"))
        	  {
        	   for(int i=0; i<params.length; i++)
        		   requestParameters.put(name, params[i]);
        	  }
            }
        }    
        
        /* does not seem to work for kvp post anyway...*/
        else if (null == requestParameters) {
            requestParameters = this.getRequestKvpParameters(springRequest);
        }
        
        return requestParameters;
    }



    /**
     * Transforms the multivalue map containing the parameters of a request into
     * a single value map (the first value is retained). 
     * 
     * @param   request the HTTP request
     * @return          a map containing the first value of each parameter
     */
    private Map<String, String> getSingleValueRequestParametersMap(
                                                   HttpServletRequest request) {

        final Map<String, String> requestParams = new HashMap<String, String>();
        final Enumeration<String> parametersNames = request.getParameterNames();
        
        while (parametersNames.hasMoreElements()) {
            final String paramName = parametersNames.nextElement();
            final String[] paramValues = request.getParameterValues(paramName);
            final String value = (null != paramValues && 0 < paramValues.length)
                               ? paramValues[0] : "";
            requestParams.put(paramName, value);
        }
        
        return requestParams;
    }



    /**
     * Extracts the parameters of a key-value-pair request.
     * 
     * @param   springRequest   the request to process
     * @return                  a map containing the KVP parameters
     */
    @SuppressWarnings("unchecked")
    private Map<String, String> getRequestKvpParameters(
                               final ServletServerHttpRequest springRequest) {
        final FormHttpMessageConverter formConverter 
            = new FormHttpMessageConverter();
        MultiValueMap<String, String> params 
            = new LinkedMultiValueMap<String, String>();
        
        try {
            final Class<? extends MultiValueMap<String, String>> mapType =
                (Class<? extends MultiValueMap<String, String>>) 
                    params.getClass();
            final MediaType contentType 
                = springRequest.getHeaders().getContentType();
            
            if (formConverter.canRead(mapType, contentType)) {
                params = formConverter.read(mapType, springRequest);
            }

        } catch (IOException exception) {
            AbstractMonitorController.LOGGER.error(
                   "Unable to convert request body from KVP", exception);
        }

        
        return (params != null) ? params.toSingleValueMap() : null;
    }
    
    
    
    /**
     * Gets the content type of a request without the optional character 
     * encoding value.
     * 
     * @param   request the request
     * @return          the content type string without encoding information
     */
    private String getContentTypeWithoutEnconding(
                                            ServletServerHttpRequest request) {
        final MediaType contentType = request.getHeaders().getContentType();
        
        if (HttpMethod.GET.equals(request.getMethod())) {
            return "";
        }
        
        return String.format("%1$s/%2$s", contentType.getType(), 
                             contentType.getSubtype());
        
    }



    /**
     * Extracts the parameters of a request whose body is in JSON.
     * 
     * @param   springRequest   the request to process
     * @return                  a map containing the JSON parameters
     */
    @SuppressWarnings("unchecked")
    private Map<String, String> getRequestJsonParameters(
            final ServletServerHttpRequest springRequest) {
        
        Map<String, String> requestParameters = null;
        final MappingJacksonHttpMessageConverter jsonConverter 
            = new MappingJacksonHttpMessageConverter();
        
        jsonConverter.setObjectMapper(
                      AppContext.getContext().getBean(ObjectMapper.class));
       
        try {
            Map<String, ?> tempMap =
                (HashMap<String, ?>) jsonConverter.read(HashMap.class, 
                                                        springRequest);
            
            if (1 == tempMap.size()) {
                final Object mapValue = tempMap.values().iterator().next();
                
                if (mapValue instanceof Map<?, ?>) {
                    tempMap = (Map<String, ?>) mapValue;
                }
            }
            
            requestParameters = new HashMap<String, String>();
            
            for (String key : tempMap.keySet()) {
                final Object value = tempMap.get(key);
                final String stringValue = (value != null) 
                                         ? value.toString() : null;
                requestParameters.put(key, stringValue);
            }
            
        } catch (IOException exception) {
            AbstractMonitorController.LOGGER.error(
                   "Unable to convert request body from JSON", exception);
        }
        
        return requestParameters;
    }
}
