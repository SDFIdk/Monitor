//$HeadURL: svn+ssh://rbezema@svn.wald.intevation.org/deegree/base/branches/2.3_testing/src/org/deegree/portal/owswatch/ServiceConfiguration.java $
/*----------------------------------------------------------------------------
 This file is part of deegree, http://deegree.org/
 Copyright (C) 2001-2009 by:
 Department of Geography, University of Bonn
 and
 lat/lon GmbH

 This library is free software; you can redistribute it and/or modify it under
 the terms of the GNU Lesser General Public License as published by the Free
 Software Foundation; either version 2.1 of the License, or (at your option)
 any later version.
 This library is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 details.
 You should have received a copy of the GNU Lesser General Public License
 along with this library; if not, write to the Free Software Foundation, Inc.,
 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

 Contact information:

 lat/lon GmbH
 Aennchenstr. 19, 53177 Bonn
 Germany
 http://lat-lon.de/

 Department of Geography, University of Bonn
 Prof. Dr. Klaus Greve
 Postfach 1147, 53001 Bonn
 Germany
 http://www.geographie.uni-bonn.de/deegree/

 e-mail: info@deegree.org
 ----------------------------------------------------------------------------*/

package org.deegree.portal.owswatch;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.deegree.framework.util.StringTools;
import org.deegree.ogcwebservices.OGCWebServiceException;
import org.deegree.portal.owswatch.validator.Validator;

/**
 * A data class to hold the information about a certain test. Like test name ,
 * type, etc..
 * 
 * @author <a href="mailto:elmasry@lat-lon.de">Moataz Elmasry</a>
 * @author last edited by: Yves Grasset - arx iT
 * 
 * @version $Date: 2010-03-19$
 */
public class ServiceConfiguration implements Serializable {

	/**
     *
     */
	private static final long		serialVersionUID	= -8730764035469230765L;

	private Properties				requestProps		= null;

	private static int				serviceCounter		= 0;

	private final int				serviceid;

	private String					requestType			= null;

	private String					serviceVersion		= null;

	private String					serviceType			= null;

	private boolean					active;

	private int						timeout;

	private final int				refreshRate;

	private String					onlineResource		= null;

	private String					serviceName			= null;

	private String					httpMethod			= null;

	private Validator				validator			= null;

	private final HttpMethodBase	method				= null;

	private Credentials				userCreds			= null;

	private String					login			    = "";
	
	private String					password			= "";

	/**
	 * This constructor is used by both ServicesConfigurationFactory and to
	 * build requests from the jsp page ,when adding/changing a service.
	 * 
	 * @param serviceId
	 *            -1 to assign a new id to this service
	 * @param serviceName
	 * @param httpMethod
	 * @param onlineResource
	 * @param active
	 * @param interval
	 * @param timeout
	 * @param requestParams
	 *            Contains the kvp request parameter, it must at least the
	 *            following keys include VERSION, SERVICE, REQUEST
	 * @throws ConfigurationsException
	 *             if any of the mandatory parameters is null
	 */
	public ServiceConfiguration(int serviceId, String serviceName, String httpMethod,
			String onlineResource, boolean active, int interval, int timeout,
			Properties requestParams,String login, String password) throws ConfigurationsException {
		if (serviceId > serviceCounter) {
			serviceCounter = serviceId;
		}

		if (serviceId < 1) {
			serviceId = generateServiceId();
		}

		this.serviceid = serviceId;

		if (serviceName == null) {
			throw new ConfigurationsException("The serviceName can not be null");
		}
		this.serviceName = serviceName;

		if (httpMethod == null) {
			throw new ConfigurationsException("The HttpMethod can not be null");
		}
		this.httpMethod = httpMethod;

		if (httpMethod.equals("POST") && !requestParams.containsKey(Constants.XML_REQUEST)) {
			throw new ConfigurationsException("Missing XML request from service: " + serviceName);
		}

		if (onlineResource == null) {
			throw new ConfigurationsException("Online Resource can not be null");
		}
		this.onlineResource = onlineResource;

		if (requestParams == null) {
			throw new ConfigurationsException("The properties can not be null");
		}

		this.requestProps = requestParams;

		if (!requestParams.containsKey(Constants.SERVICE_TYPE)) {
			throw new ConfigurationsException("The service type can not be null");
		}
		this.serviceType = requestParams.getProperty(Constants.SERVICE_TYPE);

		if (!requestParams.containsKey(Constants.REQUEST_TYPE)) {
			throw new ConfigurationsException("The requestType can not be null");
		}
		this.requestType = requestProps.getProperty(Constants.REQUEST_TYPE);

		if (!requestParams.containsKey(Constants.VERSION)) {
			throw new ConfigurationsException("The Version can not be null");
		}
		this.serviceVersion = requestParams.getProperty(Constants.VERSION);

		this.active = active;

		if (interval < 1) {
			throw new ConfigurationsException("The refresh refreshRate can not be less than one");
		}
		this.refreshRate = interval;

		if (timeout < 0) {
			throw new ConfigurationsException("The timeout can not be less than one");
		}
		this.timeout = timeout;
		
		this.login = login;
		
		this.password = password;
	}



	/**
	 * @return Service properties
	 */
	public Properties getProperties() {
		return requestProps;
	}



	/**
	 * Creates a http request based on the content of the Service. These contens
	 * also include whether its POST or GET method
	 * 
	 * @return the request String
	 */
	public String createHttpRequest() {

		if (httpMethod.equals("POST")) {
			return createPOSTRequest();
		} else if (httpMethod.equals("GET")) {

			try {
				return createGETRequest();

			} catch (OGCWebServiceException e) {
				e.printStackTrace();

				return null;
			}

		} else {
			return null;
		}
	}



