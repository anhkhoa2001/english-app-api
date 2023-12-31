package org.base.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.base.config.JwtTokenSetup;
import org.base.controller.UserController;
import org.base.dto.common.MessageRequestDTO;
import org.base.dto.common.MessageResponseDTO;
import org.base.dto.common.ModuleErrorResponseDTO;
import org.base.exception.*;
import org.base.services.SendService;
import org.base.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class UserListener {

    private final SendService sendService;
    private final UserController userController;
    private final JwtTokenSetup jwtTokenSetup;

    @Autowired
    public UserListener(SendService sendService, UserController userController, JwtTokenSetup jwtTokenSetup) {
        this.sendService = sendService;
        this.userController = userController;
        this.jwtTokenSetup = jwtTokenSetup;
    }


    @KafkaListener(topics = Constants.SERVICE.USER.TO, groupId = "english-socket")
    public void onListener(String payload, @Header(KafkaHeaders.RECEIVED_TIMESTAMP) Long ts, Acknowledgment ack) {
        log.info("Content payload {}", payload);

        try {
            MessageRequestDTO request = new ObjectMapper().readValue(payload, MessageRequestDTO.class);
            //check authentication
            if(request.isAuth()) {
                if(!userController.checkToken(request.getHeaderParam())) {
                    sendService.pushToTopic(Constants.SERVICE.USER.FROM, new UnauthorizationException().getMessage());
                    return;
                }
            }

            Object response = null;
            switch (request.getRequestMethod()) {
                case "POST":
                    if(request.getRequestPath().equalsIgnoreCase("/user/user-info/generate-token")) {
                        response = userController.generateToken(request.getBodyParam());
                        sendService.pushToTopic(Constants.SERVICE.USER.FROM, response);
                    }
                    break;
                case "GET":
                    if(request.getRequestPath().equalsIgnoreCase("/user/user-info/get-all")) {
                        response = userController.getAllUser();
                        sendService.pushToTopic(Constants.SERVICE.USER.FROM, response);
                    }
                    break;
            }
        } catch (UnauthorizationException ue) {
            sendService.pushToTopic(Constants.SERVICE.USER.FROM, new ModuleErrorResponseDTO(ue.getMessage(), 401));
        } catch (ValidationException ve) {
            sendService.pushToTopic(Constants.SERVICE.USER.FROM, new ModuleErrorResponseDTO(ve.getMessage(), 404));
        } catch (SystemException se) {
            sendService.pushToTopic(Constants.SERVICE.USER.FROM, new ModuleErrorResponseDTO(se.getMessage(), 500));
        }  catch (JsonProcessingException je) {
            sendService.pushToTopic(Constants.SERVICE.USER.FROM, new ModuleErrorResponseDTO("Parse json format MessageRequestDTO failed", 500));
        } finally {
            ack.acknowledge();
        }
    }
}
