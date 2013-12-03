package org.easysdi.monitor.gui.webapp.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.easysdi.monitor.biz.job.Job;
import org.easysdi.monitor.biz.logging.LogManager;
import org.easysdi.monitor.gui.webapp.LogSearchParams;
import org.easysdi.monitor.gui.webapp.MonitorInterfaceException;
import org.easysdi.monitor.gui.webapp.security.SecurityConstants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Processes the requests concerning the raw logs collection for a job.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
@Controller
@RequestMapping({ "/jobs/{idString}/logs", "/adminJobs/{idString}/logs" })
public class JobRawLogController extends AbstractMonitorController {

    /**
     * Creates a new controller.
     */
    public JobRawLogController() {
        
    }
    
    
    
    /**
     * Deletes a job's raw logs up to a date.
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
     * @param   idString                    the string that identifies the 
     *                                      parent job. It can contain its 
     *                                      identifier or its name
     * @return                              an object containing a success 
     *                                      message and which view must be used
     *                                      to display it
     * @throws  MonitorInterfaceException   <ul>
     *                                      <li>the job doesn't exist</li>
     *                                      <li>no maximum date has been given
     *                                      </li>
     *                                      <li>the maximum date's format is
     *                                      invalid. It must be YYYY-MM-DD</li>
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
                    "Maximum date format is invalid", "log.maxDate.illegal");
        }

        logManager.purgeRawLogs(maxDate);

        result.addObject("message", "log.purge.success");
        
        return result;
    }



    /**
     * Shows the raw logs for a job.
     * <p>
     * Request parameters can be used to refine which log entries are 
     * fetched. See {@link 
     * LogSearchParams#createFromRequest(HttpServletRequest)} for more 
     * information.
     * 
     * @param   request                     the request that asked for 
     *                                      displaying the job's raw logs
     * @param   response                    the response to the request
     * @param   idString                    the string that identifies the 
     *                                      parent job. It can contain its 
     *                                      identifier or its name
     * @return                              an object containing the requested
     *                                      raw logs collection and which view
     *                                      must be used to display it
     * @throws  MonitorInterfaceException   the parent job doesn't exist
     * @see     LogSearchParams#createFromRequest(HttpServletRequest)   
     */
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView show(HttpServletRequest request,
                             HttpServletResponse response, 
                             @PathVariable String idString)
        throws MonitorInterfaceException {

    	
    	
    	
        return this.generateJobRawLogResult(request, response, idString);
    }
}
