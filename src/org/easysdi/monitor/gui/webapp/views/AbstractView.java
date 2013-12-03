package org.easysdi.monitor.gui.webapp.views;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.easysdi.monitor.gui.webapp.MonitorInterfaceException;

import org.springframework.web.servlet.View;

/**
 * Spring MVC view outline.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.1.1, 2010-03-19
 *
 */
public abstract class AbstractView implements View {

    /**
     * Gets the content type string for the response.
     * 
     * @return the content type string
     */
    public abstract String getContentType();

    
    
    /**
     * Generates the response for this view.
     * 
     * @param   model                       the model data to output
     * @param   request                     the servlet request that this view 
     *                                      responds to
     * @param   response                    the servlet response to write to
     * @throws  MonitorInterfaceException   the view couldn't be rendered
     */
    public final void render(Map<String, ?> model, HttpServletRequest request,
                             HttpServletResponse response) 
        throws MonitorInterfaceException {

 
    	
        final String viewContent = this.generateViewContent(model, request);
        response.setContentType(this.getContentType());
        response.setCharacterEncoding("UTF-8");
   

        this.writeResponse(viewContent, response);
    }
    
    
    /**
     * Generates the data presentation through the view.
     * 
     * @param   model                       the model data to output
     * @param   request                     the servlet request
     * @return                              a string containing the generated 
     *                                      view content
     * @throws  MonitorInterfaceException   the model data is invalid
     */    
    protected abstract String generateViewContent(Map<String, ?> model, 
                                                  HttpServletRequest request)
        throws MonitorInterfaceException;
    
    
    
    /**
     * Writes the response body.
     * 
     * @param viewContent                   a string containing the data 
     *                                      presented through this view 
     * @param response                      the servlet response whose body 
     *                                      must be written
     * @throws MonitorInterfaceException    an I/O error occurred while writing
     *                                      the response
     */
    protected final void writeResponse(String viewContent, 
                                    HttpServletResponse response) 
        throws MonitorInterfaceException {
        
        if (null == response) {
            throw new IllegalArgumentException("Servlet response is null.");
        }

        this.setAdditionalHeaders(response);
        
        try {            
            response.getOutputStream().write(viewContent.getBytes("UTF-8"));
            response.getOutputStream().flush();
            response.getOutputStream().close();
            
        } catch (IOException e) {
            
            throw new MonitorInterfaceException(
                    "An error occurred while writing the response", 
                    "response.write.error");
        }
    }
    
    
    
    /**
     * Defines custom response headers if required.
     * 
     * @param   response    the servlet response
     */
    protected void setAdditionalHeaders(HttpServletResponse response) {
        
    }
}
