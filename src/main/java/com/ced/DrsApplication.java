package com.ced;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class DrsApplication {

    public static void main(String[] args) {
        SpringApplication.run(DrsApplication.class, args);
    }

}
