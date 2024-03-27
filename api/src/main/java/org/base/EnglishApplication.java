package org.base;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"org.base"})
public class EnglishApplication {
    public static void main(String[] args) {
        new SpringApplication(EnglishApplication.class).run(args);
    }
}