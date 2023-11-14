package org.base;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("org.base.*")
public class GatewayApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(GatewayApplication.class, args);
    }
}





//gateway : 9000
//user: 9001