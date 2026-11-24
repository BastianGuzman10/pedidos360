package com.pedidos360.reporting.config;

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
                (record, error) -> LOGGER.error(
                        "Evento descartado tras reintentos. topic={}, partition={}, offset={}",
                        record.topic(), record.partition(), record.offset(), error),
                new FixedBackOff(1_000L, 3L));
        handler.addNotRetryableExceptions(IllegalArgumentException.class);
        handler.setRetryListeners((record, error, attempt) -> LOGGER.warn(
                "Reintento {} para evento Kafka en {}-{}", attempt,
                record.topic(), record.partition(), error));
        return handler;
    }
}
