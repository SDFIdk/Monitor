package org.easysdi.monitor.biz.alert;

import org.easysdi.monitor.biz.job.Job;

/**
 * Exposes raised alerts to the job's RSS feed
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 */
public class RssAction extends AbstractAction {

    /**
     * No-argument constructor, used by the persistance mechanism.
     */
    protected RssAction() {

    }



    /**
     * Instantiates an RSS action.
     * 
     * @param   parentJob   the job which this action is attached to
     */
    public RssAction(Job parentJob) {
        super(parentJob, null);
        this.setType(ActionType.getFromName("RSS"));
    }



    /**
     * Exposes an alert to the job's RSS feed.
     * 
     * @param   alert   the alert to expose
     */
    @Override
    public void trigger(Alert alert) {

        if (null != alert) {
            alert.setExposedToRss(true);
        }
    }

}
