package com.skplanet.mailer.mail;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailConstants;
import org.apache.commons.mail.SimpleEmail;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skplanet.cask.container.config.ConfigReader;
import com.skplanet.cask.util.StringUtil;
import com.skplanet.mailer.db.MailDao;
import com.skplanet.mailer.db.MailRcptAliasDao;
import com.skplanet.mailer.util.Crypto;
import com.skplanet.mailer.util.HiberUtil;

public class Mailer {
    private Logger logger = LoggerFactory.getLogger(Mailer.class);
    
    private static Mailer instance = new Mailer();
    private static int TIME_OUT = 10000; 
    private static int RETRY_COUNT = 3;
    
    public static Mailer getInstance() {
        return instance;
    }

    public void send(
            String from, 
            String to, 
            String subject, 
            String msg, 
            String smtpServer, 
            String smtpPort,
            String id,
            String password,
            String ssl) throws Exception {
        
        MailDao mailDao = new MailDao();
        mailDao.setSender(from);
        mailDao.setRecipient(to);
        mailDao.setSubject(subject);
        mailDao.setMessage(msg);
        mailDao.setUseSsl(ssl);
        mailDao.setSmtpServer(smtpServer);
        mailDao.setSmtpPort(smtpPort);
        mailDao.setId(id);
        
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mailDao.setSaveTime(df.format(new Date()));
        
        if(password != null) {
            Crypto crypt = new Crypto();
            crypt.init(ConfigReader.getInstance().getServerConfig().getPropValue("crPassword"),
                        ConfigReader.getInstance().getServerConfig().getPropValue("crSalt"));
            byte[] mypass = crypt.encrypt(password);
            mailDao.setPassword(mypass);
        } else {
            mailDao.setPassword(null);
        }
        saveIntoDb(mailDao);
        logger.info(
                "save mail: mail id : {}, returnId : {}, from : {}, to : {}, subject : {}, msg : {}, smtpServer : {}, smtpPort : {}" + 
                "sender id : {}, password : {}", 
                new Object[]{ 
                        mailDao.getMailId(),  
                        mailDao.getReturnId(),
                        mailDao.getSender(),
                        mailDao.getRecipient(), 
                        mailDao.getSubject(), 
                        mailDao.getMessage(), 
                        mailDao.getSmtpServer(),
                        mailDao.getSmtpPort(),
                        mailDao.getSender(),
                        mailDao.getPassword()});

    }
    
    private void disableSameMessage(int index, List<MailDao> mailList) {
        
        for(int i = 0; i < mailList.size(); ++i) {
            if(i > index) {
                MailDao curr = mailList.get(i);
                if(curr.getNeedSend() && isSameMessage(curr, mailList.get(index))) {
                    curr.setNeedSend(false);
                    curr.setReturnId("disabled");
                    logger.info("mail disables. id : {}", curr.getMailId());
                }
            }
        }
    }
    
    private boolean isSameMessage(MailDao first, MailDao second) {
        
        if(!first.getSubject().equals(second.getSubject())) {
            return false;
        } 
        if(!removeBracket(first.getMessage()).equals(removeBracket(second.getMessage()))) {
            return false;
        }
        
        return true;
    }
    private String removeBracket(String message) {
        String remove = message.replaceAll("\\[.*\\]", "");
        return remove;
    }
    
