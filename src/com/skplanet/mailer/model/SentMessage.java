package com.skplanet.mailer.model;

import java.util.Date;

import com.skplanet.mailer.db.MailDao;

public class SentMessage {
	private MailDao mail;
	private Date registerTime;
	private Long removed;
	
	public MailDao getMail() {
		return mail;
	}
	public void setMail(MailDao mail) {
		this.mail = mail;
	}
	public Date getRegisterTime() {
		return registerTime;
	}
	public void setRegisterTime(Date registerTime) {
		this.registerTime = registerTime;
	}
	public Long getRemoved() {
		return removed;
	}
	public void setRemoved(Long removed) {
		this.removed = removed;
	}
}
