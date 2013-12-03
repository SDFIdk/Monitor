package org.easysdi.monitor.biz.job;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;



import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.deegree.framework.util.StringTools;
import org.deegree.ogcwebservices.OGCWebServiceException;
import org.deegree.portal.owswatch.ServiceConfiguration;
import org.deegree.portal.owswatch.ServiceInvoker;
import org.deegree.portal.owswatch.ServiceLog;
import org.deegree.portal.owswatch.Status;
import org.deegree.portal.owswatch.ValidatorResponse;


public class CustomQueryInvoker extends ServiceInvoker{

	public CustomQueryInvoker(ServiceConfiguration serviceconfig,
			ServiceLog serviceLog) {
		super(serviceconfig, serviceLog);
		// TODO Auto-generated constructor stub
	}

	private InputStream soap_stream_response;
	private String ContentType;
	
	// Used for multi threading
	private long startTimeSync;
	private HttpClient client_SimRun;
	private HttpMethodBase method_SimRun;
	private String ogcWebServiceExceptionMsg = "";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1627199938391138452L;
	
	  /**
     * Executes the test in the main thread
     *
     */
    public void executeTest() {

        ValidatorResponse tmpResponse = null;
        try {
        	
            HttpMethodBase method = this.getServiceConfig().getHttpMethodBase();
        	tmpResponse = executeHttpMethod(method, this.getServiceConfig().getUserCreds());
        } catch ( OGCWebServiceException e ) {
            tmpResponse = new ValidatorResponse( "Page Unavailable: " + e.getLocalizedMessage(),Status.RESULT_STATE_PAGE_UNAVAILABLE );
            tmpResponse.setLastLapse( -1 );
            tmpResponse.setLastTest( Calendar.getInstance().getTime() );
        }
        this.getServiceLog().addMessage( tmpResponse, this.getServiceConfig() );
    }
    

	protected ValidatorResponse executeHttpMethod( HttpMethodBase method, Credentials creds)
	throws OGCWebServiceException {

		HttpClient client = new HttpClient();
		HttpConnectionManagerParams cmParams = client.getHttpConnectionManager().getParams();
		cmParams.setConnectionTimeout( this.getServiceConfig().getTimeout() * 1000 );
		client.getHttpConnectionManager().setParams( cmParams );
		// Provide custom retry handler is necessary
		if(method !=null) // in case of a soap request we are not using httpmethodbase but a an http connection class.
			method.getParams().setParameter( HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler( 2, false ) );

		if (null != creds) {

			try {
				URI methodUri = method.getURI();
				client.getState().setCredentials(new AuthScope(methodUri.getHost(), methodUri.getPort()), creds);
			} catch (URIException e) {
				System.err.println(String.format("An exception was thrown while getting HTTP method URI : %1$s", 
						e.getMessage()));
			}
		}

		ValidatorResponse response = null;
		try {
			int statusCode =0;
			long startTime = System.currentTimeMillis();
			String queryHttpMethodType = this.getServiceConfig().getHttpMethod();
			String queryRequestMethodName = this.getServiceConfig().getProperties().getProperty("REQUEST");
			String queryServiceType = this.getServiceConfig().getServiceType();
				
				if(queryServiceType.equalsIgnoreCase(CustomQueryConstants.ALL)){
					
					if(queryHttpMethodType.equalsIgnoreCase(CustomQueryConstants.GET) && queryRequestMethodName.equals(CustomQueryConstants.HTTP_GET))
						statusCode = executeSimpleGetRequest(method, client);
					else if(queryHttpMethodType.equalsIgnoreCase(CustomQueryConstants.POST) && queryRequestMethodName.equals(CustomQueryConstants.HTTP_POST))
						statusCode = executeSimplePostRequest(method, client);
					else if(queryHttpMethodType.equalsIgnoreCase(CustomQueryConstants.POST) && queryRequestMethodName.contains(CustomQueryConstants.SOAP_1_1))							
						statusCode = executeSimpleSOAP_1_1_Client();
					else if(queryHttpMethodType.equalsIgnoreCase(CustomQueryConstants.POST) && queryRequestMethodName.contains(CustomQueryConstants.SOAP_1_2))					
						statusCode = executeSimpleSOAP_1_2_Client();
					else
						throw new OGCWebServiceException( "Unknown query requested. Execution not implemented"+
								"\n HHTP Method :"+queryHttpMethodType +
								"\n SERVICE Type  :" +queryServiceType +
								"\n Request Name  :" +queryRequestMethodName
								
						);
				}
			long lapse = System.currentTimeMillis() - startTime;
			if((queryServiceType.equalsIgnoreCase(CustomQueryConstants.ALL)) && (!queryRequestMethodName.contains(CustomQueryConstants.SOAP))){
				response = ((CustomValidator) this.getServiceConfig().getValidator()).validateAnswer( method, statusCode );
			
			}else{
				response = ((CustomValidator) this.getServiceConfig().getValidator()).validateAnswer(ContentType, soap_stream_response, statusCode );	
			}
		
			response.setLastLapse( lapse );
			Calendar date = Calendar.getInstance();
			date.setTimeInMillis( startTime );
			response.setLastTest( date.getTime() );
		} catch ( Exception e ) {
			throw new OGCWebServiceException( e.getLocalizedMessage() );
		} finally {
			if(null !=method)
			method.releaseConnection();
		}
		return response;
	}

