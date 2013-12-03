package org.easysdi.monitor.biz.alert;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.deegree.framework.mail.EMailMessage;
import org.deegree.framework.mail.MailHelper;
import org.deegree.framework.mail.SendMailException;
import org.easysdi.monitor.biz.job.Job;
import org.easysdi.monitor.gui.i18n.Messages;

/**
 * This class is used to send an e-mail when an alert is raised.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 * 
 */
public class EmailAction extends AbstractAction {

    private final EmailConfig config = new EmailConfig();
    private final Logger      logger = Logger.getLogger(EmailAction.class);
    private Set<String>       recipients;
    



    /**
     * No-argument constructor, used by the persistance mechanism.
     */
    @SuppressWarnings("unused")
    private EmailAction() {

    }



    /**
     * Creates a new e-mail action.
     * 
     * @param aParentJob
     *            the job that this action is attached to
     * @param aTarget
     *            a list of e-mail addresses separated by commas. Invalid
     *            adresses are ignored.
     */
    public EmailAction(Job aParentJob, String aTarget) {
        super(aParentJob, aTarget);
        this.setRecipients(aTarget);
        this.setType(ActionType.getFromName("E-MAIL"));
    }



    /**
     * Defines the recipients.
     * 
     * @param aTarget   a list of e-mail addresses separated by commas. Invalid
     *                  addresses are ignored.
     */
    @Override
    protected void processTargetSpecific(String aTarget) {
        this.setRecipients(aTarget);
    }



    /**
     * Sends the e-mail following an alert.
     * 
     * @param aAlert    the alert that triggers the action
     */
    @Override
    public void trigger(Alert aAlert) {

        if (null == aAlert) {
            throw new IllegalArgumentException("Alert can't be null.");
        }
        
        final EmailConfig mailConfig = this.getConfig();
        final String sender = mailConfig.getSenderAddress();
        final String recipientsList = this.getRecipientsString();
        final String jobName = aAlert.getParentJob().getConfig().getJobName();
        
                
        	//String.format("%1$s: %2$s\n",
            //    i18n.getMessage("general.oldStatus"),
                
        String language = this.getLanguage();
        
        if (null == language) {
            language = mailConfig.getLanguage();
        }
        
        final Locale actionLocale = new Locale(language);        
        final Messages i18n = new Messages(actionLocale);
        final String newStatus = aAlert.getNewStatus().getDisplayString(i18n.getLocale());
        
        final String automaticMsg = i18n.getMessage("mail.subject.automatic");
        final String statusMsg = i18n.getMessage("mail.subject.statusChanged");
        final String subject = String.format("%1$s [%2$s] %3$s: %4$s",
                                             automaticMsg, jobName, statusMsg, newStatus);

        final String body = this.buildBody(aAlert, jobName, i18n);

        final EMailMessage message = new EMailMessage(sender, recipientsList,
                                                      subject, body.toString());

        try {
            this.sendMail(message, mailConfig);

        } catch (SendMailException e) {
            this.logger.error("Unable to send alert e-mail", e);
        }
    }



    /**
     * Sends an e-mail message.
     * 
     * @param   message             the message to send
     * @param   mailConfig          the config object containing the SMTP 
     *                              configuration
     * @throws  SendMailException   an lower-level error occurred while sending
     *                              the message
     */
    private void sendMail(final EMailMessage message,
                          final EmailConfig mailConfig) 
        throws SendMailException {
        
        final String host = mailConfig.getSmtpHost();
        final String userName = mailConfig.getSmtpUserName();

        if (StringUtils.isNotBlank(userName)) {
            MailHelper.createAndSendMail(message, host, userName,
                                         mailConfig.getSmtpPassword());
            
        } else {
            MailHelper.createAndSendMail(message, host);
        }
    }



