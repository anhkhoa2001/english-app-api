package org.base.thread;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;

import java.util.Map;

/**
 * com.fis.fw.common.thread.MDCLogTaskDecorator
 * Author TungHuynh - sondt18@fpt.com.vn
 * Date 18/11/2021 13:19
 */
@Slf4j
public class MDCLogTaskDecorator implements TaskDecorator {

    public static String getRequestPair(){
        try{
            return MDC.get("request-uuid");
        }catch (Exception ex){
            log.error(ex.getMessage(), ex);
            return "";
        }
    }

    @Override
    public Runnable decorate(Runnable runnable) {
        Map<String, String> contextMap = MDC.getCopyOfContextMap();
        return () -> {
            try {
                if (contextMap!=null) {
                    MDC.setContextMap(contextMap);
                }
                runnable.run();
            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
            } finally {
                MDC.clear();
            }
        };
    }
}
