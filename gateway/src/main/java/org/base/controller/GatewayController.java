package org.base.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.base.config.GatewayConfig;
import org.base.ultilities.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

@RestController
@RequestMapping(value = "/api")
@Slf4j
public class GatewayController {

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



    public ResponseEntity<String> processRequest(String requestMethod, Map<String, String> urlParamMap,
                                                 Map<String, Object> bodyParamMap, Map<String, String> headerParamMap,
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
                requestMethod, requestPath, urlParamMap, pathParam, bodyParamMap, headerParamMap);
        //log to db


        //Validate url
        /*String invalidData = new GatewayValidation().validate(requestPath, service, requestMethod);

        if (invalidData != null) {
            ResponseMessage responseMessage = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), invalidData, null);
            String result = responseMessage.toJsonString();
            return new ResponseEntity(result, HttpStatus.BAD_REQUEST);
        } else {
            requestPath = requestPath.contains(GatewayConfig.API_ROOT_PATH)
                    ? requestPath.replace(GatewayConfig.API_ROOT_PATH, "/") : requestPath;

            String result = null;

            //Get rabbit type from url
            String rabbitType = GatewayConfig.SERVICE_PATH_MAP.get(requestPath + "  " + requestMethod).getRabbit_type();
            RequestMessage request = new RequestMessage(requestMethod, requestPath,
                    urlParamMap, pathParam, bodyParamMap, headerParamMap);
            log.info("Get Rabbit type for {} {} ==> Rabbit: {}", requestMethod,
                    requestPath.replace(GatewayConfig.API_ROOT_PATH, "/"), rabbitType);
            if ("rpc".equalsIgnoreCase(rabbitType)) {
                String rpcQueue = GatewayConfig.SERVICE_MAP.get(service + ".rpc.queue");
                String rpcExchange = GatewayConfig.SERVICE_MAP.get(service + ".rpc.exchange");
                String rpcKey = GatewayConfig.SERVICE_MAP.get(service + ".rpc.key");
                if (StringUtil.isNullOrEmpty(rpcQueue) || StringUtil.isNullOrEmpty(rpcExchange) || StringUtil.isNullOrEmpty(rpcKey)) {
                    throw new RuntimeException("Không tìm thấy rabbit mq cho service " + service);
                }
                result = rabbitMQClient.callRpcService(rpcExchange, rpcQueue, rpcKey, request.toJsonString());

                log.info("result: " + result);
            } else if ("worker".equalsIgnoreCase(rabbitType)) {
                String workerQueue = GatewayConfig.SERVICE_MAP.get(service + ".worker.queue");
                if (StringUtil.isNullOrEmpty(workerQueue)) {
                    throw new RuntimeException("Không tìm thấy rabbit mq cho service " + service);
                }
                //Call worker
                if (rabbitMQClient.callWorkerService(workerQueue, request.toJsonString())) {
                    MessageContent mc = new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), "OK");
                    ResponseMessage responseMessage = new ResponseMessage(mc);
                    result = responseMessage.toJsonString();
                } else {
                    MessageContent mc = new MessageContent(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            HttpStatus.INTERNAL_SERVER_ERROR.toString(), null);
                    ResponseMessage responseMessage = new ResponseMessage(mc);
                    result = responseMessage.toJsonString();
                }
            } else if ("publish".equalsIgnoreCase(rabbitType)) {
                String directExchange = GatewayConfig.SERVICE_MAP.get(service + ".direct.exchange");
                String directKey = GatewayConfig.SERVICE_MAP.get(service + ".direct.key");
                if (StringUtil.isNullOrEmpty(directExchange) || StringUtil.isNullOrEmpty(directKey)) {
                    throw new RuntimeException("Không tìm thấy rabbit mq cho service " + service);
                }
                //Call publisher
                if (rabbitMQClient.callPublishService(directExchange, directKey, request.toJsonString())) {
                    MessageContent mc = new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), "OK");
                    ResponseMessage responseMessage = new ResponseMessage(HttpStatus.OK.value(), HttpStatus.OK.toString(), mc);
                    result = responseMessage.toJsonString();
                } else {
                    MessageContent mc = new MessageContent(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.toString(), null);
                    ResponseMessage responseMessage = new ResponseMessage(mc);
                    result = responseMessage.toJsonString();
                }
            }

            //return
            if (result != null) {
                ObjectMapper mapper = new ObjectMapper();
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                mapper.setDateFormat(df);
                ResponseMessage response = mapper.readValue(result, ResponseMessage.class);
                return new ResponseEntity(response.getData(), HttpStatus.valueOf(response.getStatus()));
            }
            ResponseMessage responseMessage = new ResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.toString(), null);
            result = responseMessage.toJsonString();
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);*/
            return null;
        }
    }
