package org.easysdi.monitor.biz.job;
import java.io.IOException;
import java.util.Calendar;


import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.io.CopyStreamEvent;
import org.apache.commons.net.io.CopyStreamListener;
import org.deegree.ogcwebservices.OGCWebServiceException;
import org.deegree.portal.owswatch.ServiceConfiguration;
import org.deegree.portal.owswatch.ServiceInvoker;
import org.deegree.portal.owswatch.ServiceLog;
import org.deegree.portal.owswatch.Status;
import org.deegree.portal.owswatch.ValidatorResponse;

public class CustomFtpInvoker extends ServiceInvoker{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long startTimeSync;

	public CustomFtpInvoker(ServiceConfiguration serviceconfig,
			ServiceLog serviceLog) {
		super(serviceconfig, serviceLog);
	}
	
	  /**
     * Executes the test in the main thread
     *
     */
    public void executeTest() {

        ValidatorResponse tmpResponse = null;
        try {
            tmpResponse = executeFTP();
        } catch (OGCWebServiceException e ) {
            tmpResponse = new ValidatorResponse("FTP Unavailable: " + e.getLocalizedMessage(),Status.RESULT_STATE_PAGE_UNAVAILABLE );
            tmpResponse.setLastLapse( -1 );
            tmpResponse.setLastTest( Calendar.getInstance().getTime() );
        }
        this.getServiceLog().addMessage( tmpResponse, this.getServiceConfig());
    }
    
	protected ValidatorResponse executeFTP()
	throws OGCWebServiceException {
		ValidatorResponse response = null;
		try
		{
			boolean binaryTransfer = false, listFiles = true, hidden = false;
	        boolean localActive = false, useEpsvWithIPv4 = false, printHash = false;
	        long keepAliveTimeout = -1;
	        int controlKeepAliveReplyTimeout = -1;
	        int port = 0;
	        
			String username = this.getServiceConfig().getLogin();
        	String password = this.getServiceConfig().getPassword();
		    String filepath = this.getServiceConfig().getProperties().getProperty(CustomQueryConstants.FTPFOLDER);
		    String server = this.getServiceConfig().getOnlineResource();
		    
	        FTPClient ftp = new FTPClient();
	       /* if (printHash) {
	            ftp.setCopyStreamListener(createListener());
	        }
	        if (keepAliveTimeout >= 0) {
	            ftp.setControlKeepAliveTimeout(keepAliveTimeout);
	        }
	        if (controlKeepAliveReplyTimeout >= 0) {
	            ftp.setControlKeepAliveReplyTimeout(controlKeepAliveReplyTimeout);
	        }*/
	        ftp.setListHiddenFiles(hidden);
	        long startTime = -1;
	        long size = 0;
	        Status status;
	        try
	        {
	
	        	startTime = new Long (System.currentTimeMillis ());
	            int reply;
	            
	            if (port > 0) {
	                ftp.connect(server, port);
	            } else {
	                ftp.connect(server);
	            }
	    
	            // After connection attempt, you should check the reply code to verify success.
	            reply = ftp.getReplyCode();

	            if (!FTPReply.isPositiveCompletion(reply))
	            {
	                ftp.disconnect();
	                status = Status.RESULT_STATE_SERVICE_UNAVAILABLE;
	                String lastMessage = "FTP server refused connection";
	                ValidatorResponse errorResponse = new ValidatorResponse( lastMessage, status);
	                errorResponse.setLastLapse(-1);
	                errorResponse.setLastTest(Calendar.getInstance().getTime());
	                return errorResponse; 
	            }
	            
	            if (!ftp.login(username, password))
	            {
	                ftp.logout();
	                status = Status.RESULT_STATE_SERVICE_UNAVAILABLE;
	                String lastMessage = "Login not valid";
	                ValidatorResponse errorResponse = new ValidatorResponse(lastMessage, status);
	                errorResponse.setLastLapse(-1);
	                errorResponse.setLastTest(Calendar.getInstance().getTime());
	                return errorResponse; 
	            }
	            
	            if(filepath == null || filepath.trim().equals(""))
	            {
	            	// Only test connecion;
	               	long lapse = (System.currentTimeMillis() - startTime);
	            	response = new ValidatorResponse("",Status.RESULT_STATE_AVAILABLE);
	            	response.setLastLapse(lapse);
	            	response.setResponseLength(0);
	            
	            	Calendar date = Calendar.getInstance();
	            	date.setTimeInMillis(startTime);
	            	response.setLastTest(date.getTime());
	            	ftp.disconnect();
	            	return response;
	            }
	            
	            if (binaryTransfer) {
	                ftp.setFileType(FTP.BINARY_FILE_TYPE);
	            } else {
	                // in theory this should not be necessary as servers should default to ASCII but they don't all do so - see NET-500
	                ftp.setFileType(FTP.ASCII_FILE_TYPE);
	            }
	            // Use passive mode as default because most of us are behind firewalls these days.
	            if (localActive) {
	                ftp.enterLocalActiveMode();
	            } else {
	                ftp.enterLocalPassiveMode();
	            }
	            ftp.setUseEPSVwithIPv4(useEpsvWithIPv4);
	            boolean fileFound = false;
	            if (listFiles)
	            {
	                for (FTPFile f : ftp.listFiles(filepath)) {
	                    if(f.isFile())
	                    {
	                    	fileFound = true;
	                    	size = f.getSize();
	                    	break;
	                    }
	                }
	            }
	            if(fileFound)
	            {
	            	long lapse = (System.currentTimeMillis() - startTime);
	            	response = new ValidatorResponse("",Status.RESULT_STATE_AVAILABLE);
	            	response.setLastLapse(lapse);
	            	response.setResponseLength(size);
	            	Calendar date = Calendar.getInstance();
	            	date.setTimeInMillis(startTime);
	            	response.setLastTest(date.getTime());
	            	ftp.disconnect();
	            	return response;
	            }else
	            {
	                status = Status.RESULT_STATE_SERVICE_UNAVAILABLE;
	                String lastMessage = "File not found";
	                ValidatorResponse errorResponse = new ValidatorResponse( lastMessage, status);
	                errorResponse.setLastLapse( -1 );
	                errorResponse.setLastTest( Calendar.getInstance().getTime());
	                return errorResponse; 
	            }
	        }
	        catch (IOException e)
	        {
	            if (ftp.isConnected())
	            {
	                try
	                {
	                    ftp.disconnect();
	                }
	                catch (IOException f)
	                {
	                	throw f;
	                }
	            }
	            throw e;
	        }
	        finally
	        {
	        	// Always close connection
	        	if (ftp.isConnected())
	            {
	                try
	                {
	                	ftp.disconnect();
	                }
	                catch (IOException f)
	                {
	                	throw f;
	                }
	            }
	        }
		}catch(Exception e) {
			throw new OGCWebServiceException(e.getLocalizedMessage());
		}
	}
	
