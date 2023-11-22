package org.base;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class UserServiceApplication {
    public static void main(String[] args) {
        new SpringApplication(UserServiceApplication.class).run(args);
    }
}