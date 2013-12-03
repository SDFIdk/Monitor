package org.easysdi.monitor.gui.webapp.controllers;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.TransformerException;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.easysdi.monitor.biz.job.Job;
import org.easysdi.monitor.biz.job.Query;
import org.easysdi.monitor.biz.logging.AbstractAggregateLogEntry;
import org.easysdi.monitor.biz.logging.LogManager;
import org.easysdi.monitor.gui.webapp.AppContext;
import org.easysdi.monitor.gui.webapp.LogSearchParams;
import org.easysdi.monitor.gui.webapp.MonitorInterfaceException;
import org.easysdi.monitor.gui.webapp.ReportExportHelper;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Servlet implementation class GroupReportExportController
 */
@Controller
@RequestMapping({ "/groups/{groupId}/aggLogs/export/{xsltId}" })
public class GroupAggLogExportController extends AbstractMonitorController {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GroupAggLogExportController() {
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
	protected void doGet(HttpServletRequest request, HttpServletResponse response, @PathVariable String groupId, @PathVariable String xsltId) throws ServletException, IOException, MonitorInterfaceException, TransformerException, NamingException {
    	
        final Map<String, String> requestParams = this.getRequestParametersMap(request);
        final LogSearchParams searchParams = LogSearchParams.createFromParametersMap(requestParams);
        Map<Date, AbstractAggregateLogEntry> logEntries;
        
        final ObjectMapper mapper = AppContext.getContext().getBean("jsonMapper", ObjectMapper.class);
        ArrayNode objNode = mapper.createArrayNode();
                
        String slaName = "Default";
        
    	final Job job = this.getJobProtected(groupId, request, response);
        
    	// Get log data for each request in the group
    	for(Query monRequest : job.getQueriesList()) {
    		final LogManager requestLogManager = new LogManager(monRequest);
    		    		    		
    		logEntries = requestLogManager.getAggregLogsSubset(searchParams.getMinDate(),
                    searchParams.getMaxDate(),
                    searchParams.getMaxResults(),
                    searchParams.getStartIndex());
    		
    		ArrayNode jsonLogsCollection = 
            	ReportExportHelper.CreateJSONData_AggLog(requestLogManager, logEntries, searchParams, 
            			job, monRequest, slaName);
    	
    		objNode.addAll(jsonLogsCollection);
    	}
    	
    	// Get summary logdata
    	final LogManager logManager = new LogManager(job);
        
        logEntries = logManager.getAggregLogsSubset(searchParams.getMinDate(),
                searchParams.getMaxDate(),
                searchParams.getMaxResults(),
                searchParams.getStartIndex());
        
        ArrayNode jsonLogsCollection = 
        	ReportExportHelper.CreateJSONData_AggLog(logManager, logEntries, searchParams, 
        			job, null, slaName);

        objNode.addAll(jsonLogsCollection);
        
    	String xml = ReportExportHelper.ConvertJSONReportDataToXml(objNode);
    	String dataOut = ReportExportHelper.DoTransformation(xml, xsltId);
    	    	
    	response.setContentType("application/octet-stream");
		response.addHeader("Content-disposition", "attachment; filename=" + 
				ReportExportHelper.GenerateXsltOutputFilename(xsltId));
    	
		javax.servlet.ServletOutputStream out = response.getOutputStream();
		out.print(dataOut);
		out.flush();
		
	}
}
