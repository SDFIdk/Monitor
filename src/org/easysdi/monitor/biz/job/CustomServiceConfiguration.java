package org.easysdi.monitor.biz.job;


import java.util.Enumeration;
import java.util.Properties;

import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

import org.deegree.ogcwebservices.OGCWebServiceException;
import org.deegree.portal.owswatch.ConfigurationsException;
import org.deegree.portal.owswatch.ServiceConfiguration;
import org.deegree.portal.owswatch.validator.Validator;

public class CustomServiceConfiguration extends ServiceConfiguration{

	
	
	public CustomServiceConfiguration(int serviceId, String serviceName,
			String httpMethod, String onlineResource, boolean active,
			int interval, int timeout, Properties requestParams,String login, String password)
			throws ConfigurationsException {
		super(serviceId, serviceName, httpMethod, onlineResource, active, interval,
				timeout, requestParams,login, password);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 6171707957348666410L;
	
	
	public Validator getValidator() {
		
		return new CustomValidator();
		
	}
	
	public HttpMethodBase getHttpMethodBase() throws OGCWebServiceException{

	
		HttpMethodBase method = null;
		String queryHttpMethodType = getHttpMethod();  
		String queryRequestMethodName = getProperties().getProperty("REQUEST");
		String queryServiceType = getServiceType();
			
			if(queryServiceType.equalsIgnoreCase(CustomQueryConstants.ALL)){
				
				if(queryHttpMethodType.equalsIgnoreCase(CustomQueryConstants.GET) && queryRequestMethodName.equals(CustomQueryConstants.HTTP_GET))
				{	String uri = createHttpRequest();
					method = new GetMethod(uri);
				}
				else if(queryHttpMethodType.equalsIgnoreCase(CustomQueryConstants.POST) && queryRequestMethodName.equals(CustomQueryConstants.HTTP_POST))
				{
					method = new PostMethod(getOnlineResource());		

					Enumeration<Object> keys = this.getProperties().keys();

					while (keys.hasMoreElements()) {
						String key = (String) keys.nextElement();
						String rawValue = (String) this.getProperties().get(key);
						((PostMethod) method).addParameter(key, rawValue);
					}
				}
				else if(queryHttpMethodType.equalsIgnoreCase(CustomQueryConstants.POST) && queryRequestMethodName.equals(CustomQueryConstants.SOAP_1_1))
				{
					return null ;
				}else if(queryHttpMethodType.equalsIgnoreCase(CustomQueryConstants.POST) &&queryRequestMethodName.equals(CustomQueryConstants.SOAP_1_2))
				{	
					return null;
				}else if(queryHttpMethodType.equalsIgnoreCase(CustomQueryConstants.GET) && queryRequestMethodName.equals(CustomQueryConstants.FTP))
				{
					return null;
				}
				else{
					throw new OGCWebServiceException( "Unknown query requested. Execution not implemented"+
							"\n HHTP Method :"+queryHttpMethodType +
							"\n SERVICE Type  :" +queryServiceType +
							"\n Request Name  :" +queryRequestMethodName							
					);
				}
			}
			else{
				throw new OGCWebServiceException( "Unknown query requested. Execution not implemented"+
						"\n HHTP Method :"+queryHttpMethodType +
						"\n SERVICE Type  :" +queryServiceType +
						"\n Request Name  :" +queryRequestMethodName
						
				);
			}
		
	
			return method;
		
	}

	

}
