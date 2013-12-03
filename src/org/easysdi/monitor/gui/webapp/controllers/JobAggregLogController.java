package org.easysdi.monitor.gui.webapp.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.easysdi.monitor.biz.job.Job;
import org.easysdi.monitor.biz.job.Sla;
import org.easysdi.monitor.biz.logging.AbstractAggregateHourLogEntry;
import org.easysdi.monitor.biz.logging.AbstractAggregateLogEntry;
import org.easysdi.monitor.biz.logging.LogManager;
import org.easysdi.monitor.gui.webapp.LogSearchParams;
import org.easysdi.monitor.gui.webapp.LogSlaHelper;
import org.easysdi.monitor.gui.webapp.MonitorInterfaceException;
import org.easysdi.monitor.gui.webapp.security.SecurityConstants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Processes the requests concerning the aggregate logs for a job. 
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
@Controller
@RequestMapping({ "/jobs/{idString}/aggLogs", "/adminJobs/{idString}/aggLogs" })
public class JobAggregLogController extends AbstractMonitorController {
    
    /**
     * Creates a new controller.
     */
    public JobAggregLogController() {
        
    }
    
    

    /**
     * Deletes the aggregate logs up to a given date.
     * <p>
     * The date is passed through the <code>maxDate</code> request parameter.
     * It must be formatted as YYYY-MM-DD.
     * <p>
     * <i><b>Note:</b> The given date is <b>included</b> in the deletion span.
     * </i>
     * 
     * @param   request                     the request that asked for the logs'
     *                                      deletion
     * @param   response                    the response to the request
     * @param   idString                    the string identifying the job. It 
     *                                      can contain its identifier or its 
     *                                      name
     * @return                              a object containing a success 
     *                                      message and which view must be used 
     *                                      to display it
     * @throws MonitorInterfaceException    <ul>
     *                                      <li>the job doesn't exist</li>
     *                                      <li>no maximum date has been given
     *                                      </li>
     *                                      <li>the maximum date format isn't
     *                                      YYYY-MM-DD</li>
     *                                      </ul>
     */
    @RequestMapping(method = RequestMethod.DELETE)
    public ModelAndView delete(HttpServletRequest request,
                               HttpServletResponse response,
                               @PathVariable String idString)
        throws MonitorInterfaceException {

        this.enforceRole(SecurityConstants.ADMIN_ROLE, request, response);
        final ModelAndView result = new ModelAndView("message");
        final Map<String, String> requestParams 
            = this.getRequestParametersMap(request);
        final String inputDate = requestParams.get("maxDate");
        
        if (StringUtils.isBlank(inputDate)) {
            throw new MonitorInterfaceException(
                    "The maximum date is mandatory.", "log.maxDate.mandatory");
        }

        final Job job = this.getJob(idString);
        final LogManager logManager = new LogManager(job);
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        final Calendar maxDate = Calendar.getInstance();

        try {
            maxDate.setTime(dateFormat.parse(inputDate));

        } catch (ParseException e) {
            throw new MonitorInterfaceException(
                    "Maximum date format is invalid",
                    "log.maxDate.illegal");
        }

        logManager.purgeAggregateLogs(maxDate);
        logManager.purgeAggregateHourLogs(maxDate);
        result.addObject("message", "log.purge.success");
        return result;
    }



    /**
     * Shows the aggregate logs for a job.
     * <p>
     * Request parameters can be used to refine which log entries are 
     * fetched. See {@link 
     * LogSearchParams#createFromRequest(HttpServletRequest)} for more 
     * information.
     * 
     * @param   request                     the servlet request that asked for 
     *                                      displaying the logs
     * @param   response                    the response to the request
     * @param   idString                    the string that identifies the job.
     *                                      It can contain either its identifier
     *                                      or its name
     * @return                              an object containing the aggregate
     *                                      logs and which view must be used to
     *                                      display them
     * @throws  MonitorInterfaceException   the job doesn't exist
     * @see     LogSearchParams#createFromRequest(HttpServletRequest)
     */
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView show(HttpServletRequest request,
                             HttpServletResponse response, 
                             @PathVariable String idString)
        throws MonitorInterfaceException {

        final Map<String, String> requestParams
            = this.getRequestParametersMap(request);
        final LogSearchParams searchParams 
            = LogSearchParams.createFromParametersMap(requestParams);
        String viewName = "aggregLogsJson";
        final String altParam = request.getParameter("alt");
        final String contentType = request.getHeader("Content-Type");

        if ((null != altParam && altParam.equals("csv"))
            || (null != contentType && contentType.equals("text/csv"))) {
        	
        	if(requestParams.get("useSla") != null)
        	{
        		viewName = "aggregHourLogsCsv";
        	}else
        	{
        		viewName = "aggregLogsCsv";
        	}
        }else if(requestParams.get("useSla") != null)
        {
        	viewName = "aggregHourLogsJson";
        }

        
        final ModelAndView result = new ModelAndView(viewName);
        final Job job = this.getJobProtected(idString, request, response);
        final LogManager logManager = new LogManager(job);
        Map<Date, AbstractAggregateHourLogEntry> logHourEntries;
        Map<Date, AbstractAggregateLogEntry> logEntries;
        String slaName = "Default";
        if(requestParams.get("useSla") != null)
        {
        	// TODO with paging
        	logHourEntries = logManager.getAggregHourLogsSubset(searchParams.getMinDate(),searchParams.getMaxDate(), 
        			searchParams.getMaxResults(), searchParams.getStartIndex());
        	logHourEntries = LogSlaHelper.getAggHourLogForSla(requestParams.get("useSla"), logHourEntries);
        	
        	result.addObject("noPagingCount", 
                    logManager.getAggregateHourLogsItemsNumber(
                           searchParams.getMinDate(), searchParams.getMaxDate(), 
                           null, null));
        	result.addObject("message", "log.details.success");
        	result.addObject("aggregHourLogsCollection", logHourEntries.values());
        	slaName = Sla.getFromIdString(requestParams.get("useSla")).getName();
        }else
        {
        	logEntries = logManager.getAggregLogsSubset(searchParams.getMinDate(),
                                             searchParams.getMaxDate(),
                                             searchParams.getMaxResults(),
                                             searchParams.getStartIndex());
        	if (null == logEntries) {
                logEntries = new LinkedHashMap<Date, AbstractAggregateLogEntry>();
        	}
        	result.addObject("noPagingCount", logManager.getAggregateLogsItemsNumber(
                           searchParams.getMinDate(), searchParams.getMaxDate(), 
                           null, null));
        	result.addObject("message", "log.details.success");
        	result.addObject("aggregLogsCollection", logEntries.values());
       
        }
        
        if(requestParams.get("export") != null)
        {
        	result.addObject("getExport", true);
        	result.addObject("Jobname",job.getConfig().getJobName());
        	result.addObject("Queryname","");
        	result.addObject("Slaname",slaName);
        }
     	return result;
    }
}
