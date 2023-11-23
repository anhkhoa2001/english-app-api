package org.base.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.base.dto.common.MessageRequestDTO;
import org.base.exception.SystemException;
import org.base.utils.Constants;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserServiceListener {

    @KafkaListener(topics = Constants.SERVICE.USER.FROM, groupId = "english-socket")
    public void onListener(String payload, @Header(KafkaHeaders.RECEIVED_TIMESTAMP) Long ts, Acknowledgment ack) {
        log.info("Content payload {}", payload);
    }
}
