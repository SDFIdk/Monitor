package org.easysdi.monitor.gui.webapp.controllers;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import org.easysdi.monitor.biz.job.Job;
import org.easysdi.monitor.biz.job.Query;
import org.easysdi.monitor.biz.job.Sla;
import org.easysdi.monitor.biz.logging.LogManager;
import org.easysdi.monitor.biz.logging.RawLogEntry;
import org.easysdi.monitor.gui.webapp.AppContext;
import org.easysdi.monitor.gui.webapp.LogSearchParams;
import org.easysdi.monitor.gui.webapp.LogSlaHelper;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * Servlet implementation class ServiceApiGroupStatController
 */
@Controller
@RequestMapping({ "/serviceapi/groups/{groupId}/stats" })
public class ServiceApiGroupStatController extends AbstractMonitorController {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServiceApiGroupStatController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @throws Exception 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    @RequestMapping(method = RequestMethod.GET)
	protected void doGet(HttpServletRequest request, HttpServletResponse response, @PathVariable String groupId) throws Exception {
    	
        final Map<String, String> requestParams = this.getRequestParametersMap(request);
        final LogSearchParams searchParams = LogSearchParams.createFromParametersMap(requestParams);
        Set<RawLogEntry> logEntries;
                
        String dataOut;
        
        // Check if JSONP should be used
        String jsonp = "";
        if (requestParams.get("jsonpCallback") != null) {
        	jsonp=requestParams.get("jsonpCallback");
        }
        
        String slaName;
                
        final ObjectMapper mapper = AppContext.getContext().getBean("jsonMapper", ObjectMapper.class);
        final ArrayNode rowsCollection = mapper.createArrayNode();
        
        try 
        {
        
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
	    		    		
	    		rowsCollection.add(appendLogLine(searchParams, logEntries, slaName, mapper, job, monRequest));
	    		    		
	    	}

	    	ArrayNode objNode = mapper.createArrayNode();

	    	objNode.addAll(rowsCollection);

	    	final ObjectNode jsonEntry = mapper.createObjectNode();
	    	jsonEntry.put("success", true);
	    	jsonEntry.put("message", "Statistic data for group " + job.getConfig().getJobName() + " retrieved successfully");
	    	jsonEntry.put("data", objNode);

	    	dataOut = jsonEntry.toString();
        } 
        catch (Exception ex) 
        {
        	dataOut = CreateErrorMessage(ex.getMessage(), mapper);
        }

        if (!jsonp.equals("")) {
    		dataOut = jsonp + "(" + dataOut + ");";
    		response.setContentType("application/javascript");
    	} else {
        	response.setContentType("application/json");
    	}

		javax.servlet.ServletOutputStream out = response.getOutputStream();
		out.print(dataOut);
		out.flush();
	}

    private String CreateErrorMessage(String message, ObjectMapper mapper) {
    	final ObjectNode jsonEntry = mapper.createObjectNode();
    	ObjectNode noDataNode = mapper.createObjectNode();
    	jsonEntry.put("success", false);
    	jsonEntry.put("message", message);
    	jsonEntry.put("data", noDataNode);
    	return jsonEntry.toString();
    }
    
	private JsonNode appendLogLine(final LogSearchParams searchParams,
			Set<RawLogEntry> logEntries, String slaName,
			ObjectMapper mapper, final Job job, Query monRequest) {
		
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
		float avgResponseTime = 0;
		float pctUptime = 0;
		if (countAll > 0) {
			pctAvailable = ((float)countAvailable/(float)countAll)*100;
			pctUnavailable = ((float)countUnavailable/(float)countAll)*100;
			pctFailed = ((float)countFailed/(float)countAll)*100;
			pctNotTested = ((float)countNotTested/(float)countAll)*100;
			avgResponseTime = (float)sumResponseTime/(float)countAll;
			pctUptime = pctAvailable+pctFailed;
		}
		
		String minDateStr = "";
		if (searchParams.getMinDate()!=null){
			minDateStr = (new SimpleDateFormat("yyyy-MM-dd")).format(searchParams.getMinDate().getTime());
		}
		
		String maxDateStr = "";
		if (searchParams.getMaxDate()!=null){
			maxDateStr = (new SimpleDateFormat("yyyy-MM-dd")).format(searchParams.getMaxDate().getTime());
		}

		final ObjectNode jsonEntry = mapper.createObjectNode();
		
		jsonEntry.put("RequestId", String.valueOf(requestId));
		jsonEntry.put("GroupName", groupName);
		jsonEntry.put("RequestName", requestName);
		jsonEntry.put("AvailablePct", RoundFloat(pctAvailable, 2));
		jsonEntry.put("UnavailablePct", RoundFloat(pctUnavailable, 2));
		jsonEntry.put("FailedPct", RoundFloat(pctFailed, 2));
		jsonEntry.put("NotTestedPct", RoundFloat(pctNotTested, 2));
		jsonEntry.put("UpTimePct", RoundFloat(pctUptime, 2));
		jsonEntry.put("AvailableCount", String.valueOf((int)countAvailable));
		jsonEntry.put("FailedCount", String.valueOf((int)countFailed));
		jsonEntry.put("NotTestedCount", String.valueOf((int)countNotTested));
		jsonEntry.put("NormResponseTime", String.valueOf((int)normResposeTime.floatValue()));
		jsonEntry.put("AvgResponseTime", String.valueOf((int)(avgResponseTime*1000)));
		jsonEntry.put("SLAName", slaName);
		jsonEntry.put("PeriodFrom", minDateStr);
		jsonEntry.put("PeriodTo", maxDateStr);
			
		return jsonEntry;
		
	}

	private String RoundFloat(float value, int precision)
	{
		BigDecimal bd = new BigDecimal(value);
		BigDecimal rounded = bd.setScale(precision, BigDecimal.ROUND_HALF_UP);
		DecimalFormat df = new DecimalFormat("0.00");
		return df.format(rounded.floatValue());
	}
	
}
