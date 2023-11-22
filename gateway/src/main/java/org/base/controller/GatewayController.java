package org.base.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.base.config.EnableWrapResponse;
import org.base.config.GatewayConfig;
import org.base.dto.ApiDTO;
import org.base.dto.TopicMapper;
import org.base.dto.common.MessageRequestDTO;
import org.base.exception.SystemException;
import org.base.utils.Constants;
import org.base.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api")
@Slf4j
@EnableWrapResponse
public class GatewayController {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final CountDownLatch latch = new CountDownLatch(1);
    private final ConsumerFactory consumerFactory;

    @Autowired
    public GatewayController(KafkaTemplate<String, String> kafkaTemplate,
                             ConsumerFactory consumerFactory) {
        this.kafkaTemplate = kafkaTemplate;
        this.consumerFactory = consumerFactory;
    }

    //GET
    @RequestMapping(value = "**", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getMethod(@RequestParam Map<String, String> reqParam,
                                            @RequestHeader Map<String, String> headers,
                                            HttpServletRequest req) throws Exception {
        return processRequest("GET", reqParam, null, headers, req);
    }

    //POST
    @RequestMapping(value = "**", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity postMethod(@RequestParam Map<String, String> reqParam,
                                             @RequestBody(required = false) Map<String, Object> requestBody,
                                             @RequestHeader Map<String, String> headers,
                                             HttpServletRequest req) {
        return processRequest("POST", reqParam, requestBody, headers, req);
    }

    //PUT
    @RequestMapping(value = "**", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity putMethod(@RequestParam Map<String, String> reqParam,
                                            @RequestBody(required = false) Map<String, Object> requestBody,
                                            @RequestHeader Map<String, String> headers,
                                            HttpServletRequest req) throws Exception {
        return processRequest("PUT", reqParam, requestBody, headers, req);
    }

    //PATCH
    @RequestMapping(value = "**", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> patchMethod(@RequestParam Map<String, String> reqParam,
                                              @RequestBody(required = false) Map<String, Object> requestBody,
                                              @RequestHeader Map<String, String> headers,
                                              HttpServletRequest req) throws Exception {
        return processRequest("PATCH", reqParam, requestBody, headers, req);
    }

    //DELETE
    @RequestMapping(value = "**", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteMethod(@RequestParam Map<String, String> reqParam,
                                               @RequestBody(required = false) Map<String, Object> requestBody,
                                               @RequestHeader Map<String, String> headers,
                                               HttpServletRequest req) throws Exception {
        return processRequest("DELETE", reqParam, requestBody, headers, req);
    }


    public ResponseEntity processRequest(String requestMethod, Map<String, String> urlParams,
                                                 Map<String, Object> body, Map<String, String> header,
                                                 HttpServletRequest req) {
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
                TopicMapper topicMapper = GatewayConfig.MAPPING_SERVICE_TOPIC.get(api.getTopic());

                try {
                    if(StringUtil.isObject(topicMapper)) {
                        //
                        kafkaTemplate.send(topicMapper.getTo(), new ObjectMapper().writeValueAsString(request));
                        // Đợi response từ topic 'from'
                        Consumer<String, String> consumer = consumerFactory.createConsumer();
                        consumer.subscribe(Collections.singletonList(topicMapper.getFrom()));

                        new Thread(() -> {
                            try {
                                while (true) {
                                    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                                    records.forEach(record -> {
                                        log.info("Receive response {}", record);
                                        latch.countDown();
                                    });
                                }
                            } finally {
                                consumer.close();
                            }
                        }).start();

                        return ResponseEntity.ok("Receive response from topic "+ topicMapper.getFrom() +" timeout!!!");
                    }
                } catch (Exception e) {
                    throw new SystemException(500, e.getMessage());
                }
            }
            //return
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
    }
}