package com.skplanet.mailer.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skplanet.cask.container.ServiceRuntimeInfo;
import com.skplanet.cask.container.config.ConfigReader;
import com.skplanet.cask.container.model.SimpleParams;
import com.skplanet.cask.container.service.SimpleService;
import com.skplanet.cask.util.StringUtil;
import com.skplanet.mailer.db.MailDao;
import com.skplanet.mailer.mail.Mailer;
import com.skplanet.mailer.util.CommonUtils;

// from=lonslonz@sk.com&to=lonslonz@sk.com;lonslonz@daum.net&subject=test&msg=my body test
// from : lonslonz@sk.com
// to : [lonslonz@sk.com, lonslonz@daum.net],
// subject : test 
// msg : my body test
public class SendMail implements SimpleService {
    private Logger logger = LoggerFactory.getLogger(SendMail.class);
    
    @Override
    public void handle(SimpleParams request, SimpleParams response, ServiceRuntimeInfo runtimeInfo) throws Exception {
        
        logger.info("sendmail service start : {} ", request.getParams());
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
           
            String returnId = sendMail(request, response, runtimeInfo);
            
            resultMap.put("returnCode",  1);
            resultMap.put("returnDesc",  "success");
            resultMap.put("returnId",  returnId);
            
        } catch(Exception e) {
            logger.error("{}",  StringUtil.exception2Str(e));
            resultMap.put("returnCode",  0);
            resultMap.put("returnDesc",  "fail");
            resultMap.put("msg",  e.getMessage());
        } finally {
            response.setParams(resultMap);
            logger.info("sendmail service complete : {} ", response.getParams());
        }
    }
    
    
    public static String sendMail(SimpleParams request, SimpleParams response, ServiceRuntimeInfo runtimeInfo) throws Exception {
        CommonUtils.validate(request.getString("from"), "from");
        CommonUtils.validate(request.getString("to"), "to");
        CommonUtils.validate(request.getString("subject"), "subject");
        CommonUtils.validate(request.getString("msg"), "msg");
        
        String smtpServer = request.getString("smtpServer"); 
        if(smtpServer == null) {
            smtpServer = ConfigReader.getInstance().getServerConfig().getPropValue("smtpServer");
        }
        
        String smtpPort = request.getString("smtpPort");
        if(smtpPort == null) {
            smtpPort = ConfigReader.getInstance().getServerConfig().getPropValue("smtpPort");
        }
        String id = request.getString("id");
        if(id == null) {
            id = ConfigReader.getInstance().getServerConfig().getPropValue("id");
        }
        String password = request.getString("password");
        if(password == null) {
            password = ConfigReader.getInstance().getServerConfig().getPropValue("password");
        }
        String ssl = request.getString("ssl");
        if(ssl == null) {
            ssl = ConfigReader.getInstance().getServerConfig().getPropValue("ssl");
        }
//        String returnId = Mailer.getInstance().sendMail(request.getString("from"),
//                                    request.getString("to"), 
//                                    request.getString("subject"),
//                                    request.getString("msg"),
//                                    smtpServer,
//                                    smtpPort,
//                                    id,
//                                    password,
//                                    ssl);
        
        Mailer.getInstance().send(request.getString("from"),
                request.getString("to"), 
                request.getString("subject"),
                request.getString("msg"),
                smtpServer,
                smtpPort,
                id,
                password,
                ssl);
        
       
        
        return "test";
    }
    
}
