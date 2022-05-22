package com.hunglp.iambackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class IamBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(IamBackendApplication.class, args);
    }

}
