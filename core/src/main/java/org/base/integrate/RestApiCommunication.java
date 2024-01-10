package org.base.integrate;

import lombok.extern.slf4j.Slf4j;
import org.base.exception.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.function.Function;

@Slf4j
@Component
public class RestApiCommunication<T> {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${kong.api-key}")
    private String apiKey;

    public ResponseEntity<T> exchangeJsonUrl(HttpMethod method,
                                                HttpHeaders headers,
                                                String url,
                                                String params,
                                             Class<T> responseType) throws SystemException {
        ResponseEntity<T> response = null;
        headers.add("apikey", apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        try {
            URI uri = new URI(url);
            HttpEntity<String> entity = new HttpEntity(params, headers);
            response = restTemplate.exchange(uri, method, entity, responseType);

            return response;
        } catch (Exception e) {
            e.printStackTrace();
            throw new SystemException("Call to " + url + " failed!!");
        }
    }
}
