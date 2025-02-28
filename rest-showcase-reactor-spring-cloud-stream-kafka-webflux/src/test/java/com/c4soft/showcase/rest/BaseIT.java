package com.c4soft.showcase.rest;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;
import org.wiremock.spring.EnableWireMock;
import utils.ConfiguredTestContainers;
import utils.TestcontainersConfiguration;

@EnableWireMock
@Testcontainers(parallel = true)
@Import(TestcontainersConfiguration.class)
@SpringBootTest(classes = WorkerApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public abstract class BaseIT {

    @Container
    protected static final KafkaContainer kafkaContainer = ConfiguredTestContainers.createKafkaContainer();

    protected static void setupProperties(DynamicPropertyRegistry registry) {
        ConfiguredTestContainers.configureKafkaContainer(registry, kafkaContainer);
    }

}
