/**
 * 
 */
package org.easysdi.monitor.gui.webapp.views.json.serializers;

import java.util.Locale;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.easysdi.monitor.biz.job.QueryTestResult;
import org.codehaus.jackson.node.ObjectNode;
/**
 * @author BERG3428
 *
 */
public class QueryTestResultSerializer {

	/**
	 * 
	 */
    /**
     * No-argument private constructor used to prevent instantiation.
     */
    private QueryTestResultSerializer() {
        throw new UnsupportedOperationException(
                                           "This class can't be instantiated.");
    }
    
    public static JsonNode serialize(QueryTestResult resultItem, Locale locale, 
            ObjectMapper mapper) {
    	
    	final ObjectNode jsonResult = mapper.createObjectNode();
    	jsonResult.put("queryID", resultItem.getQueryid());
		jsonResult.put("content_type", resultItem.getContentType());
    	jsonResult.put("time", resultItem.getResponseDelay());
    	jsonResult.put("size", resultItem.getResponseSize());
    	jsonResult.put("xpath_result", resultItem.getXpathresult());
    	return jsonResult;
    }

}