    /**
     * Builds the message's body.
     * 
     * @param alert         the alert that triggered this action
     * @param jobName       the parent job's name
     * @param i18n          the messages object providing access to 
     *                      internationalized strings 
     * @return              a string containing the message body
     */
    private String buildBody(Alert alert, String jobName,
                             Messages i18n) {
        final Locale actionLocale = i18n.getLocale();
        final StringBuilder body = new StringBuilder();
        final SimpleDateFormat dateFormat 
            = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final Date alertTime = alert.getTime().getTime();
        
        body.append(i18n.getMessage("mail.body.noAnswer")).append("\n\n");
        body.append(i18n.getMessage("mail.body.statusChanged")).append(" "+jobName).append("\n\n");
        
        body.append(String.format("%1$s: %2$s\n",
                                  i18n.getMessage("general.oldStatus"),
                                  alert.getOldStatus().getDisplayString(
                                           actionLocale)));
        body.append(String.format("%1$s: %2$s\n",
                                  i18n.getMessage("general.newStatus"),
                                  alert.getNewStatus().getDisplayString(
                                           actionLocale)));        
        body.append(String.format("%1$s: %2$s\n",
                                  i18n.getMessage("general.cause"),
                                  alert.getCause()));
        body.append(String.format("%1$s: %2$s\n",
                                  i18n.getMessage("mail.body.dateTimeChange"),
                                  dateFormat.format(alertTime)));
        //body.append(String.format("\n%1$s\n",
        //                          i18n.getMessage("mail.body.greetings")));
        
        return body.toString();
    }



    /**
     * Gets the object containing the configuration for sending an e-mail.
     * 
     * @return the email configuration
     */
    private EmailConfig getConfig() {
        return this.config;
    }



    /**
     * Gets the recipients list for this e-mail.
     * 
     * @return a set containing the recipients' adresses
     */
    private Set<String> getRecipients() {

        if (null == this.recipients) {
            this.setRecipients(this.getTarget());
        }

        return Collections.unmodifiableSet(this.recipients);
    }



    /**
     * Gets the recipients list as a single string.
     * 
     * @return the string containing the recipients' addresses
     */
    private String getRecipientsString() {
        return StringUtils.join(this.getRecipients().iterator(), ",");
    }



    /**
     * Sets the e-mail recipients.
     * <p>
     * Splits the passed string and checks the validity of each address. Invalid
     * ones are ignored
     * 
     * @param recipientsList
     *            a comma-separated string containing the recipients' addresses
     */
    private void setRecipients(String recipientsList) {
        final Set<String> newRecipients = new HashSet<String>();

        for (String addressString : recipientsList.split(",")) {
            final String trimmedAddress = addressString.trim();

            if (this.isValidAddress(trimmedAddress)
                && !newRecipients.contains(trimmedAddress)) {
                newRecipients.add(trimmedAddress);
            }
        }

        this.recipients = newRecipients;
    }



    /**
     * Checks if an e-mail address is valid.
     * <p>
     * To be deemed valid, the address must respect the norm AND specify a host
     * name.
     * 
     * @param aAddressString    the e-mail address to check
     * @return                  <code>true</code> if the address is valid<br>
     *                          <code>false</code> otherwise
     */
    private boolean isValidAddress(String aAddressString) {
        boolean validity;

        try {
            @SuppressWarnings("unused")
            final InternetAddress address = new InternetAddress(aAddressString);

            validity = this.hasNameAndHost(aAddressString);

        } catch (AddressException e) {
            validity = false;
        }

        return validity;
    }



    /**
     * Checks if an e-mail address contains both a user name and a host name
     * (toto@titi).
     * 
     * @param aAddressString    the e-mail address to check
     * @return                  <code>true</code> if the address has both 
     *                          required parts<br>
     *                          <code>false</code> otherwise
     */
    private boolean hasNameAndHost(String aAddressString) {
        final String[] addressParts = aAddressString.split("@");

        return (2 == addressParts.length 
                && !addressParts[0].equals("") 
                && !addressParts[1].equals(""));
    }
}
