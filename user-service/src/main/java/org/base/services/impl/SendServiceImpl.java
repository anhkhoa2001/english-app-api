package org.base.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.base.services.SendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SendServiceImpl implements SendService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper mapper;

    @Autowired
    public SendServiceImpl(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper mapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.mapper = mapper;
    }


    @Override
    public void pushToTopic(String topic, Object request) {
        try {
            String content = mapper.writeValueAsString(request);
            kafkaTemplate.send(topic, content);

            log.info("Send to topic {} with content {}", topic, content);
        } catch (Exception e) {
            kafkaTemplate.send(topic, e.getClass().toString());
        }
    }
}