	/**
	 * @return the created GET Request
	 * @throws OGCWebServiceException
	 */
	protected String createGETRequest() throws OGCWebServiceException {
		final String encoding = "UTF-8";

		StringBuilder builder = new StringBuilder(200);
		builder.append(onlineResource);
		if (!onlineResource.endsWith("?")) {
			builder.append("?");
		}
		Enumeration<Object> keys = requestProps.keys();

		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			String rawValue = (String) requestProps.get(key);
			String encodedValue;

			try {
				encodedValue = URLEncoder.encode(rawValue, encoding);

			} catch (UnsupportedEncodingException e) {
				throw new OGCWebServiceException("Unable to encode request parameters' values");
			}
			builder.append(key).append("=").append(encodedValue);
			if (keys.hasMoreElements()) {
				builder.append("&");
			}
		}

		return builder.toString();
	}



	/**
	 * @return stored XML POST fragment
	 */
	protected String createPOSTRequest() {
		return requestProps.getProperty(Constants.XML_REQUEST);
	}



	/**
	 * @return int
	 */
	public static int generateServiceId() {
		return ++serviceCounter;
	}



	/**
	 * @return int
	 */
	public int getServiceid() {
		return serviceid;
	}



	/**
	 * @return boolean
	 */
	public boolean isActive() {
		return active;
	}



	/**
	 * @param active
	 */
	public void setActive(boolean active) {
		this.active = active;
	}



	/**
	 * @return String
	 */
	public String getHttpMethod() {
		return httpMethod;
	}



	/**
	 * @return the refresh refreshRate
	 */
	public int getRefreshRate() {
		return refreshRate;
	}



	/**
	 * @return the server address to which the requests will be sent
	 */
	public String getOnlineResource() {
		return onlineResource;
	}



	/**
	 * @return ex. Getcapabilities, GetFeature, etc..
	 */
	public String getRequestType() {
		return requestType;
	}



	/**
	 * @return the given name during adding a new test
	 */
	public String getServiceName() {
		return serviceName;
	}



	/**
	 * @return ex WMS, WFS, etc..
	 */
	public String getServiceType() {
		return serviceType;
	}



	/**
	 * @return String
	 */
	public String getServiceVersion() {
		return serviceVersion;
	}



	/**
	 * @return Timeout of the requests
	 */
	public int getTimeout() {
		return timeout;
	}



	/**
	 * @return counter is assigned to the newly created service
	 */
	public static int getServiceCounter() {
		return serviceCounter;
	}



	/**
	 * @param serviceCounter
	 *            Sets the service counter. It should be used solely by the
	 *            class ServiceWatcher
	 * 
	 */
	public static void setServiceCounter(int serviceCounter) {
		if (serviceCounter > ServiceConfiguration.serviceCounter) {
			ServiceConfiguration.serviceCounter = serviceCounter;
		}
	}



	/**
	 * @param onlineResource
	 *            of the service
	 */
	public void setOnlineResource(String onlineResource) {
		this.onlineResource = onlineResource;
	}



	/**
	 * @param serviceName
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}



	/**
	 * @param timeout
	 *            in milliseconds
	 */
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}



	/**
	 * Creates an HttpMethodBase on the first call and keeps returning it back
	 * 
	 * @return {@link HttpMethodBase}
	 * @throws OGCWebServiceException
	 *             If no Httpmethod could be initialized as POST
	 */
	public HttpMethodBase getHttpMethodBase() throws OGCWebServiceException {

		if (method == null) {
			HttpMethodBase method = null;
			if ("POST".equals((getHttpMethod()))) {
				method = new PostMethod(getOnlineResource());
				try {
					// In POST, the next line has to be added, otherwise POST
					// does not work
					
					((PostMethod) method).setRequestEntity(new StringRequestEntity(
							createHttpRequest(), "text/xml", "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					throw new OGCWebServiceException(StringTools
						.concat(100, "Could not set RequestEntity for ", getServiceName(),
								" in executeMethod()"));
				}
			} else if ("GET".equals(getHttpMethod())) {
				String uri = createHttpRequest();
				method = new GetMethod(uri);
			}

			return method;
		}
		return method;
	}



	/**
	 * @param userCreds
	 *            the userCreds to set
	 */
	public void setUserCreds(String userName, String password) {

		if (null == userName || null == password) {
			this.userCreds = null;
		} else {
			this.userCreds = new UsernamePasswordCredentials(userName, password);
		}
	}



	/**
	 * @return the userCreds
	 */
	public Credentials getUserCreds() {
		return userCreds;
	}



	/**
	 * Generically creates an instance of the needed Validator using reflection
	 * 
	 * @return {@link Validator}
	 * @throws ConfigurationsException
	 */
	public Validator getValidator() throws ConfigurationsException {

		if (validator == null) {
			String validatorClassName = StringTools.concat(100, getClass().getPackage().getName(),
															".validator.", serviceType,
															requestType, "Validator");
			if (validatorClassName.startsWith("package")) {
				validatorClassName = validatorClassName.substring("package".length()).trim();
			}
			try {
				// get the validate function
				this.validator = (Validator) getClass().getClassLoader()
					.loadClass(validatorClassName).getConstructor(new Class[] {})
					.newInstance(new Object[] {});

			} catch (Exception e) {
				System.err.println(e.getLocalizedMessage());
				throw new ConfigurationsException(StringTools.concat(100, "Error: The method ",
																		validatorClassName,
																		" is not implemented"));

			}
		}

		return validator;
	}



	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getServiceid();
	}
	
	public String getLogin()
	{
		return this.login;
	}
	
	public String getPassword()
	{
		return this.password;
	}

}
