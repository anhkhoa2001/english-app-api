package org.base.config;

import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigPort implements WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {

    public void customize(ConfigurableServletWebServerFactory factory){
        factory.setPort(9998);
    }

}