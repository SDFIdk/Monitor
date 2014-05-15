package org.easysdi.monitor.biz.logging;

//import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
//import java.io.InputStream;
//import java.util.Properties;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Set;

import javax.xml.parsers.*;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.w3c.dom.traversal.NodeIterator;

import com.sun.org.apache.xpath.internal.XPathAPI;

import org.apache.log4j.Logger;
import org.deegree.portal.owswatch.ServiceConfiguration;
import org.deegree.portal.owswatch.ServiceLog;
import org.deegree.portal.owswatch.Status;
import org.deegree.portal.owswatch.ValidatorResponse;
import org.easysdi.monitor.biz.alert.AbstractAction;
import org.easysdi.monitor.biz.alert.Alert;
import org.easysdi.monitor.biz.alert.EmailAction;
import org.easysdi.monitor.biz.job.Job;
import org.easysdi.monitor.biz.job.JobConfiguration;
import org.easysdi.monitor.biz.job.QueryResult;
import org.easysdi.monitor.biz.job.QueryTestResult;
import org.easysdi.monitor.biz.job.QueryValidationResult;
import org.easysdi.monitor.biz.job.QueryValidationSettings;
import org.easysdi.monitor.dat.dao.LogDaoHelper;
import org.easysdi.monitor.biz.job.OverviewLastQueryResult;
import org.easysdi.monitor.biz.job.Status.StatusValue;
import org.easysdi.monitor.dat.dao.LastLogDaoHelper;

/**
 * Processes the result of a owsWatch query polling.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.1, 2010-04-30
 *
 */
public class MonitorServiceLog extends ServiceLog {

    private static final long serialVersionUID = 5207213371103847912L;
    
    private final Logger logger = Logger.getLogger(MonitorServiceLog.class);

    private boolean           resultLogging;
    private boolean           saveTestResult = false;
    private QueryResult       lastResult;
    
	private static HashMap<Long,String> last_status = new HashMap<Long,String>();



    /**
     * Creates a new logger for Monitor queries.
     * 
     * @throws  IOException the paths are invalid.
     *                      <p>
     *                      This is an exception that may be thrown by the 
     *                      subclass. It should never happen with this class.
     */
    public MonitorServiceLog() throws IOException {
        super("", -1, "", "", "", null);

        this.setResultLogged(true);
    }



