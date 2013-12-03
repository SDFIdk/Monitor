package org.deegree.portal.owswatch.validator;


import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.Serializable;


import org.apache.commons.httpclient.HttpMethodBase;
import org.deegree.framework.util.StringTools;
import org.deegree.portal.owswatch.Status;
import org.deegree.portal.owswatch.ValidatorResponse;

public class WMTSGetTileValidator extends AbstractValidator implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6010956771613989911L;

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