	private int executeSimplePostRequest(HttpMethodBase method, HttpClient client) throws HttpException, IOException{
		
		return client.executeMethod( method );
	} 

	private int executeSimpleGetRequest(HttpMethodBase method, HttpClient client) throws HttpException, IOException{
		
		return client.executeMethod( method );
	} 
	
	private String getSoapEnvelope(){
		// the last param added contains the soap envelope.
		return this.getServiceConfig().getProperties().getProperty("soapenvelope");
	}
	
	private int executeSimpleSOAP_1_1_Client() throws IOException{
		
		int statusCode = 0 ;
		 HttpURLConnection con  = null;
		 try {
			
			 String soapAction = this.getServiceConfig().getProperties().getProperty(CustomQueryConstants.SOAPURL);
			 URL oURL = new URL(this.getServiceConfig().getOnlineResource());
			 con = (HttpURLConnection) oURL.openConnection();
			 con.setRequestMethod("POST");
			 con.setRequestProperty(
			    "Content-type", "text/xml; charset=utf-8");
			 con.setRequestProperty("SOAPAction", soapAction);
			 con.setDoOutput(true);
			 con.setDoInput(true);

			 // Posting the SOAP request XML message
			 OutputStream reqStream = con.getOutputStream();
			 reqStream.write(getSoapEnvelope().getBytes());
			 reqStream.flush();

			 // Reading the SOAP response XML message

			 soap_stream_response = con.getInputStream();
			

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			soap_stream_response=null; 
			
		}finally{
			 statusCode = ((HttpURLConnection) con).getResponseCode();
			 ContentType  =  ((HttpURLConnection) con).getHeaderField("Content-Type");

		}
		
		return statusCode;

		
	}
	
	private int executeSimpleSOAP_1_2_Client() throws IOException{
			
			int statusCode = 0 ;
			 HttpURLConnection con  = null;
			 try {
				 URL oURL = new URL(this.getServiceConfig().getOnlineResource());
				 con = (HttpURLConnection) oURL.openConnection();
				 con.setRequestMethod("POST");
				 //    "Content-type", "text/xml; charset=utf-8");
				 //con.setRequestProperty("Content-type", "text/xml; charset=utf-8");
				 con.setRequestProperty("Content-type", "application/soap+xml; charset=utf-8");	
				 con.setDoOutput(true);
				 con.setDoInput(true);	
				 // Posting the SOAP request XML message
				 OutputStream reqStream = con.getOutputStream();
				 
				 reqStream.write(getSoapEnvelope().getBytes());
				 reqStream.flush();
	
				 // Reading the SOAP response XML message
	
				 soap_stream_response = con.getInputStream();
				
	
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				soap_stream_response=null; 
				
			}finally{
				 statusCode = ((HttpURLConnection) con).getResponseCode();
				 ContentType  =  ((HttpURLConnection) con).getHeaderField("Content-Type");
	
			}
			
			return statusCode;
	
			
		}

