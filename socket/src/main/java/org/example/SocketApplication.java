package org.example;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SocketApplication {

    public static void main(String[] args) {
        new SpringApplication(SocketApplication.class).run(args);
    }

}
