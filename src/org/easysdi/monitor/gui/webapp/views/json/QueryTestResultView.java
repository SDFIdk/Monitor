package org.easysdi.monitor.gui.webapp.views.json;

import java.util.Locale;
import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import org.easysdi.monitor.biz.job.QueryTestResult;
import org.easysdi.monitor.gui.webapp.MonitorInterfaceException;
import org.easysdi.monitor.gui.webapp.views.json.serializers.QueryTestResultSerializer;

public class QueryTestResultView extends AbstractJsonView {

	public QueryTestResultView() {
	}
	
	 /**
     * Generates the JSON code for the query result.
     * 
     * @param   model                       the model data to output
     * @param   locale                      the locale indicating the language 
     *                                      in which localizable data should be 
     *                                      shown
     * @return                              a string containing the JSON code 
     *                                      for the query result
     * @throws  MonitorInterfaceException   <ul>
     *                                      <li>the model data is invalid</li>
     *                                      <li>an error occurred while 
     *                                      converting the query result to JSON
     *                                      </li>
     *                                      </ul>
     */
    @Override
    protected JsonNode getResponseData(Map<String, ?> model, Locale locale) 
        throws MonitorInterfaceException {

        if (model.containsKey("queryTestResult")) {
            final QueryTestResult queryTestResult = (QueryTestResult) model.get("queryTestResult");
            final ObjectMapper mapper = this.getObjectMapper();
            return QueryTestResultSerializer.serialize(queryTestResult, locale, mapper);
        } 
        throw new MonitorInterfaceException("An internal error occurred",
                                            "internal.error");
    }
	
	/**
    * {@inheritDoc}
    */
    @Override
    protected Boolean isSuccess() {
        return true;
    }
}
