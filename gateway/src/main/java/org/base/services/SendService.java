package org.base.services;

public interface SendService {

    void pushToTopic(String topic, Object request) throws Exception;

}
