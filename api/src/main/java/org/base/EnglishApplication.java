package org.base;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class EnglishApplication {

    public static void main(String[] args) {
        new SpringApplication(EnglishApplication.class).run(args);
    }

}