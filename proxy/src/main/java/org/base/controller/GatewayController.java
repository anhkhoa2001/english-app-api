package org.base.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.base.config.EnableWrapResponse;
import org.base.dto.common.MessageResponseDTO;
import org.base.exception.SystemException;
import org.base.integrate.RestApiCommunication;
import org.base.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/api")
public class GatewayController {

    @Autowired
    private RestApiCommunication restApiCommunication;

    @Value("${kong.gateway.port}")
    private Long port;

    @Value("${kong.gateway.host}")
    private String host;

    @PostMapping(value = "**", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity post(@RequestParam Map<String, String> reqParam,
                               @RequestBody(required = false) Object requestBody,
                               @RequestHeader Map<String, String> headers,
                               HttpServletRequest req) {
        return processRequest(HttpMethod.POST, reqParam, requestBody, headers, req);
    }

    @GetMapping(value = "**", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity get(@RequestParam Map<String, String> reqParam,
                               @RequestHeader Map<String, String> headers,
                               HttpServletRequest req) throws JsonProcessingException {
        return processRequest(HttpMethod.GET, reqParam, null, headers, req);
    }

    private ResponseEntity processRequest(HttpMethod method,
                                          Map<String, String> urlParamMap,
                                          Object bodyParamMap,
                                          Map<String, String> headerParamMap,
                                          HttpServletRequest req) {
        StringBuilder urlBuilder = new StringBuilder(host).append(":").append(port).append(req.getRequestURI());
        String body = null;
        HttpHeaders headers = new HttpHeaders();

        try {
            if(!urlParamMap.isEmpty()) {
                urlBuilder.append(StringUtil.convertToQueryString(urlParamMap));
            }

            if(!headerParamMap.isEmpty()) {
                headerParamMap.forEach((k, v) -> {
                    headers.put(k, Collections.singletonList(v));
                });
            }

            if(StringUtil.isObject(bodyParamMap)) {
                body = new ObjectMapper().writeValueAsString(bodyParamMap);
            }

            ResponseEntity<MessageResponseDTO> res = restApiCommunication.exchangeJsonUrl(method,
                    new HttpHeaders(), urlBuilder.toString(), body, MessageResponseDTO.class);


            return res;
        } catch (JsonProcessingException je) {
            log.error("Build json body string error {}", bodyParamMap);
            throw new SystemException("Build json body string error");
        }
    }
}
