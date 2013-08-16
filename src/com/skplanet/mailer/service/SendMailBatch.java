package com.skplanet.mailer.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skplanet.cask.container.ServiceRuntimeInfo;
import com.skplanet.cask.container.model.SimpleParams;
import com.skplanet.cask.container.service.SimpleService;
import com.skplanet.cask.util.StringUtil;
import com.skplanet.mailer.mail.Mailer;

public class SendMailBatch implements SimpleService {
    private Logger logger = LoggerFactory.getLogger(SendMailBatch.class);
    
    private static final int SLEEP_DEFAULT_MSEC = 30000;
    private boolean stop = false;
    
    @Override
    public void handle(SimpleParams request, SimpleParams response, ServiceRuntimeInfo runtimeInfo) throws Exception {
    
        logger.info("send mail batch start : {} ", request.getParams());
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            Integer sleepMSec = (Integer)request.get("sleepMSec");
            if(sleepMSec == null) {
                sleepMSec = SLEEP_DEFAULT_MSEC;
            }
            
            while(true) {
                try {
                    int count = Mailer.getInstance().sendRealNotSent();
                    
                    logger.info("complete to send mail. count : {}", count);
                    
                }
                catch(Exception e) {
                    logger.error(StringUtil.exception2Str(e));
                }
                
                logger.info("batch service sleep {} msec", sleepMSec);
                Thread.sleep(sleepMSec);
                
                if(stop) {
                    break;
                }
            }
            
            resultMap.put("returnCode",  true);
            resultMap.put("returnDesc",  "success");
            
        } catch(Exception e) {
            resultMap.put("returnCode",  false);
            resultMap.put("returnDesc",  StringUtil.exception2Str(e));
        } finally {
            response.setParams(resultMap);
            logger.info("send mail batch complete. : {} ", response.getParams());
        }
    }
}
