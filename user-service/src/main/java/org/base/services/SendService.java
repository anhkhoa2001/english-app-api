package org.base.services;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface SendService {

    void pushToTopic(String topic, Object request);

}