    /**
     * Processes a new polling result.
     * 
     * @param   response        the query response produced by owsWatch
     * @param   serviceConfig   the owsWatch test configuration
     */
    @Override
    public void addMessage(ValidatorResponse response,
                           ServiceConfiguration serviceConfig) {
        final QueryResult result 
            = new QueryResult(serviceConfig.getServiceid(), 
                              serviceConfig.getServiceName(), response,
                              serviceConfig.createHttpRequest(), 
                              serviceConfig.getHttpMethod());
    
        Float deliveryTime = null;
		long responseSize = 0;
		
	
        this.setLastResult(result);

        if (this.isResultLogged()) {
            final RawLogEntry logEntry = result.createRawLogEntry();
                   
            
			boolean error = false;
			if (response.getStatus() != Status.RESULT_STATE_AVAILABLE) {
				// Error
				error = true;
			}
			
			String requestType = serviceConfig.getRequestType().toLowerCase();
			
			long queryID = result.getQueryId();

			// Does a log already exist
			OverviewLastQueryResult lastQueryEntry = LastLogDaoHelper.getLastLogDao().exist(queryID);

			if (lastQueryEntry == null) {
				lastQueryEntry = new OverviewLastQueryResult();
				lastQueryEntry.setQueryid(queryID);
			}
			deliveryTime=logEntry.getResponseDelay();

			// Error request
			if (error) {
				lastQueryEntry.setTextResult(response.getMessage());
				lastQueryEntry.setData(response.getData());
				lastQueryEntry.setContentType(response.getContentType());
			} else {
				if (requestType.equalsIgnoreCase("getmap") || requestType.equalsIgnoreCase("gettile") ||
						requestType.equalsIgnoreCase("FTP")) {
					lastQueryEntry.setData(response.getData());
					lastQueryEntry.setContentType(response.getContentType());
					// TO check response size for different image types
					responseSize = response.getResponseLength();
				}else
				{
					responseSize = response.getData() != null? response.getData().length: 0;
					lastQueryEntry.setData(response.getData());
					lastQueryEntry.setContentType(response.getContentType());
				}

				QueryValidationResult validationResult = result.getParentQuery().getQueryValidationResult();
				if(validationResult == null)
				{
					validationResult = new QueryValidationResult();
					validationResult.setQueryId(result.getParentQuery().getQueryId());
				}
				validationResult.setSizeValidationResult(true);
				validationResult.setTimeValidationResult(true);
				validationResult.setXpathValidationResult(true);

				QueryValidationSettings validationSettings = result.getParentQuery().getQueryValidationSettings();
				if(validationSettings != null)
				{
					// 1) Time validation
					if(validationSettings.isUseTimeValidation() && validationSettings.getNormTime() != null)
					{
						if(validationSettings.getNormTime() < (deliveryTime *1000)){
							logEntry.setMessage("Time validation fail");
							logEntry.setStatus(StatusValue.OUT_OF_ORDER);
							validationResult.setTimeValidationResult(false);
						}
					}
					
					// 2) Size validation 
					if(validationSettings.isUseSizeValidation() && validationSettings.getNormSize() != 0)
					{
						float max = validationSettings.getNormSize() + (validationSettings.getNormSize() * validationSettings.getNormSizeTolerance() / 100); 
						float min = validationSettings.getNormSize() - (validationSettings.getNormSize() * validationSettings.getNormSizeTolerance() / 100);
						if(responseSize < min || responseSize > max){
							logEntry.setMessage("Size validation fail");
							logEntry.setStatus(StatusValue.UNAVAILABLE);
							validationResult.setSizeValidationResult(false);
						}			
					}
				
					// 3) XPath validation
					if(validationSettings.isUseXpathValidation() && validationSettings.getExpectedXpathOutput() != null)
					{
						String xpathValidationOutput = "";
						try
						{
							xpathValidationOutput = xpathValidation(response.getData(),validationSettings.getXpathExpression());

						}catch(Exception e)
						{
							logEntry.setMessage("Xpath evaluation failed");
							logEntry.setStatus(StatusValue.UNAVAILABLE);
							xpathValidationOutput = "Xpath evaluation failed";
							validationResult.setXpathValidationResult(false);
						}

						validationResult.setXpathValidationOutput(xpathValidationOutput);
						if(validationSettings.getExpectedXpathOutput().compareTo(xpathValidationOutput)!= 0){
							logEntry.setMessage(xpathValidationOutput);
							logEntry.setStatus(StatusValue.UNAVAILABLE);
							validationResult.setXpathValidationResult(false);
						}
					}
					
					// 4) Text validation
					if(validationSettings.isUseTextValidation() && validationSettings.getExpectedTextMatch() != null){
						
						boolean valResult = textValidation(response.getData(), validationSettings.getExpectedTextMatch());
						
						validationResult.setXpathValidationResult(valResult);
						if (valResult){
							validationResult.setXpathValidationOutput("MATCH FOUND");
						} else {
							validationResult.setXpathValidationOutput("NO MATCH FOUND");
							logEntry.setStatus(StatusValue.UNAVAILABLE);
						}
					}
				}
				validationResult.setResponseSize(responseSize);
				validationResult.setDeliveryTime(deliveryTime);	
						
				try
				{	
					// Create/Update validation result
					if(!validationResult.persist())
					{
						this.logger.error("An exception was thrown while saving validationResult");
					}
				}catch(Exception e)
				{
					this.logger.error("An exception was thrown while saving validationResult: "+e.getMessage());
				}
			}
			
			try
			{
				JobConfiguration jobConfig = result.getParentQuery().getConfig().getParentJob().getConfig();
				if(jobConfig.isAlertsActivated() && logEntry.getStatusValue().equals((StatusValue.OUT_OF_ORDER)))
				{			
					final Alert alert = Alert.create(result.getParentQuery().getConfig().getParentJob().getStatusValue() ,
						logEntry.getStatusValue(), logEntry.getMessage(), logEntry.getResponseDelay(),null 
						,result.getParentQuery().getConfig().getParentJob(),response.getData(),response.getContentType());
					this.triggerActions(alert,result.getParentQuery().getConfig().getParentJob());
				}
			}catch(Exception e)
			{
				this.logger.error("An exception was thrown while saving alert: "+e.getMessage());
			}
			
			//System.out.println("BEFORE SAVE: "+logEntry.getQueryId()+" "+logEntry.getRequestTime());
			// Save raw log	PROBLEM HERE WITH MANY REQUEST
			if (!LogDaoHelper.getLogDao().persistRawLog(logEntry)) {
                this.logger.error("An exception was thrown while saving a log entry");
            }
            
        	// Save or update last log
			if (!LastLogDaoHelper.getLastLogDao().create(lastQueryEntry)) {
				this.logger.error("An exception was thrown while saving a last log entry");
			}
			
			try
	        {
				StatusValue svalue = logEntry.getStatus().getStatusValue();
				if(response.getStatus() != Status.RESULT_STATE_AVAILABLE || svalue.equals(StatusValue.UNAVAILABLE) || svalue.equals(StatusValue.OUT_OF_ORDER))
				{
					EmailAction action = new EmailAction();
					boolean cancel = false;
					if(action.config.getOutOfOder().equalsIgnoreCase("false") && svalue.equals(StatusValue.OUT_OF_ORDER))
					{
						cancel = true;
					}
					if(!cancel && sendMailStatus(logEntry.getQueryId(),true))
					{
			        	if(action.config.getQuerymail().equalsIgnoreCase("true"))
			        	{
			        		String time = "";
			        		try
			        		{
			        			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			        			time = dateFormat.format(result.getRequestTime().getTime());
			        		}catch(Exception ex)
			        		{
			        		}
			        		action.sendAlertJobMail(result.getParentQuery().getConfig().getParentJob(),result.getParentQuery(),time,svalue.name());
			        	}
					}
	        	}else if (response.getStatus() == Status.RESULT_STATE_AVAILABLE || svalue.equals(StatusValue.AVAILABLE))
	        	{
	        		// Send mail if status has changed
	        		if(sendMailStatus(logEntry.getQueryId(),false))
	        		{
	        			EmailAction action = new EmailAction();
			        	if(action.config.getQuerymail().equalsIgnoreCase("true"))
			        	{
			        		String time = "";
			        		try
			        		{
			        			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			        			time = dateFormat.format(result.getRequestTime().getTime());
			        		}catch(Exception ex)
			        		{	
			        		}
			        		action.sendAlertJobMail(result.getParentQuery().getConfig().getParentJob(),result.getParentQuery(),time,"AVAILABLE");
			        	}
	        		}
	        	}
	        }catch(Exception ex)
	        {
	        	// Error
	        }
			
        }else
        {
        	if(this.saveTestResult)
        	{
        		QueryTestResult resultTest = new QueryTestResult();
        		QueryValidationSettings validationSettings = result.getParentQuery().getQueryValidationSettings();
        		resultTest.setQueryid(result.getQueryId());
        		resultTest.setData(response.getData());
        		resultTest.setContentType(response.getContentType());
        		resultTest.setResponseDelay(result.getResponseDelay());
        		if (serviceConfig.getRequestType().equalsIgnoreCase("getmap") || serviceConfig.getRequestType().equalsIgnoreCase("FTP") || serviceConfig.getRequestType().equalsIgnoreCase("gettile")) {
					responseSize =  response.getResponseLength();
				}else
				{
					responseSize = response.getData() != null? response.getData().length: 0;
				}
        		resultTest.setResponseSize(responseSize);
        	
        		if(validationSettings != null && validationSettings.isUseXpathValidation())
        		{	
        			resultTest.setXpathresult(xpathValidation(response.getData(),validationSettings.getXpathExpression()));
        		}
        		else if(validationSettings != null && validationSettings.isUseTextValidation()) 
        		{
        			boolean valResult = textValidation(response.getData(), validationSettings.getExpectedTextMatch());
        			if (valResult) {
        				resultTest.setXpathresult("MATCH FOUND");
        			} else {
        				resultTest.setXpathresult("MATCH NOT FOUND");
        			}
        		}	
        		else
        		{
        			resultTest.setXpathresult("");
        		}
        		if(!resultTest.saveTestResult())
        		{
        			 this.logger.error("An exception was thrown while saving a query run test");
        		}
        	}
        }
    }
    