	/**
	 * Method: setupHTTPClient
	 * Build the http request
	 * Used for multi threading 
	 */
    @Override
	public void setupHTTPClient()
	{
	  	try
    	{
    		this.method_SimRun = this.getServiceConfig().getHttpMethodBase();
    	}catch(OGCWebServiceException e)
    	{
    		this.ogcWebServiceExceptionMsg = e.getLocalizedMessage();
    	}
    	Credentials creds = this.getServiceConfig().getUserCreds();
		
    	HttpClient client = new HttpClient();
		HttpConnectionManagerParams cmParams = client.getHttpConnectionManager().getParams();
		cmParams.setConnectionTimeout( this.getServiceConfig().getTimeout() * 1000 );
		client.getHttpConnectionManager().setParams( cmParams );
		// Provide custom retry handler is necessary
		if(this.method_SimRun !=null) // in case of a soap request we are not using httpmethodbase but a an http connection class.
			this.method_SimRun.getParams().setParameter( HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler( 2, false ) );

		if (null != creds) {

			try {
				URI methodUri =this.method_SimRun.getURI();
				client.getState().setCredentials(new AuthScope(methodUri.getHost(), methodUri.getPort()), creds);
			} catch (URIException e) {
				System.err.println(String.format("An exception was thrown while getting HTTP method URI : %1$s", 
						e.getMessage()));
			}
		}
		this.client_SimRun = client;  
	}
	
    /**
     * Method executeHttpMethodSimRun
     * Execute the SOAP request
     * Used for multi threading
     */
    @Override
	public void executeHttpMethodSimRun()
	{
		// CALLED when running multithread
		ValidatorResponse response = null;
		try {
			 if(!StringTools.isNullOrEmpty(this.ogcWebServiceExceptionMsg))
        	 {
        		 throw new OGCWebServiceException(this.ogcWebServiceExceptionMsg);
        	 }
			int statusCode =0;
			String queryHttpMethodType = this.getServiceConfig().getHttpMethod();
			String queryRequestMethodName = this.getServiceConfig().getProperties().getProperty("REQUEST");
			String queryServiceType = this.getServiceConfig().getServiceType();
				
			if(queryServiceType.equalsIgnoreCase(CustomQueryConstants.ALL)){
				
				if(queryHttpMethodType.equalsIgnoreCase(CustomQueryConstants.GET) && queryRequestMethodName.equals(CustomQueryConstants.HTTP_GET))
					statusCode = executeSimpleGetRequest(this.method_SimRun, this.client_SimRun);
				else if(queryHttpMethodType.equalsIgnoreCase(CustomQueryConstants.POST) && queryRequestMethodName.equals(CustomQueryConstants.HTTP_POST))
					statusCode = executeSimplePostRequest(this.method_SimRun, this.client_SimRun);
				else if(queryHttpMethodType.equalsIgnoreCase(CustomQueryConstants.POST) && queryRequestMethodName.contains(CustomQueryConstants.SOAP_1_1))							
					statusCode = executeSimpleSOAP_1_1_Client();
				else if(queryHttpMethodType.equalsIgnoreCase(CustomQueryConstants.POST) && queryRequestMethodName.contains(CustomQueryConstants.SOAP_1_2))					
					statusCode = executeSimpleSOAP_1_2_Client();
				else
					throw new OGCWebServiceException( "Unknown query requested. Execution not implemented"+
							"\n HHTP Method :"+queryHttpMethodType +
							"\n SERVICE Type  :" +queryServiceType +
							"\n Request Name  :" +queryRequestMethodName
							
					);
			}
			long lapse = System.currentTimeMillis() - this.startTimeSync;
			if((queryServiceType.equalsIgnoreCase(CustomQueryConstants.ALL)) && (!queryRequestMethodName.contains(CustomQueryConstants.SOAP))){
				response = ((CustomValidator) this.getServiceConfig().getValidator()).validateAnswer( this.method_SimRun, statusCode );
			
			}else{
				response = ((CustomValidator) this.getServiceConfig().getValidator()).validateAnswer(ContentType, soap_stream_response, statusCode );
				
			}
		
			response.setLastLapse( lapse );
			Calendar date = Calendar.getInstance();
			date.setTimeInMillis( this.startTimeSync );
			response.setLastTest( date.getTime() );
		} catch ( Exception e ) {
			response = new ValidatorResponse( "Page Unavailable: " + e.getLocalizedMessage(),
			Status.RESULT_STATE_PAGE_UNAVAILABLE );
			response.setLastLapse( -1 );
			response.setLastTest( Calendar.getInstance().getTime() );
		} finally {
			if(null !=this.method_SimRun)
				this.method_SimRun.releaseConnection();
		}
		this.getServiceLog().addMessage(response, this.getServiceConfig() );
	}
	
	/**
	 * @return the startTimeSync
	 */
    @Override
	public long getStartTimeSync() {
		return startTimeSync;
	}

	/**
	 * @param startTimeSync the startTimeSync to set
	 */
    @Override	
	public void setStartTimeSync(long startTimeSync) {
		this.startTimeSync = startTimeSync;
	}


}
