package org.base.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.base.controller.UserController;
import org.base.dto.common.MessageRequestDTO;
import org.base.exception.SystemException;
import org.base.exception.ValidationException;
import org.base.services.SendService;
import org.base.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserListener {

    private final SendService sendService;
    private final UserController userController;

    @Autowired
    public UserListener(SendService sendService, UserController userController) {
        this.sendService = sendService;
        this.userController = userController;
    }


    @KafkaListener(topics = Constants.SERVICE.USER.TO, groupId = "english-socket")
    public void onListener(String payload, @Header(KafkaHeaders.RECEIVED_TIMESTAMP) Long ts, Acknowledgment ack) {
        log.info("Content payload {}", payload);

        try {
            MessageRequestDTO request = new ObjectMapper().readValue(payload, MessageRequestDTO.class);
            Object response = null;
            switch (request.getRequestMethod()) {
                case "POST":
                    if(request.getRequestPath().equalsIgnoreCase("/user/user-info/generate-token")) {
                        response = userController.generateToken(request.getBodyParam());
                        sendService.pushToTopic(Constants.SERVICE.USER.FROM, response);
                    }
                    break;
            }
        } catch (IllegalArgumentException ie) {
            ie.printStackTrace();
            log.info("Parse json format MessageRequestDTO failed!! ---> {}", payload);
            throw new SystemException("Parse json format MessageRequestDTO failed");
        } catch (JsonProcessingException je) {
            log.info("Build json response failed!!");
            throw new SystemException("Build json response failed!!");
        } finally {
            ack.acknowledge();
        }
    }
}
