package org.base.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InitialCollection {

    //mapping session id cua client va user id
    public static final Map<String, String> mapping = new HashMap<>();

}
