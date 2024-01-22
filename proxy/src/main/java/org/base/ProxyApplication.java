package org.base;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProxyApplication {
    public static void main(String[] args) {
        new SpringApplication(ProxyApplication.class).run(args);
    }
}