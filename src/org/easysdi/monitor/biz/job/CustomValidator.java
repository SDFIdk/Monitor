package org.easysdi.monitor.biz.job;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;

import javax.imageio.ImageIO;


import org.apache.commons.httpclient.HttpMethodBase;
import org.deegree.framework.util.StringTools;
import org.deegree.portal.owswatch.Status;
import org.deegree.portal.owswatch.ValidatorResponse;
import org.deegree.portal.owswatch.validator.AbstractValidator;

public class CustomValidator extends AbstractValidator implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2335714763916219298L;
	


	//process image or xml
    public ValidatorResponse validateAnswer( HttpMethodBase method, int statusCode ) {
    	
        String contentType = method.getResponseHeader( "Content-Type" ).getValue();
        String lastMessage = null;
        Status status = null;
        ValidatorResponse rs = null;
        String xml = null;
        InputStream stream = null;
        
    	if ( !isValidHttpResponse( statusCode ) ) {
    		rs = validateErrorHttpResponse( statusCode );
    		rs.setHttpStatusCode(500);
    		try
    		{
    			rs.setData(method.getResponseBody());
    			rs.setContentType(contentType);
    		}catch(IOException e)
    		{
    			rs.setData(new byte[0]);
    			rs.setContentType("");
    		}
    		return rs;
    	}

    	if (null == method) {
    		status = Status.RESULT_STATE_BAD_RESPONSE;
    		lastMessage = "No response received";
    		rs = new ValidatorResponse( lastMessage, status );
    		rs.setHttpStatusCode(500);
    		rs.setContentType("");
    		rs.setData(new byte[0]);
    		return rs;
    	}

        

        //process whatever it is image, , xml and others
        try {
        	  if ( contentType.contains( "image" ) ) {        	
        		  stream = copyStream( method.getResponseBodyAsStream() );
        		  stream.reset();
        		  BufferedImage image = ImageIO.read(stream);
        		  String imageformat = "jpg";
        		  int index  = contentType.lastIndexOf("/");
        		  if(index > -1 && index < contentType.length())
        		  {
        			  imageformat = contentType.substring(index+1).toLowerCase();
        		  }
        		  // Open
        		  ByteArrayOutputStream baos = new ByteArrayOutputStream();
        		  // Write
        		  ImageIO.write(image,imageformat, baos);
        		  //close
        		  baos.flush();
        		  byte[] imageAsBytes = baos.toByteArray();
        		  baos.close();

        		  status = Status.RESULT_STATE_AVAILABLE;
        		  lastMessage = status.getStatusMessage();
        		  rs = new ValidatorResponse( lastMessage, status,imageAsBytes,method.getResponseContentLength(),contentType);	
        		  rs.setHttpStatusCode(200);
        		  return rs;
        	  }
        	  
        	  else if (contentType.contains( "xml" ) ){
        		   stream = copyStream( method.getResponseBodyAsStream() );
                   stream.reset();
                   xml = parseStream( stream );
                   rs = validateCustomXML(xml, false);
                   rs.setContentType(contentType);
                   rs.setData(xml.getBytes());
                   return rs;
        	  }else { // whatever response it is, json/html/plain i.e not image or xml.
        		  InputStreamReader reader = new InputStreamReader(  method.getResponseBodyAsStream() );
        		  BufferedReader bufReader = new BufferedReader( reader );
        		  StringBuilder builder = new StringBuilder();
        		  String line = null;

        		  line = bufReader.readLine();
        		  while ( line != null ) {
        			  builder.append( line );
        			  line = bufReader.readLine();
        		  }

        		  String html = builder.toString();
        		  status = Status.RESULT_STATE_AVAILABLE;
        		  lastMessage = "Html response received";
        		  rs = new ValidatorResponse( lastMessage, status);
        		  rs.setContentType(contentType);
        		  rs.setData(html.getBytes());
        		  rs.setHttpStatusCode(200);
        		  return rs;
        		  
        	  }
         
        } catch ( Exception e ) {
            status = Status.RESULT_STATE_SERVICE_UNAVAILABLE;
            lastMessage = e.getLocalizedMessage();
            rs = new ValidatorResponse( lastMessage, status);
            rs.setContentType("");
            rs.setData(lastMessage.getBytes());
            rs.setHttpStatusCode(500);
            return rs;
        }
    }
    
    public ValidatorResponse validateAnswer( String ContentType,  InputStream response, int statusCode )  {
    	//process for xml
    	String lastMessage = null;
    	Status status = null;
    	String xml = null;       
    	ValidatorResponse rs = null;;
   


    	if ( !isValidHttpResponse( statusCode ) ) {
    		rs =  validateErrorHttpResponse( statusCode );
    		rs.setHttpStatusCode(500);
    		rs.setContentType(ContentType);
            rs.setData(new byte[0]);
    		return rs ;
    	}

    	if (null == response) {
    		status = Status.RESULT_STATE_BAD_RESPONSE;
    		lastMessage = "No response received";
    		rs = new ValidatorResponse( lastMessage, status );
    		rs.setHttpStatusCode(500);
    		rs.setContentType(ContentType);
            rs.setData(lastMessage.getBytes());
    		return rs ;
    	}



    	// Get reply content

    	try {
    		InputStreamReader reader = new InputStreamReader( response );
    		BufferedReader bufReader = new BufferedReader( reader );
    		StringBuilder builder = new StringBuilder();
    		String line = null;

    		line = bufReader.readLine();
    		while ( line != null ) {
    			builder.append( line );
    			line = bufReader.readLine();
    		}

    		xml = builder.toString();
    		
    		if ( !ContentType.contains( "xml" ) ) {
    			status = Status.RESULT_STATE_UNEXPECTED_CONTENT;    			
    			lastMessage = StringTools.concat( 100, "Error: Response Content is ", ContentType, " not xml" , "Reponse received is", xml);
    			rs = new ValidatorResponse( lastMessage, status );
    			rs.setContentType(ContentType);
    			rs.setData(lastMessage.getBytes());
    			return rs;
    		} 
    		   	   
    		rs = validateCustomXML(xml, true);
    		rs.setContentType(ContentType);
    		rs.setData(xml.getBytes());              

    		return rs;



    	} catch (IOException e) {
    		// TODO Auto-generated catch block
    		status = Status.RESULT_STATE_BAD_RESPONSE;
    		lastMessage = "The following error occured :"+ e.getMessage();
    		rs = new ValidatorResponse( lastMessage, status );
    		rs.setContentType("");
    		rs.setData(lastMessage.getBytes());
    		return rs;
    	}
    	


		
	}
    
    public ValidatorResponse validateCustomXML( String xml, boolean isSoapResponse)  {
    	
    	String lastMessage = null;
    	Status status = null;    
    	ValidatorResponse rs = null;;
   

    	if ( xml.length() == 0 ) {
    		status = Status.RESULT_STATE_BAD_RESPONSE;
    		lastMessage = "Error: XML Response is empty";

    		rs = new ValidatorResponse( lastMessage, status );
    		rs.setHttpStatusCode(500);
    		return rs;
    	}    		
    	if(isSoapResponse){ 		


    		if ( xml.contains(":Fault") ) {  // this is a soap fault  			

    			status = Status.RESULT_STATE_BAD_RESPONSE;
    			lastMessage = "Exception occured";			
    			rs = new ValidatorResponse( lastMessage, status );
    			rs.setHttpStatusCode(500);
    			return rs;
    		}
    	}else{

    		if ( xml.contains( "ServiceException" ) ) {
    			status = Status.RESULT_STATE_BAD_RESPONSE;
    			lastMessage = "Exception occured";			
    			rs = new ValidatorResponse( lastMessage, status );
    			rs.setHttpStatusCode(500);
    			return rs;
    		}

    	}

    	status = Status.RESULT_STATE_AVAILABLE;
    	lastMessage = "correct response received";
    	//return new ValidatorResponse( lastMessage, status );
    	rs = new ValidatorResponse( lastMessage, status );
    	rs.setHttpStatusCode(200);
    	return rs;
    }
    

    
    

}
