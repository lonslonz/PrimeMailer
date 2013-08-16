package com.skplanet.mailer.mail;

import org.apache.commons.mail.Email;

import com.skplanet.mailer.db.MailDao;

public class MailInfo {
    private Email email;
    private MailDao mailDao;
    
    public MailInfo(Email email, MailDao mailDao) {
        this.email = email;
        this.mailDao = mailDao;
    }
    
    public Email getEmail() {
        return email;
    }
    public void setEmail(Email email) {
        this.email = email;
    }
    public MailDao getMailDao() {
        return mailDao;
    }
    public void setMailDao(MailDao mailDao) {
        this.mailDao = mailDao;
    }
    
    
    
}
