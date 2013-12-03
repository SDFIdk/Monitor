package org.easysdi.monitor.gui.webapp.views.csv;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.easysdi.monitor.biz.logging.RawLogEntry;
import org.easysdi.monitor.gui.webapp.views.csv.serializers.RawLogSerializer;


/**
 * Displays a set of raw logs in CSV format.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 */
public class RawLogView extends AbstractRawLogView {
    
    /**
     * Creates a new view.
     */
    public RawLogView() {
        super();
    }
    
    

    /**
     * {@inheritDoc}
     */
    @Override
    protected String serializeData(Set<RawLogEntry> logsCollection,
                                   boolean queryIdDisplayed, Locale locale) {
        final List<String> fileLines = new LinkedList<String>();
    
        fileLines.add(RawLogSerializer.generateHeaderLine(FIELD_SEPARATOR,
                                                          queryIdDisplayed,
                                                          locale));
    
        for (RawLogEntry logEntry : logsCollection) {
            fileLines.add(RawLogSerializer.serialize(logEntry,
                                                     FIELD_SEPARATOR,
                                                     queryIdDisplayed, locale));
        }
    
        return StringUtils.join(fileLines.iterator(), ENTRY_SEPARATOR);
    }

}
