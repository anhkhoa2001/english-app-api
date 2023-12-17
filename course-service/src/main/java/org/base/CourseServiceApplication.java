package org.base;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CourseServiceApplication {
    public static void main(String[] args) {
        new SpringApplication(CourseServiceApplication.class).run(args);
    }
}