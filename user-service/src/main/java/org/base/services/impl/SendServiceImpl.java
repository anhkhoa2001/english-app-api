package org.base.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.base.services.SendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class SendServiceImpl implements SendService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper mapper;

    @Autowired
    public SendServiceImpl(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper mapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.mapper = mapper;
    }


    @Override
    public void pushToTopic(String topic, Object request) throws JsonProcessingException {
        String content = mapper.writeValueAsString(request);
        kafkaTemplate.send(topic, content);
    }
}