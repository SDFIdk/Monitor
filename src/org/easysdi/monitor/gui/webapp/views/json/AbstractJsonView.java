package org.easysdi.monitor.gui.webapp.views.json;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.easysdi.monitor.gui.i18n.Messages;
import org.easysdi.monitor.gui.webapp.AppContext;
import org.easysdi.monitor.gui.webapp.MonitorInterfaceException;
import org.easysdi.monitor.gui.webapp.views.AbstractView;
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 * Spring MVC View displaying the model data as JSON.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.2, 2010-08-06
 *
 */
public abstract class AbstractJsonView extends AbstractView {
    
    private ObjectMapper objectMapper;
    private final Logger logger = Logger.getLogger(AbstractJsonView.class);
    private ObjectNode rootObjectNode;
    
    /**
     * Gets the root object used to transform object to or from JSON.
     * 
     * @return  the JSON root object mapper
     */
    protected ObjectNode getRootObjectNode(){
    	if (null == this.objectMapper)
    		this.getObjectMapper();
    	
    	if (null == this.rootObjectNode)
    	    this.rootObjectNode = this.objectMapper.createObjectNode();
    	
    	return this.rootObjectNode;
    }
    
    /**
     * Gets the object used to transform object to or from JSON.
     * 
     * @return  the JSON object mapper
     */
    protected ObjectMapper getObjectMapper() {
        
        if (null == this.objectMapper) {
            this.objectMapper 
                = AppContext.getContext().getBean("jsonMapper", 
                                                  ObjectMapper.class);
        }
         
        return this.objectMapper;
    }
    
    
    
    /**
     * Builds the JSON string with the content to send to the user.
     * 
     * @param   model   the map containing the data to display
     * @param   request the HTTP request that produced the data to display
     * @return          a string containing the response data in JSON format
     * @throws  MonitorInterfaceException   an error occurred while the data
     *                                      was converted to JSON
     */
    @Override
    protected String generateViewContent(Map<String, ?> model, 
                                         HttpServletRequest request) 
        throws MonitorInterfaceException {
        
        final Locale locale = RequestContextUtils.getLocale(request);
        final ObjectMapper mapper = this.getObjectMapper();
        final ObjectNode root = this.getRootObjectNode();
        root.put("success", this.isSuccess());
        root.put("message", this.getResponseMessage(model, locale));
        root.put("data", this.getResponseData(model, locale));
        // this check is carried out in case we are returning results for a paginated query.
        if(model.containsKey("count"))
        	root.put("count", model.get("count").toString());
        
        try {
            return mapper.writeValueAsString(root);
            
        } catch (JsonGenerationException exception) {
            throw this.processError(exception);
            
        } catch (JsonMappingException exception) {
            throw this.processError(exception);
            
        } catch (IOException exception) {
            throw this.processError(exception);
        }
    };
    
    
    
    /**
     * Carries the appropriate actions when an error is raised.
     * <p>
     * Logs the original exception and wraps it.
     * 
     * @param   exception   the original exception thrown when processing the 
     *                      data to display
     * @return              a Monitor interface exception
     */
    private MonitorInterfaceException processError(Exception exception) {
        this.logger.error("Error during the conversion of the response to JSON",
                          exception);
        
        return new MonitorInterfaceException(
                "An error occured while the response was written", 
                "response.write.error");
    }
    
    
    
    /**
     * Tells whether the operation that triggered this view succeeded.
     * 
     * @return  <code>true</code> if the operation was a success
     */
    protected abstract Boolean isSuccess();
    
    
    
    /**
     * Gets the message explaining the result of the operation displayed in 
     * this view.
     * 
     * @param   model   a map containing the data to display (including the 
     *                  message)
     * @param   locale  the locale indicating in which language the message 
     *                  should be displayed
     * @return          the operation message
     */
    protected String getResponseMessage(Map<String, ?> model, Locale locale) {
        final Messages i18n = new Messages(locale);
        
        if (model.containsKey("message")) {
            final String messageKey = (String) model.get("message");
            
            return i18n.getMessage(messageKey);
        }
            
        return "";
    }
    
    
    
    /**
     * Retrieves the JSON-formatted data to return. 
     * 
     * @param   model   a map containing the data to display
     * @param   locale  the locale indicating the language in which localizable
     *                  data should be shown
     * @return          a JSON node containing the result data
     * @throws  MonitorInterfaceException   an error occurred while the data 
     *                                      was formatted
     */
    protected abstract JsonNode getResponseData(Map<String, ?> model,
                                                Locale locale)
        throws MonitorInterfaceException;
    
    
    
    /**
     * Gets the content type string for the response.
     * 
     * @return the JSON content type 
     */
    @Override
    public final String getContentType() {
        return "application/json";
    }
}