    private String getDecryptPassword(MailDao dao) throws Exception {
        Crypto crypt = new Crypto();
        crypt.init(ConfigReader.getInstance().getServerConfig().getPropValue("crPassword"),
                ConfigReader.getInstance().getServerConfig().getPropValue("crSalt"));
        return crypt.decrypt(dao.getPassword());
    }
    private List<String> resolveAlias(String alias) throws Exception {
        
        List<MailRcptAliasDao> aliasList = selectAlias(alias);
        List<String> rcptList = new ArrayList<String>();
        
        if(aliasList.size() == 0) {
            logger.info("no alias. send to : {}", alias);
            rcptList.add(alias);
        } else { 
            
            for(int i = 0; i < aliasList.size(); i++) {
                MailRcptAliasDao curr = aliasList.get(i);
                String rcpt = curr.getRcptAddr();
                rcptList.add(rcpt);
            }
            logger.info("resolve alias. alias {}, send to : {}", alias, aliasList.toString());
        }
        return rcptList;
    }
    private void sendRealLow(MailDao curr) throws Exception {
        try {
            Email email = new SimpleEmail();
            email.setCharset(EmailConstants.UTF_8);
            
            if(curr.getId() != null) {
                email.setAuthenticator(
                        new DefaultAuthenticator(curr.getId(), 
                                                 getDecryptPassword(curr)));
            }
            
            if(curr.getUseSsl() != null) {
                email.setSSLOnConnect(Boolean.parseBoolean(curr.getUseSsl()));
            }
            
            email.setHostName(curr.getSmtpServer());
            email.setSmtpPort(Integer.parseInt(curr.getSmtpPort()));
            
            email.setFrom(curr.getSender());
            email.setSubject(curr.getSubject());
            email.setMsg(curr.getMessage());
            
            
            String[] receivers = curr.getRecipient().split("[,;]");
            for(int j = 0; j < receivers.length; j++) {
                List<String> realRcpt = resolveAlias(receivers[j]);
                for(int k = 0; k < realRcpt.size(); k++) {
                    email.addTo(realRcpt.get(k));    
                }
            }
             
            email.setSocketConnectionTimeout(TIME_OUT);
            email.setSocketTimeout(TIME_OUT);
            
            int retryCount = 0;
            while(true) {
                try {
                    String sendId = email.send();
                    curr.setReturnId(sendId);
                    break;
                } catch(Exception e) {
                    logger.error("mail send failed. retry : {}, {}", retryCount, StringUtil.exception2Str(e));
                    ++retryCount;
                    if(retryCount > RETRY_COUNT) {
                        throw e;
                    }
                    continue;
                }
            }
            
        } catch(Exception e) {
          curr.setDescription(e.getMessage());
          curr.setReturnId("failed");
          throw e;
        } finally {
            update(curr);
            logger.info(
                    "send mail: id : {}, returnId : {}, from : {}, to : {}, subject : {}, msg : {}, smtpServer : {}, smtpPort : {}", 
                    new Object[]{ 
                            curr.getMailId(),  
                            curr.getReturnId(),
                            curr.getSender(),
                            curr.getRecipient(), 
                            curr.getSubject(), 
                            curr.getMessage(), 
                            curr.getSmtpServer(),
                            curr.getSmtpPort()});
        }
    }
    public int sendRealNotSent() throws Exception {
        
        List<MailDao> mailList = selectNotSent();
        
        
        for(int i = 0; i < mailList.size(); ++i) {
            MailDao curr = mailList.get(i);
            if(curr.getNeedSend()) {
                disableSameMessage(i, mailList);
            }
        }
        
        int count = 0;
        for(int i = 0; i < mailList.size(); ++i) {
            MailDao curr = mailList.get(i);
            if(curr.getNeedSend()) {
                sendRealLow(curr);
                ++count;
            } else {
                update(curr);
            }
        }
        return count;
    }
    


    private static void update(MailDao mailDao) throws Exception {
        Logger logger = LoggerFactory.getLogger(Mailer.class);
        Session session = HiberUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(mailDao);
            tx.commit();
            logger.info("update mail info.");
        } catch (Exception e) {
            if(tx != null) {
                tx.rollback();
            }
            throw e;
        } finally {
            session.close();
        }
    }
    private static void saveIntoDb(MailDao mailDao) throws Exception {
        Logger logger = LoggerFactory.getLogger(Mailer.class);
        Session session = HiberUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(mailDao);
            tx.commit();
            logger.info("insert mail info.");
        } catch (Exception e) {
            if(tx != null) {
                tx.rollback();
            }
            throw e;
        } finally {
            session.close();
        }
    }
    public static List<MailDao> selectByDate(String date) throws Exception {
        Session session = HiberUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            String startTime = date + " 00:00:00";
            String endTime = date + " 23:59:59";
            
            String hql  = "from MailDao M where M.updateTime > :startTime and M.updateTime <= :endTime order by M.mailId desc";
            Query query = session.createQuery(hql);
            query.setParameter("startTime", startTime);
            query.setParameter("endTime", endTime);
            
            List result = query.list();
            Iterator itRes = result.iterator();
            
            List<MailDao> resultList = (List<MailDao>)result;
            
            tx.commit();
            return resultList;
        } catch(Exception e) {
            if(tx != null) {
                tx.rollback();
            }
            throw e;
        } finally {
            session.close();
        }
    }
    private static List<MailDao> selectNotSent() throws Exception {
        Session session = HiberUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            String hql  = "from MailDao M where M.returnId is null order by M.mailId";
            Query query = session.createQuery(hql);
            
            List result = query.list();
            Iterator itRes = result.iterator();
            
            List<MailDao> resultList = (List<MailDao>)result;
            
            tx.commit();
            return resultList;
        } catch(Exception e) {
            if(tx != null) {
                tx.rollback();
            }
            throw e;
        } finally {
            session.close();
        }
    }
    private static List<MailRcptAliasDao> selectAlias(String alias) throws Exception {
        Session session = HiberUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            String hql  = "from MailRcptAliasDao M where M.alias = :alias order by rcpt_id";
            Query query = session.createQuery(hql);
            query.setParameter("alias", alias);
            List result = query.list();
            
            Iterator itRes = result.iterator();
            
            
            tx.commit();
            return (List<MailRcptAliasDao>)result;
        } catch(Exception e) {
            if(tx != null) {
                tx.rollback();
            }
            throw e;
        } finally {
            session.close();
        }
    }
}
