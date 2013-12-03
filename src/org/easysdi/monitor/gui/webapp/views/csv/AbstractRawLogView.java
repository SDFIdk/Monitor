package org.easysdi.monitor.gui.webapp.views.csv;

import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.easysdi.monitor.biz.logging.RawLogEntry;
import org.easysdi.monitor.gui.webapp.MonitorInterfaceException;
import org.springframework.web.servlet.support.RequestContextUtils;


/**
 * Defines an outline for displaying raw log entries in CSV format.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
public abstract class AbstractRawLogView extends AbstractCsvView {

    /**
     * Creates a new view.
     */
    public AbstractRawLogView() {
        super();
    }

    
    
    /**
     * Generates the raw logs CSV.
     * 
     * @param   model                       the model data to output
     * @param   request                     the servlet request that this view
     *                                      responds to
     * @return                              a string containing the raw logs as 
     *                                      CSV
     * @throws  MonitorInterfaceException   the model data is invalid
     */
    @SuppressWarnings("unchecked")
    @Override
    public String generateViewContent(Map<String, ?> model, 
                                      HttpServletRequest request) 
        throws MonitorInterfaceException {

        if (model.containsKey("rawLogsCollection")
                && model.containsKey("addQueryId")) {
            final Set<RawLogEntry> logsCollection 
                = (Set<RawLogEntry>) model.get("rawLogsCollection");
            final Boolean queryIdDisplayed = (Boolean) model.get("addQueryId");
            final Locale locale = RequestContextUtils.getLocale(request);

            return this.serializeData(logsCollection, queryIdDisplayed, locale);
            
        }
            
        throw new MonitorInterfaceException("An internal error occurred",
                                            "internal.error");
    }
    
    
    
    /**
     * Transforms the raw logs in a CSV document.
     * 
     * @param logsCollection    a set containing the logs to serialize
     * @param queryIdDisplayed  <code>true</code> to add the query 
     *                          identifier to each CSV entry
     * @param locale            the locale to use to display the data
     * @return                  a string containing the raw logs as CSV
     */
    protected abstract String serializeData(Set<RawLogEntry> logsCollection, 
                                   boolean queryIdDisplayed, Locale locale);
}
