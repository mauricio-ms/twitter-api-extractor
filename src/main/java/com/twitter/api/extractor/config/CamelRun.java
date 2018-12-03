package com.twitter.api.extractor.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CamelRun {

    public static void main(final String[] args) {
        SpringApplication.run(CamelConfig.class, args);
    }
}