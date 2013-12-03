package org.easysdi.monitor.gui.webapp.views.csv;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.easysdi.monitor.biz.logging.AbstractAggregateLogEntry;
import org.easysdi.monitor.gui.webapp.MonitorInterfaceException;
import org.easysdi.monitor.gui.webapp.views.csv.serializers.AggregLogSerializer;
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 * Displays a set of aggregate log entries in CSV format.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
public class AggregLogView extends AbstractCsvView {
    
    /**
     * Creates a new view.
     */
    public AggregLogView() {
        super();
    }

    
    
    /**
     * Generates the aggregate logs CSV.
     * 
     * @param   model                       the model data to output
     * @param   request                     the servlet request that this view 
     *                                      responds to
     * @return                              a string containing the aggregate 
     *                                      logs as CSV
     * @throws  MonitorInterfaceException   the model data is invalid
     */
    @SuppressWarnings("unchecked")
    @Override
    public String generateViewContent(Map<String, ?> model, 
                                      HttpServletRequest request) 
        throws MonitorInterfaceException {

        final List<String> fileLines = new LinkedList<String>();

        if (model.containsKey("aggregLogsCollection")) {
            final Collection<AbstractAggregateLogEntry> logsCollection 
                = (Collection<AbstractAggregateLogEntry>) model.get(
                     "aggregLogsCollection");
            
            final Locale locale = RequestContextUtils.getLocale(request);

            fileLines.add(AggregLogSerializer.generateHeaderLine(
                 FIELD_SEPARATOR,
                 locale));

            for (AbstractAggregateLogEntry logEntry : logsCollection) {
                fileLines.add(AggregLogSerializer.serialize(logEntry,
                                                            FIELD_SEPARATOR));
            }

            return StringUtils.join(fileLines.iterator(), ENTRY_SEPARATOR);
            
        }
        
        throw new MonitorInterfaceException("An internal error occurred",
                                            "internal.error");
    }

}
