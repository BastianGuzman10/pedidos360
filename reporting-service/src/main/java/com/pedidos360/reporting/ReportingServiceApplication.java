package com.pedidos360.reporting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@SpringBootApplication
public class ReportingServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReportingServiceApplication.class, args);
    }
}
