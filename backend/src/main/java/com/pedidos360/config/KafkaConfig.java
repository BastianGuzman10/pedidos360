package com.pedidos360.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import java.time.Duration;

@Configuration
public class KafkaConfig {

    @Bean
    NewTopic pedidosTopic(@Value("${app.kafka.pedidos-topic}") String name) {
        return TopicBuilder.name(name)
                .partitions(3)
                .replicas(1)
                .config("cleanup.policy", "delete")
                .config("retention.ms", String.valueOf(Duration.ofDays(7).toMillis()))
                .build();
    }

    @Bean
    NewTopic auditoriaTopic(@Value("${app.kafka.auditoria-topic}") String name) {
        return TopicBuilder.name(name)
                .partitions(3)
                .replicas(1)
                .config("cleanup.policy", "delete")
                .config("retention.ms", String.valueOf(Duration.ofDays(30).toMillis()))
                .build();
    }
}
