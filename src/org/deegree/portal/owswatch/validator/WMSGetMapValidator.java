//$HeadURL: svn+ssh://rbezema@svn.wald.intevation.org/deegree/base/branches/2.3_testing/src/org/deegree/portal/owswatch/validator/WMSGetMapValidator.java $
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

package org.deegree.portal.owswatch.validator;


import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.Serializable;

import org.apache.commons.httpclient.HttpMethodBase;
import org.deegree.framework.util.StringTools;
import org.deegree.portal.owswatch.Status;
import org.deegree.portal.owswatch.ValidatorResponse;

/**
 * A specific implementation of AbstractValidator
 *
 * @author <a href="mailto:elmasry@lat-lon.de">Moataz Elmasry</a>
 * @author last edited by: $Author: jmays $
 *
 * @version $Revision: 20271 $, $Date: 2009-10-21 13:07:15 +0200 (Mi, 21. Okt 2009) $
 */
public class WMSGetMapValidator extends AbstractValidator implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -5983538240882784183L;

    /*
     * (non-Javadoc)
     *
     * @see org.deegree.portal.owswatch.validator.AbstractValidator#validateAnswer(org.apache.commons.httpclient.HttpMethodBase,
     *      int)
     */
    @Override
    protected ValidatorResponse processAnswer( HttpMethodBase method ) {
        String contentType = method.getResponseHeader( "Content-Type" ).getValue();
        String lastMessage = null;
        Status status = null;
        byte[] responseAsBytes = null;
        try {
        	
	        if ( !contentType.contains( "image" ) ) {
	            if ( !contentType.contains( "xml" ) ) {
	                status = Status.RESULT_STATE_UNEXPECTED_CONTENT;
	                lastMessage = StringTools.concat( 100, "Error: Response Content is ", contentType, " not image" );
	                responseAsBytes = method.getResponseBody();
	                return new ValidatorResponse( lastMessage, status,responseAsBytes,method.getResponseContentLength(), contentType );
	            } else {
	                return validateXmlServiceException( method );
	            }
	        }

	       	InputStream stream = copyStream( method.getResponseBodyAsStream());
         	int bufferSize = stream.available();
         	ByteArrayOutputStream baos = new ByteArrayOutputStream(bufferSize);   
        	try
        	{    	
	        	byte[] buffer = new byte[bufferSize];
	        	int bytesread = 0;
	        	while(true){
	        		bytesread = stream.read(buffer);
	        		if (bytesread == -1)
	        			break;
	        		baos.write(buffer,0,bytesread);
	        	}
	        	responseAsBytes = baos.toByteArray();
        	}catch(Exception e)
        	{}
        	finally
        	{
        		baos.close();
        		stream.close();
        	}
            
            status = Status.RESULT_STATE_AVAILABLE;
            lastMessage = status.getStatusMessage();
            return new ValidatorResponse( lastMessage, status,responseAsBytes,method.getResponseContentLength(),contentType);
        } catch ( Exception e ) {
            status = Status.RESULT_STATE_SERVICE_UNAVAILABLE;
            lastMessage = e.getLocalizedMessage();         
            return new ValidatorResponse( lastMessage, status);
        }
    }
}
