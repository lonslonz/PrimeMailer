package com.skplanet.mailer.mail;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skplanet.cask.container.config.ConfigReader;
import com.skplanet.mailer.db.MailDao;
import com.skplanet.mailer.model.SentMessage;

public class DuplicatedWindow {
	private List<SentMessage> sentMessages = new LinkedList<SentMessage>();
	private static DuplicatedWindow instance = new DuplicatedWindow();
	private Logger logger = LoggerFactory.getLogger(DuplicatedWindow.class);
	private String windowTime = null;
	
	public static DuplicatedWindow getInstance() {
		return instance;
	}
	
	public synchronized void register(MailDao message) {
		
		SentMessage newMessage = new SentMessage();
		newMessage.setMail(message);
		newMessage.setRegisterTime(new Date());
		newMessage.setRemoved(0L);
		sentMessages.add(newMessage);
	}
	public synchronized Boolean existSameMessage(MailDao message) {
		return findMessageByContent(message) != null ? true : false;
	}
	public synchronized SentMessage findMessageByContent(MailDao message) {
		for(SentMessage sentMessage : sentMessages) {
			if(isSameMessage(message, sentMessage.getMail())) {
				return sentMessage;
			}
		}
		return null;
	}
	public synchronized void removeStaled() {
		if(ConfigReader.getInstance().getServerConfig() != null) {
			windowTime = ConfigReader.getInstance().getServerConfig().getPropValue("duplicateMessageWindowTime");
		}
		
		long limit = Integer.valueOf(windowTime) * 60 * 1000;
		
		Date curr = new Date();
		
		List<SentMessage> newSentMessages = new LinkedList<SentMessage>();
		for(SentMessage sentMessage : sentMessages) {
			if(curr.getTime() - sentMessage.getRegisterTime().getTime() < limit) {
				newSentMessages.add(sentMessage);
			}
		}
		sentMessages = newSentMessages;
	}
	public static boolean isSameMessage(MailDao first, MailDao second) {
        Logger logger = LoggerFactory.getLogger(DuplicatedWindow.class);
        if(!first.getSubject().equals(second.getSubject())) {
            return false;
        } 
        logger.info("first remove bracket : {}", removeBracket(first.getMessage()));
        logger.info("second remove bracket : {}", removeBracket(second.getMessage()));
        if(!removeBracket(first.getMessage()).equals(removeBracket(second.getMessage()))) {
            return false;
        }
        
        return true;
    }
	public static String removeBracket(String message) {
        String remove = message.replaceAll("\\[[^\\]]*\\]", "");
		return remove;
    }

	public String getWindowTime() {
		return windowTime;
	}

	public void setWindowTime(String windowTime) {
		this.windowTime = windowTime;
	}
	
}
