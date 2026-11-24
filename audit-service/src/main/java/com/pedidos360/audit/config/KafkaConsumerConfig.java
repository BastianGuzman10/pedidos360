package com.pedidos360.audit.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class KafkaConsumerConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumerConfig.class);

    @Bean
    CommonErrorHandler kafkaErrorHandler() {
        DefaultErrorHandler handler = new DefaultErrorHandler(
                (record, error) -> LOGGER.error("Auditoría descartó evento en {}-{} offset {}",
                        record.topic(), record.partition(), record.offset(), error),
                new FixedBackOff(1_000L, 3L));
        handler.addNotRetryableExceptions(IllegalArgumentException.class);
        return handler;
    }
}
