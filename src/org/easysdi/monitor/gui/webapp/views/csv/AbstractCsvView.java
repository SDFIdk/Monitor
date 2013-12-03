package org.easysdi.monitor.gui.webapp.views.csv;

import javax.servlet.http.HttpServletResponse;

import org.easysdi.monitor.gui.webapp.views.AbstractView;


/**
 * Spring MVC View displaying the model data as CSV.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
public abstract class AbstractCsvView extends AbstractView {

    /**
     * String used to separate columns data.
     */
    protected static final String FIELD_SEPARATOR = ";";
    
    /**
     * String used to separate lines of data.
     */
    protected static final String ENTRY_SEPARATOR = "\n";



    /**
     * Creates a new CSV view.
     */
    public AbstractCsvView() {
    }



    /**
     * Gets the content type string for the response.
     * 
     * @return  the CSV content type string 
     */
    @Override
    public final String getContentType() {
        return "text/csv";
    }
    
    
    
    /**
     * Defines specific headers for CSV content.
     * 
     * @param   response    the servlet response
     */
    @Override
    protected final void setAdditionalHeaders(HttpServletResponse response) {
        
        response.setHeader("Content-Disposition", 
                           "attachment; filename=\"logs.csv\"");
    }
}
