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
import com.skplanet.mailer.mail.Mailer;
import com.skplanet.mailer.util.CommonUtils;


public class SendMailPost implements SimpleService {
    private Logger logger = LoggerFactory.getLogger(SendMailPost.class);
    

    @Override
    public void handle(SimpleParams request, SimpleParams response, ServiceRuntimeInfo runtimeInfo) throws Exception {
        
        logger.info("sendmailPost service start : {} ", request.getParams());
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
           
            String returnId = SendMail.sendMail(request, response, runtimeInfo);
            
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

            logger.info("sendmailPost service complete : {} ", response.getParams());
        }
    }
    
    
    
    
}
