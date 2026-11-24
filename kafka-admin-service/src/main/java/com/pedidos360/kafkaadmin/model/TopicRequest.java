package com.pedidos360.kafkaadmin.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record TopicRequest(
        @NotBlank String nombre,
        @Min(1) int particiones,
        @Min(1) short factorReplicacion,
        @Min(60_000) long retencionMs,
        @Pattern(regexp = "delete|compact|compact,delete|delete,compact") String cleanupPolicy
) {
}
