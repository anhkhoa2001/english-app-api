
package org.base.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class GatewayController {

    /*@Autowired
    private RestApiCommunication restApiCommunication;

    @Value("${kong.gateway.port}")
    private Long port;

    @Value("${kong.gateway.host}")
    private String host;

    @Autowired
    private JwtTokenSetup jwtTokenSetup;

    @PostMapping(value = "**", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity post(@RequestParam Map<String, String> reqParam,
                               @RequestBody(required = false) Object requestBody,
                               @RequestHeader Map<String, String> headers,
                               HttpServletRequest req) {
        return processRequest(HttpMethod.POST, reqParam, requestBody, headers, req);
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping(value = "**", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity get(@RequestParam Map<String, String> reqParam,
                               @RequestHeader Map<String, String> headers,
                               HttpServletRequest req) {
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
            if(!headerParamMap.isEmpty()) {
                headerParamMap.forEach((k, v) -> {
                    headers.put(k, Collections.singletonList(v));

                    if(k.equals("authorization")) {
                        String token = v.replaceAll("Bearer ", "");

                        Claims claims = jwtTokenSetup.getClaimsFromToken(token);
                        urlParamMap.put("username", claims.get("username").toString());
                    }
                });
            }

            if(!urlParamMap.isEmpty()) {
                urlBuilder.append("?").append(StringUtil.convertToQueryString(urlParamMap));
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
    }*/
}
