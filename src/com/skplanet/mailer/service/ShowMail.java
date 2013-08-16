package com.skplanet.mailer.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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

public class ShowMail implements SimpleService {
    private Logger logger = LoggerFactory.getLogger(ShowMail.class);
    

    @Override
    public void handle(SimpleParams request, SimpleParams response, ServiceRuntimeInfo runtimeInfo) throws Exception {
        
        logger.info("showmail service start : {} ", request.getParams());
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
           
            String date = request.getString("date");
            if(date == null) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                date = dateFormat.format(new Date());
            }
            List<MailDao> resultList = Mailer.selectByDate(date);
            
            resultMap.put("returnCode",  1);
            resultMap.put("returnDesc",  "success");
            resultMap.put("results",  resultList);
            
            
        } catch(Exception e) {
            logger.error("{}",  StringUtil.exception2Str(e));
            resultMap.put("returnCode",  0);
            resultMap.put("returnDesc",  "fail");
            resultMap.put("msg",  e.getMessage());
        } finally {
            response.setParams(resultMap);

            logger.info("showmail service complete : {} ", response.getParams());
        }
    }
    
    
    
    
}
