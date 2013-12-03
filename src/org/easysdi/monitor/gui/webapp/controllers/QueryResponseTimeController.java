package org.easysdi.monitor.gui.webapp.controllers;

import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.easysdi.monitor.biz.job.Query;
import org.easysdi.monitor.biz.logging.LogManager;
import org.easysdi.monitor.biz.logging.RawLogEntry;
import org.easysdi.monitor.gui.webapp.LogSearchParams;
import org.easysdi.monitor.gui.webapp.MonitorInterfaceException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Processes the requests concerning the response times of a query.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
@Controller
@RequestMapping({ "/jobs/{jobIdString}/queries/{queryIdString}/respTime", 
                  "/adminJobs/{jobIdString}/queries/{queryIdString}/respTime" })
public class QueryResponseTimeController extends AbstractMonitorController {
    
    /**
     * Creates a new query response times controller. 
     */
    public QueryResponseTimeController() {
        
    }
    
    

    /**
     * Displays the response times for a query.
     * <p>
     * Request parameters can be used to refine which response times are 
     * fetched. See {@link 
     * LogSearchParams#createFromRequest(HttpServletRequest)} for more 
     * information.
     * 
     * @param   request         the servlet request that asked for displaying
     *                          the response times
     * @param   response        the response to the request
     * @param   jobIdString     the string that identifies the parent job of the
     *                          query. It can contain either the identifier or 
     *                          the name of the job 
     * @param   queryIdString   the string that identifies the query. It can 
     *                          contain either its identifier or its name
     * @return                  an object containing the response times and 
     *                          which view must be used to display them
     * @throws  MonitorInterfaceException   the query doesn't exist or isn't
     *                                      visible by the current user
     * @see     LogSearchParams#createFromRequest(HttpServletRequest)
     */
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView show(HttpServletRequest request,
                             HttpServletResponse response,
                             @PathVariable String jobIdString,
                             @PathVariable String queryIdString)
        throws MonitorInterfaceException {

        final Map<String, String> requestParams
            = this.getRequestParametersMap(request);
        final LogSearchParams searchParams 
            = LogSearchParams.createFromParametersMap(requestParams);
        String viewName = "respTimeJson";
        final String altParam = requestParams.get("alt");
        final String contentType = request.getHeader("Content-Type");

        if ((null != altParam && altParam.equals("csv"))
            || (null != contentType && contentType.equals("text/csv"))) {

            viewName = "respTimeCsv";
        }

        final ModelAndView result = new ModelAndView(viewName);

        final Query query = this.getQueryProtected(jobIdString, queryIdString, 
                                                   request, response);
        final LogManager logManager = new LogManager(query);
        final Set<RawLogEntry> rawLogs = 
            logManager.getRawLogsSubset(searchParams.getMinDate(),
                                        searchParams.getMaxDate(),
                                        searchParams.getMaxResults(),
                                        searchParams.getStartIndex());

        result.addObject("message", "reponseTimes.details.success");
        result.addObject("rawLogsCollection", rawLogs);
        
        result.addObject("noPagingCount", 
                 logManager.getRawLogsItemsNumber(searchParams.getMinDate(), 
                                                  searchParams.getMaxDate(), 
                                                  null, null));
        result.addObject("addQueryId", false);

        return result;
    }
}
