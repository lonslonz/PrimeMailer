package com.skplanet.mailer.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="tb_mail")
public class MailDao {
    private Integer mailId;
    private String sender;
    private String recipient;
    private String subject;
    private String message;
    private String smtpServer;
    private String smtpPort;
    private String useSsl;
    private String returnId;
    private String description;
    private String saveTime;
    private String updateTime;
    private String id;
    private byte[] password;
    private Boolean needSend = true;
    private Integer windowSize = 1;
    
    @Id @GeneratedValue
    @Column(name = "mail_id")
    public Integer getMailId() {
        return mailId;
    }
    public void setMailId(Integer mailId) {
        this.mailId = mailId;
    }
    @Column(name = "sender")
    public String getSender() {
        return sender;
    }
    public void setSender(String sender) {
        this.sender = sender;
    }
    @Column(name = "recipient")
    public String getRecipient() {
        return recipient;
    }
    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }
    @Column(name = "subject")
    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }
    @Column(name = "message")
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    @Column(name = "smtp_server")
    public String getSmtpServer() {
        return smtpServer;
    }
    public void setSmtpServer(String smtpServer) {
        this.smtpServer = smtpServer;
    }
    @Column(name = "smtp_port")
    public String getSmtpPort() {
        return smtpPort;
    }
    public void setSmtpPort(String smtpPort) {
        this.smtpPort = smtpPort;
    }
    @Column(name = "use_ssl")
    public String getUseSsl() {
        return useSsl;
    }
    public void setUseSsl(String useSsl) {
        this.useSsl = useSsl;
    }
    
    @Column(name = "return_id")
    public String getReturnId() {
        return returnId;
    }
    public void setReturnId(String returnId) {
        this.returnId = returnId;
    }
    @Column(name = "update_time")
    public String getUpdateTime() {
        return updateTime;
    }
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
    @Column(name = "description")
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    @Column(name = "id")
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    @Column(name = "password")
    public byte[] getPassword() {
        return password;
    }
    public void setPassword(byte[] password) {
        this.password = password;
    }
    @Column(name = "save_time")
    public String getSaveTime() {
        return saveTime;
    }
    public void setSaveTime(String saveTime) {
        this.saveTime = saveTime;
    }
    @Transient
    public Boolean getNeedSend() {
        return needSend;
    }
    public void setNeedSend(Boolean needSend) {
        this.needSend = needSend;
    }
    
}
