package org.base.listener;

import lombok.extern.slf4j.Slf4j;
import org.base.services.SendService;
import org.base.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "kafka.noti.event.listener.enabled", havingValue = "true")
@Slf4j
public class UserListener {

    private final SendService sendServicel;

    @Autowired
    public UserListener(SendService sendServicel) {
        this.sendServicel = sendServicel;
    }


    @KafkaListener(topics = Constants.SERVICE.USER.TO)
    public void onNotificationSMS(String payload, @Header(KafkaHeaders.RECEIVED_TIMESTAMP) Long ts, Acknowledgment ack) {

        log.info("Content payload {}", payload);
    }
}
