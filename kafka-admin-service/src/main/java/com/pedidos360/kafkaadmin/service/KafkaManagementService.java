package com.pedidos360.kafkaadmin.service;

import com.pedidos360.kafkaadmin.model.TopicRequest;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.config.ConfigResource;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class KafkaManagementService {
    private final AdminClient adminClient;

    public KafkaManagementService(AdminClient adminClient) {
        this.adminClient = adminClient;
    }

    public void crear(TopicRequest request) throws Exception {
        NewTopic topic = new NewTopic(request.nombre(), request.particiones(), request.factorReplicacion())
                .configs(configuraciones(request));
        adminClient.createTopics(List.of(topic)).all().get();
    }

    public List<TopicInfo> listar() throws Exception {
        Set<String> nombres = adminClient.listTopics().names().get();
        Map<String, TopicDescription> descripciones = adminClient.describeTopics(nombres).allTopicNames().get();
        List<TopicInfo> resultado = new ArrayList<>();
        for (String nombre : new TreeSet<>(nombres)) {
            TopicDescription descripcion = descripciones.get(nombre);
            short replicas = descripcion.partitions().isEmpty() ? 0
                    : (short) descripcion.partitions().get(0).replicas().size();
            resultado.add(new TopicInfo(nombre, descripcion.partitions().size(), replicas));
        }
        return resultado;
    }

    public Map<String, String> configuracion(String nombre) throws Exception {
        ConfigResource recurso = new ConfigResource(ConfigResource.Type.TOPIC, nombre);
        Config config = adminClient.describeConfigs(List.of(recurso)).all().get().get(recurso);
        return config.entries().stream()
                .filter(entry -> !entry.isSensitive())
                .collect(Collectors.toMap(ConfigEntry::name, entry -> Objects.toString(entry.value(), ""),
                        (a, b) -> a, TreeMap::new));
    }

    public void actualizar(String nombre, TopicRequest request) throws Exception {
        if (!nombre.equals(request.nombre())) {
            throw new IllegalArgumentException("El nombre de la URL y del payload debe coincidir");
        }
        TopicDescription actual = adminClient.describeTopics(List.of(nombre)).allTopicNames().get().get(nombre);
        if (request.particiones() < actual.partitions().size()) {
            throw new IllegalArgumentException("Kafka no permite reducir el número de particiones");
        }
        if (request.particiones() > actual.partitions().size()) {
            adminClient.createPartitions(Map.of(nombre, NewPartitions.increaseTo(request.particiones()))).all().get();
        }
        ConfigResource recurso = new ConfigResource(ConfigResource.Type.TOPIC, nombre);
        List<AlterConfigOp> cambios = configuraciones(request).entrySet().stream()
                .map(entry -> new AlterConfigOp(new ConfigEntry(entry.getKey(), entry.getValue()),
                        AlterConfigOp.OpType.SET))
                .toList();
        adminClient.incrementalAlterConfigs(Map.of(recurso, cambios)).all().get();
    }

    public void eliminar(String nombre) throws Exception {
        adminClient.deleteTopics(List.of(nombre)).all().get();
    }

    public List<ConsumerGroupInfo> consumerGroups() throws Exception {
        Collection<ConsumerGroupListing> groups = adminClient.listConsumerGroups().all().get();
        return groups.stream()
                .map(group -> new ConsumerGroupInfo(group.groupId(), group.state().map(Enum::name).orElse("UNKNOWN")))
                .sorted(Comparator.comparing(ConsumerGroupInfo::groupId))
                .toList();
    }

    private Map<String, String> configuraciones(TopicRequest request) {
        return Map.of(
                "retention.ms", String.valueOf(request.retencionMs()),
                "cleanup.policy", request.cleanupPolicy(),
                "min.insync.replicas", request.factorReplicacion() > 1 ? "2" : "1"
        );
    }

    public record TopicInfo(String nombre, int particiones, short factorReplicacion) {}
    public record ConsumerGroupInfo(String groupId, String estado) {}
}
