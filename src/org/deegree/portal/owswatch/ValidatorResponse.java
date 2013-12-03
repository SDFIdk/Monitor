//$HeadURL: svn+ssh://rbezema@svn.wald.intevation.org/deegree/base/branches/2.3_testing/src/org/deegree/portal/owswatch/ValidatorResponse.java $
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
import java.util.Date;

/**
 * Data class that holds the response of execute test that tests a certain service.
 *
 * @author <a href="mailto:elmasry@lat-lon.de">Moataz Elmasry</a>
 * @author last edited by: $Author: jmays $
 *
 * @version $Revision: 20271 $, $Date: 2009-10-21 13:07:15 +0200 (Mi, 21. Okt 2009) $
 */
public class ValidatorResponse implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 7602599272924961670L;

    private String message = null;
    
    private int httpStatusCode = 0;

    private Date lastTest = null;

    private long lastLapse = -1;
    
    private String serviceExceptionCode = null;

    private Status status = null;
    
    private long responseLength;

    private byte[] data = null;
    
    private String contentType = null;
    
    private long first_byte_time = 0;

    /**
     * Constructor
     *
     * @param message
     * @param status
     */
    public ValidatorResponse( String message, Status status ) {
        this.setMessage(message);
        this.setStatus(status);
    }
    
    /**
     * Constructor
     *     
     * @param message
     * @param status
     * @param exceptionCode
     */
    public ValidatorResponse( String message, Status status, 
                              String exceptionCode ) {
        this( message, status);
        this.setServiceExceptionCode(exceptionCode);
    }

    /**
     * Constructor
     *
     * @param message
     * @param status
     * @param httpStatusCode
     */
    public ValidatorResponse( String message, Status status, 
                              int httpStatusCode ) {
        this(message, status);
        this.setHttpStatusCode(httpStatusCode);
    }
    
    /**
     * Constructor
     * 
     * @param message
     * @param status
     * @param image
     */
    public ValidatorResponse(String message,Status status,byte[] data,long length,String contentType)
    {
    	this(message,status);
    	this.setData(data);
    	this.setResponseLength(length);
    	this.setContentType(contentType);
    }
	
    /**
	 * @return the data
	 */
	public byte[] getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(byte[] data) {
		this.data = data;
	}
	
	
    
    /**
	 * @return the contentType
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * @param contentType the contentType to set
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	/**
     * Defines the returned HTTP status code.
     * 
     * @param   newStatusCode   the returned HTTP status code
     */
    public void setHttpStatusCode(int newStatusCode) {
        
        if (100 > newStatusCode || 600 < newStatusCode) {
            throw new IllegalArgumentException(
                   "The HTTP status code is invalid.");
        }
        
        this.httpStatusCode = newStatusCode;
    }

    /**
     * Gets the returned HTTP status code.
     * 
     * @return the HTTP status code
     */
    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    /**
     * @return lastTest Lapse
     */
    public long getLastLapse() {
        return lastLapse;
    }

    /**
     * @param lastLapse
     */
    public void setLastLapse( long lastLapse ) {
        this.lastLapse = lastLapse;
    }

    /**
     * @return last test Date
     */
    public Date getLastTest() {
        return lastTest;
    }

    /**
     * @param lastTest
     */
    public void setLastTest( Date lastTest ) {
        this.lastTest = lastTest;
    }

    /**
     * @return last message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message
     */
    public void setMessage( String message ) {
        this.message = message;
    }

    /**
     * @param serviceExceptionCode the serviceExceptionCode to set
     */
    public void setServiceExceptionCode(String serviceExceptionCode) {
        this.serviceExceptionCode = serviceExceptionCode;
    }

    /**
     * @return the service exception code, if any
     */
    public String getServiceExceptionCode() {
        return this.serviceExceptionCode;
    }

    /**
     * @return status of the last test
     */
    public Status getStatus() {
        return status;
    }

    /**
     * @param status
     */
    public void setStatus( Status status ) {
        this.status = status;
    }

	public long getResponseLength() {
		return responseLength;
	}

	public void setResponseLength(long responseLength) {
		this.responseLength = responseLength;
	}
    
    public void setFirst_byte_time(long milliseconds)
    {
    	this.first_byte_time = milliseconds;
    }
    
    public long getFirst_byte_time()
    {
    	return this.first_byte_time;
    }
}