    private boolean sendMailStatus(long queryID, boolean failed)
    {
    	String value = last_status.get(queryID);
    	// System.out.println(last_status.size()+" "+value+" "+failed);
      	// System.out.println("");
    	if(value != null)
    	{
    		if(failed)
    		{
    			return false;
    		}else
    		{
    			last_status.remove(queryID);
    			return true;
    		}
    	}else
    	{
    		if(failed)
    		{
    			last_status.put(queryID,"error");
    			return true;
    		}else
    		{
    			return false;
    		}
    	}
    }
    
    private void triggerActions(Alert alert,Job parentJob) {
        final Set<AbstractAction> actionsSet = parentJob.getActions();

        if (null != actionsSet) {

            for (AbstractAction action : actionsSet) {
                action.trigger(alert);
            }
        }
    }
    
    /**
     * Validate a xml for an expression
     * @param xml
     * @param expression
     * @return
     */
    private String xpathValidation(byte[] xml,String expression)
    {
    	String xpathValidationOutput = "";
    	try
		{
    		String xmlStr = new String(xml);
    		DocumentBuilderFactory xmlFact = DocumentBuilderFactory.newInstance();
            xmlFact.setNamespaceAware(false);
            InputSource inputSource = new InputSource(new StringReader(xmlStr));
            Document doc = xmlFact.newDocumentBuilder().parse(inputSource);
			
            NodeIterator nl = XPathAPI.selectNodeIterator(doc, expression);
    		xpathValidationOutput = nl.nextNode().getFirstChild().getNodeValue();
            
		}catch(Exception e)
		{
			xpathValidationOutput = "Xpath evaluation failed";
		}
    	return xpathValidationOutput;
    }

