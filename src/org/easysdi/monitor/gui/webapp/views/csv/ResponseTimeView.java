package org.easysdi.monitor.gui.webapp.views.csv;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.easysdi.monitor.biz.logging.RawLogEntry;
import 
    org.easysdi.monitor.gui.webapp.views.csv.serializers.ResponseTimeSerializer;

/**
 * Displays a set of response times in CSV format.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
public class ResponseTimeView extends AbstractRawLogView {
    
    /**
     * Creates a new view.
     */
    public ResponseTimeView() {
        super();
    }
    
    

    /**
     * Transforms raw log entries in a response times CSV document.
     * 
     * @param   logsCollection      a set of raw logs to transform
     * @param   queryIdDisplayed    <code>true</code> to add the query 
     *                              identifier to each CSV entry
     * @param   locale              the locale to use to display the data
     * @return                      a string containing the response 
     *                              times as CSV
     */
    @Override
    public String serializeData(Set<RawLogEntry> logsCollection,
                                boolean queryIdDisplayed, Locale locale) {

        final List<String> fileLines = new LinkedList<String>();

        fileLines.add(ResponseTimeSerializer.generateHeaderLine(
            FIELD_SEPARATOR, queryIdDisplayed, locale));

        for (RawLogEntry logEntry : logsCollection) {
            fileLines.add(ResponseTimeSerializer.serialize(logEntry,
                                                           FIELD_SEPARATOR,
                                                           queryIdDisplayed));
        }

        return StringUtils.join(fileLines.iterator(), ENTRY_SEPARATOR);
    }

}
