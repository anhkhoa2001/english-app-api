package org.base.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.base.config.GatewayConfig;
import org.base.dto.ApiDTO;
import org.base.dto.TopicMapper;
import org.base.dto.common.MessageRequestDTO;
import org.base.exception.SystemException;
import org.base.ultilities.Constants;
import org.base.ultilities.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping(value = "/api")
@Slf4j
public class GatewayController {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    //GET
    @RequestMapping(value = "**", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getMethod(@RequestParam Map<String, String> reqParam,
                                            @RequestHeader Map<String, String> headers,
                                            HttpServletRequest req) throws JsonProcessingException {
        return processRequest("GET", reqParam, null, headers, req);
    }

    //POST
    @RequestMapping(value = "**", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> postMethod(@RequestParam Map<String, String> reqParam,
                                             @RequestBody(required = false) Map<String, Object> requestBody,
                                             @RequestHeader Map<String, String> headers,
                                             HttpServletRequest req) throws JsonProcessingException {
        return processRequest("POST", reqParam, requestBody, headers, req);
    }

    //PUT
    @RequestMapping(value = "**", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> putMethod(@RequestParam Map<String, String> reqParam,
                                            @RequestBody(required = false) Map<String, Object> requestBody,
                                            @RequestHeader Map<String, String> headers,
                                            HttpServletRequest req) throws JsonProcessingException {
        return processRequest("PUT", reqParam, requestBody, headers, req);
    }

    //PATCH
    @RequestMapping(value = "**", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> patchMethod(@RequestParam Map<String, String> reqParam,
                                              @RequestBody(required = false) Map<String, Object> requestBody,
                                              @RequestHeader Map<String, String> headers,
                                              HttpServletRequest req) throws JsonProcessingException {
        return processRequest("PATCH", reqParam, requestBody, headers, req);
    }

    //DELETE
    @RequestMapping(value = "**", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteMethod(@RequestParam Map<String, String> reqParam,
                                               @RequestBody(required = false) Map<String, Object> requestBody,
                                               @RequestHeader Map<String, String> headers,
                                               HttpServletRequest req) throws JsonProcessingException {
        return processRequest("DELETE", reqParam, requestBody, headers, req);
    }


    public ResponseEntity<String> processRequest(String requestMethod, Map<String, String> urlParams,
                                                 Map<String, Object> body, Map<String, String> header,
                                                 HttpServletRequest req) throws JsonProcessingException {
        //Get all value
        String requestPath = req.getRequestURI();
        String pathParam = null;

        //get service from url
        int index = requestPath.indexOf("/", GatewayConfig.API_ROOT_PATH.length());
        String service = null;
        if (index != -1) {
            service = requestPath.substring(GatewayConfig.API_ROOT_PATH.length(), index);
        } else {
            service = requestPath.replace(GatewayConfig.API_ROOT_PATH, "");
        }

        //Check has path param
        int lastIndex = requestPath.lastIndexOf("/");
        if (lastIndex != -1) {
            String lastStr = requestPath.substring(lastIndex + 1);
            if (StringUtil.isNumberic(lastStr) || StringUtil.isUUID(lastStr)) {
                requestPath = requestPath.substring(0, lastIndex);
                pathParam = lastStr;
            }
        }

        //Log request info
        log.info("[{}] to requestPath: {} - urlParam: {} - pathParm: {} - bodyParam: {} - headerParam: {}",
                requestMethod, requestPath, urlParams, pathParam, body, header);
        //log to db


        //Validate url
        String message = GatewayConfig.validate(requestPath, service, requestMethod);

        if (message != null) {
            throw new SystemException(message);
        } else {
            requestPath = requestPath.contains(GatewayConfig.API_ROOT_PATH)
                    ? requestPath.replace(GatewayConfig.API_ROOT_PATH, "/") : requestPath;

            String result = null;

            //Get rabbit type from url
            ApiDTO api = GatewayConfig.MAPPING_SERVICE_PATH.getOrDefault(requestPath + Constants.TEMPLE_SPLIT + requestMethod, null);
            if(StringUtil.isObject(api)) {
                MessageRequestDTO request = new MessageRequestDTO(requestMethod, requestPath,
                        urlParams, pathParam, body, header);
                TopicMapper topicMapper = GatewayConfig.MAPPING_SERVICE_TOPIC.get(api.getService());

                if(StringUtil.isObject(topicMapper)) {
                    sendToKafka(topicMapper.getTo(), request);
                } else {
                    throw new SystemException("TopicMapper invalid");
                }
            }
            //return
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void sendToKafka(String topic, MessageRequestDTO request) {
        kafkaTemplate.send(topic, request);

        log.info("Send to topic |{}| kafka {} ==>", topic,  request);
    }
}