	/* Method executeHttpMethodSimRun
	 * Execute the FTP request
     * Used for multi threading
     */
    @Override
	public void executeHttpMethodSimRun()
	{
		ValidatorResponse response = null;
		try
		{
			boolean binaryTransfer = false, listFiles = true, hidden = false;
	        boolean localActive = false, useEpsvWithIPv4 = false, printHash = false;
	        long keepAliveTimeout = -1;
	        int controlKeepAliveReplyTimeout = -1;
	        int port = 0;
	        
			String username = this.getServiceConfig().getLogin();
        	String password = this.getServiceConfig().getPassword();
		    String filepath = this.getServiceConfig().getProperties().getProperty(CustomQueryConstants.FTPFOLDER);
		    String server = this.getServiceConfig().getOnlineResource();
		    
	        FTPClient ftp = new FTPClient();
	        /*if (printHash) {
	            ftp.setCopyStreamListener(createListener());
	        }
	        if (keepAliveTimeout >= 0) {
	            ftp.setControlKeepAliveTimeout(keepAliveTimeout);
	        }
	        if (controlKeepAliveReplyTimeout >= 0) {
	            ftp.setControlKeepAliveReplyTimeout(controlKeepAliveReplyTimeout);
	        }*/
	        ftp.setListHiddenFiles(hidden);
	        //long startTime = -1;
	        long size = 0;
	        Status status;
	        try
	        {
	        	//startTime = new Long (System.currentTimeMillis ());
	            int reply;

	            if (port > 0) {
	                ftp.connect(server, port);
	            } else {
	                ftp.connect(server);
	            }
	    
	            // After connection attempt, you should check the reply code to verify success.
	            reply = ftp.getReplyCode();

	            if (!FTPReply.isPositiveCompletion(reply))
	            {
	                ftp.disconnect();
	                status = Status.RESULT_STATE_SERVICE_UNAVAILABLE;
	                String lastMessage = "FTP server refused connection";
	                response = new ValidatorResponse(lastMessage,status);
	                response.setLastLapse(-1);
	                response.setLastTest(Calendar.getInstance().getTime());
	            }
	            
	            if (response == null && !ftp.login(username, password))
	            {
	                ftp.logout();
	                status = Status.RESULT_STATE_SERVICE_UNAVAILABLE;
	                String lastMessage = "Login not valid";
	                response = new ValidatorResponse(lastMessage, status);
	                response.setLastLapse(-1);
	                response.setLastTest(Calendar.getInstance().getTime());
	            }
	            
	            if(response == null && (filepath == null || filepath.trim().equals("")))
	            {
	            	// Only test connecion;
	               	long lapse = (System.currentTimeMillis() - this.startTimeSync);
	            	response = new ValidatorResponse("",Status.RESULT_STATE_AVAILABLE);
	            	response.setLastLapse(lapse);
	            	response.setResponseLength(0);
	            	Calendar date = Calendar.getInstance();
	            	date.setTimeInMillis(this.startTimeSync);
	            	response.setLastTest(date.getTime());
	            	ftp.disconnect();
	            }
	            if(response == null)
	            {
		            if (binaryTransfer) {
		                ftp.setFileType(FTP.BINARY_FILE_TYPE);
		            } else {
		                // in theory this should not be necessary as servers should default to ASCII but they don't all do so - see NET-500
		                ftp.setFileType(FTP.ASCII_FILE_TYPE);
		            }
		            // Use passive mode as default because most of us are behind firewalls these days.
		            if (localActive) {
		                ftp.enterLocalActiveMode();
		            } else {
		                ftp.enterLocalPassiveMode();
		            }
		            ftp.setUseEPSVwithIPv4(useEpsvWithIPv4);
		            boolean fileFound = false;
		            if (listFiles)
		            {
		                for (FTPFile f : ftp.listFiles(filepath)) {
		                    if(f.isFile())
		                    {
		                    	fileFound = true;
		                    	size = f.getSize();
		                    	break;
		                    }
		                }
		            }
		            if(fileFound)
		            {
		            	long lapse = (System.currentTimeMillis() - this.startTimeSync);
		            	response = new ValidatorResponse("",Status.RESULT_STATE_AVAILABLE);
		            	response.setLastLapse(lapse);
		            	response.setResponseLength(size);
		            	Calendar date = Calendar.getInstance();
		            	date.setTimeInMillis(this.startTimeSync);
		            	response.setLastTest(date.getTime());
		            	ftp.disconnect();
		            }else
		            {
		                status = Status.RESULT_STATE_SERVICE_UNAVAILABLE;
		                String lastMessage = "File not found";
		                response = new ValidatorResponse( lastMessage, status);
		                response.setLastLapse( -1 );
		                response.setLastTest( Calendar.getInstance().getTime());
		            }
	            }
	        }
	        catch (IOException e)
	        {
	            if (ftp.isConnected())
	            {
	                try
	                {
	                    ftp.disconnect();
	                }
	                catch (IOException f)
	                {
	                	throw f;
	                }
	            }
	            throw e;
	        }
	        finally
	        {
	        	// Always close connection
	        	if (ftp.isConnected())
	            {
	                try
	                {
	                    ftp.disconnect();
	                }
	                catch (IOException f)
	                {
	                	throw f;
	                }
	            }
	        }
		}catch(Exception e) {
			response = new ValidatorResponse("FTP Unavailable: " + e.getLocalizedMessage(),Status.RESULT_STATE_PAGE_UNAVAILABLE);
			response.setLastLapse(-1);
			response.setLastTest(Calendar.getInstance().getTime());
		}
		this.getServiceLog().addMessage(response,this.getServiceConfig());
	}
	
	private static CopyStreamListener createListener(){
        return new CopyStreamListener(){
            private long megsTotal = 0;
//            @Override
            public void bytesTransferred(CopyStreamEvent event) {
                bytesTransferred(event.getTotalBytesTransferred(), event.getBytesTransferred(), event.getStreamSize());
            }
//            @Override
            public void bytesTransferred(long totalBytesTransferred,
                    int bytesTransferred, long streamSize) {
                long megs = totalBytesTransferred / 1000000;
                for (long l = megsTotal; l < megs; l++) {
                    System.err.print("#");
                }
                megsTotal = megs;
            }
        };
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