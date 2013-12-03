package org.easysdi.monitor.gui.webapp.controllers;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Set;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.TransformerException;

import org.easysdi.monitor.biz.job.Job;
import org.easysdi.monitor.biz.job.Query;
import org.easysdi.monitor.biz.job.Sla;
import org.easysdi.monitor.biz.logging.LogManager;
import org.easysdi.monitor.biz.logging.RawLogEntry;
import org.easysdi.monitor.gui.webapp.LogSearchParams;
import org.easysdi.monitor.gui.webapp.LogSlaHelper;
import org.easysdi.monitor.gui.webapp.MonitorInterfaceException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Servlet implementation class GroupReportStatExportController
 */
@Controller
@RequestMapping({ "/groups/{groupId}/logs/statexport" })
public class GroupRawLogStatExportController extends AbstractMonitorController {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GroupRawLogStatExportController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @throws MonitorInterfaceException 
	 * @throws TransformerException 
	 * @throws NamingException 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    @RequestMapping(method = RequestMethod.GET)
	protected void doGet(HttpServletRequest request, HttpServletResponse response, @PathVariable String groupId) throws ServletException, IOException, MonitorInterfaceException, TransformerException, NamingException {
    	
        final Map<String, String> requestParams = this.getRequestParametersMap(request);
        final LogSearchParams searchParams = LogSearchParams.createFromParametersMap(requestParams);
        Set<RawLogEntry> logEntries;
                                        
        String slaName;
        
        // Add CSV header
        StringBuilder outputCSV = new StringBuilder();
        outputCSV.append("RequestId;GroupName;RequestName;AvailablePct;UnavailablePct;FailedPct;NotTestedPct;UptimePct;AvailableCount;FailedCount;NotTestedCount;NormResponseTime;AvgResponseTime;SLAName;PeriodFrom;PeriodTo\r");
        
    	final Job job = this.getJobProtected(groupId, request, response);
    	
    	// Get log data for each request in the group
    	for(Query monRequest : job.getQueriesList()) {
    		final LogManager requestLogManager = new LogManager(monRequest);

    		slaName = "Default";
    		
    		logEntries = requestLogManager.getRawLogsSubset(searchParams.getMinDate(),
                    searchParams.getMaxDate(),
                    searchParams.getMaxResults(),
                    searchParams.getStartIndex());
    		
    		if(requestParams.get("useSla") != null)
            {
            	logEntries = LogSlaHelper.getRawlogForSla(requestParams.get("useSla"), logEntries,false);
            	slaName = Sla.getFromIdString(requestParams.get("useSla")).getName();
            }
    		    		
    		appendLogLine(searchParams, logEntries, slaName, outputCSV, job,
					monRequest);
    		    		
    	}
        
    	String dataOut = outputCSV.toString();
    	
    	response.setContentType("application/octet-stream");
		response.addHeader("Content-disposition", "attachment; filename=result.csv");
    	
		javax.servlet.ServletOutputStream out = response.getOutputStream();
		out.print(dataOut);
		out.flush();

	}

	private void appendLogLine(final LogSearchParams searchParams,
			Set<RawLogEntry> logEntries, String slaName,
			StringBuilder outputCSV, final Job job, Query monRequest) {
		
		float sumResponseTime = 0;
		int countAvailable = 0;
		int countUnavailable = 0;
		int countFailed = 0;
		int countNotTested = 0;
		int countAll = 0;
		
		for(RawLogEntry log: logEntries)
		{
			countAll += 1;
			
			sumResponseTime += log.getResponseDelay();
			if (log.getStatus().getValue().equals("AVAILABLE")) {
				countAvailable += 1;
			}
			if (log.getStatus().getValue().equals("UNAVAILABLE")) {
				countUnavailable += 1;
			}
			if (log.getStatus().getValue().equals("OUT_OF_ORDER")) {
				countFailed += 1;
			}
			if (log.getStatus().getValue().equals("NOT_TESTED")) {
				countNotTested += 1;
			}
		}

		long requestId = monRequest.getQueryId(); 
		String requestName = monRequest.getConfig().getQueryName();
		
		Float normResposeTime = null;
		if (monRequest.getQueryValidationSettings()!= null){
			normResposeTime = monRequest.getQueryValidationSettings().getNormTime();
		}
		if (normResposeTime == null) {normResposeTime = 0F;}
				
		String groupName = job.getConfig().getJobName();
		
		float pctAvailable = 0;
		float pctUnavailable = 0;
		float pctFailed = 0;
		float pctNotTested = 0;
		float pctUptime = 0;
		float avgResponseTime = 0;
		if (countAll > 0) {
			pctAvailable = ((float)countAvailable/(float)countAll)*100;
			pctUnavailable = ((float)countUnavailable/(float)countAll)*100;
			pctFailed = ((float)countFailed/(float)countAll)*100;
			pctNotTested = ((float)countNotTested/(float)countAll)*100;
			pctUptime = pctAvailable+pctFailed;
			avgResponseTime = (float)sumResponseTime/(float)countAll;
		}
		
		String minDateStr = "";
		if (searchParams.getMinDate()!=null){
			minDateStr = (new SimpleDateFormat("yyyy-MM-dd")).format(searchParams.getMinDate().getTime());
		}
		
		String maxDateStr = "";
		if (searchParams.getMaxDate()!=null){
			maxDateStr = (new SimpleDateFormat("yyyy-MM-dd")).format(searchParams.getMaxDate().getTime());
		}
				
		outputCSV.append(
				String.valueOf(requestId) + ";" +
				groupName + ";" +
				requestName + ";" +
				RoundFloat(pctAvailable, 2) + ";" +
				RoundFloat(pctUnavailable, 2) + ";" +
				RoundFloat(pctFailed, 2) + ";" +
				RoundFloat(pctNotTested, 2) + ";" +
				RoundFloat(pctUptime, 2) + ";" +
				String.valueOf((int)countAvailable) + ";" +
				String.valueOf((int)countFailed) + ";" +
				String.valueOf((int)countNotTested) + ";" +
				String.valueOf((int)normResposeTime.floatValue()) + ";" +
				String.valueOf((int)(avgResponseTime*1000)) + ";" +
				slaName + ";" +
				minDateStr + ";" +
				maxDateStr + "\r");
	}

	private String RoundFloat(float value, int precision)
	{
		BigDecimal bd = new BigDecimal(value);
		BigDecimal rounded = bd.setScale(precision, BigDecimal.ROUND_HALF_UP);
		DecimalFormat df = new DecimalFormat("0.00");
		return df.format(rounded.floatValue());
	}
	
}