    /**
     * Validate if response contains specific text 
     * @param response
     * @param expectedResult
     * @return
     */
    private boolean textValidation(byte[] response, String expectedResult){
    	
    	boolean found = false;
    	try
    	{
    		String responseText = new String(response);
    		found = responseText.contains(expectedResult);
    	}
    	catch(Exception e)
    	{
    		found = false;
    	}
    	return found;
    }
    
    /**
     * Defines whether the query results must be logged.
     * 
     * @param   newResultLogging    <code>true</code> if the next result must be
     *                              logged
     */
    public void setResultLogged(boolean newResultLogging) {
        this.resultLogging = newResultLogging;
    }



    /**
     * Gets if the query results are logged.
     * 
     * @return <code>true</code> if the query result are logged
     */
    private boolean isResultLogged() {
        return this.resultLogging;
    }



    /**
     * Defines the last query result to date.
     * 
     * @param   result  the latest query result
     */
    private void setLastResult(QueryResult result) {

        this.lastResult = result;

    }



    /**
     * Gets the last query result.
     * 
     * @return  the query result
     */
    public QueryResult getLastResult() {

        return this.lastResult;

    }



	/**
	 * Checks if query test result for a non auto should be saved
	 * @return the saveTestResult
	 */
	public boolean isSaveTestResult() {
		return saveTestResult;
	}



	/**
	 * Sets if a non auto query test result should be saved
	 * @param saveTestResult the saveTestResult to set
	 */
	public void setSaveTestResult(boolean saveTestResult) {
		this.saveTestResult = saveTestResult;
	}
    

    
}
