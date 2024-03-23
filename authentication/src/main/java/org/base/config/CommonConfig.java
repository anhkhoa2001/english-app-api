package org.base.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CommonConfig {

    //mapping session id cua client va user id
    public static final Map<String, String> mapping = new HashMap<>();

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
