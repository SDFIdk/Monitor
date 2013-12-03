package org.easysdi.monitor.gui.webapp.views.csv.serializers;

import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.easysdi.monitor.biz.logging.AbstractAggregateLogEntry;
import org.easysdi.monitor.biz.logging.AggregateStats;
import org.easysdi.monitor.gui.i18n.Messages;

/**
 * Helper functions to create a CSV document displaying aggregate log entries.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
public final class AggregLogSerializer {
    
    /**
     * Hidden constructor (non instantiable class).
     */
    private AggregLogSerializer() {
        
    }

    
    
    /**
     * Transforms an aggregate log entry into a CSV entry.
     * 
     * @param entry             the aggregate log entry to serialize
     * @param fieldSeparator    the string used to separate the columns data
     * @return                  a string containing the aggregate log in CSV
     *                          format 
     */
    public static String serialize(AbstractAggregateLogEntry entry,
                            String fieldSeparator) {

        final AggregateStats h24Stats = entry.getH24Stats();
        final AggregateStats slaStats = entry.getSlaStats();
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        final List<String> elements = new LinkedList<String>();
        elements.add(dateFormat.format(entry.getLogDate().getTime()));
        elements.add(Float.toString(h24Stats.getMeanRespTime()));
        elements.add(Float.toString(slaStats.getMeanRespTime()));
        elements.add(Float.toString(h24Stats.getAvailability()));
        elements.add(Float.toString(slaStats.getAvailability()));
        elements.add(Float.toString(h24Stats.getNbBizErrors()));
        elements.add(Float.toString(slaStats.getNbBizErrors()));
        elements.add(Float.toString(h24Stats.getNbConnErrors()));
        elements.add(Float.toString(slaStats.getNbConnErrors()));
        elements.add(Float.toString(h24Stats.getMaxRespTime()));
        elements.add(Float.toString(slaStats.getMaxRespTime()));
        elements.add(Float.toString(h24Stats.getMinRespTime()));
        elements.add(Float.toString(slaStats.getMinRespTime()));
        elements.add(Float.toString(h24Stats.getUnavailability()));
        elements.add(Float.toString(slaStats.getUnavailability()));
        elements.add(Float.toString(h24Stats.getFailure()));
        elements.add(Float.toString(slaStats.getFailure()));
        elements.add(Float.toString(h24Stats.getUntested()));
        elements.add(Float.toString(slaStats.getUntested()));

        return StringUtils.join(elements.iterator(), fieldSeparator);
    }



    /**
     * Creates a columns titles entry.
     * 
     * @param fieldSeparator    the string used to separate the columns titles
     * @param locale            the locale used to display the columns titles
     *                          in the correct language
     * @return                  a string containing the columns titles entry
     */
    public static String generateHeaderLine(String fieldSeparator, 
                                            Locale locale) {

        final List<String> headers = new LinkedList<String>();
        final Messages i18nMessages = new Messages(locale);

        headers.add(i18nMessages.getMessage("log.header.date"));
        headers.add(i18nMessages.getMessage("log.header.h24.meanRespTime"));
        headers.add(i18nMessages.getMessage("log.header.sla.meanRespTime"));
        headers.add(i18nMessages.getMessage("log.header.h24.availability"));
        headers.add(i18nMessages.getMessage("log.header.sla.availability"));
        headers.add(i18nMessages.getMessage("log.header.h24.nbBizErrors"));
        headers.add(i18nMessages.getMessage("log.header.sla.nbBizErrors"));
        headers.add(i18nMessages.getMessage("log.header.h24.nbConnErrors"));
        headers.add(i18nMessages.getMessage("log.header.sla.nbConnErrors"));

        return StringUtils.join(headers.iterator(), fieldSeparator);
    }
}
