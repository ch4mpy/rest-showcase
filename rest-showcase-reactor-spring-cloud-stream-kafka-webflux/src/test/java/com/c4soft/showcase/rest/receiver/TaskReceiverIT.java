package com.c4soft.showcase.rest.receiver;

import com.c4soft.showcase.rest.BaseIT;
import com.c4soft.showcase.rest.application.model.TaskEventDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import generated.com.c4soft.showcase.rest.infrastructure.rest.chronos.model.ChronosCloseTaskRequestDTO;
import generated.com.c4soft.showcase.rest.infrastructure.rest.chronos.model.ChronosCloseTaskRequestDTO.StatusEnum;
import integration.utils.wiremock.chronos.TaskManagerChronosMockServer;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamOperations;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.Duration;
import java.util.stream.Stream;


class TaskReceiverIT extends BaseIT {

    private static final String CHRONOS_TASK_BASE_ENDPOINT = "/api/task";
    private static final String CHRONOS_SELECT_TASK_ENDPOINT = CHRONOS_TASK_BASE_ENDPOINT + "/{id}";
    private static final String CHRONOS_CLOSE_TASK_ENDPOINT = CHRONOS_SELECT_TASK_ENDPOINT + "/close";
    private static final String CHRONOS_START_TASK_ENDPOINT = CHRONOS_SELECT_TASK_ENDPOINT + "/start";
    @Autowired
    private StreamOperations inputDestination;
    @Autowired
    private ObjectMapper objectMapper;
    @Value("${worker.queue.main}")
    private String executeTaskFunctionQueue;

    @DynamicPropertySource
    private static void setup(DynamicPropertyRegistry registry) {
        setupProperties(registry);
    }

    private static Stream<TaskEventDTO> executeTaskIntegrationTest() {
        return Stream.of(
                TaskEventDTO.builder()
                        .id(1L)
                        .build()
        );
    }

    @BeforeEach
    void setup() {
        WireMock.reset();
    }

    @AfterEach
    void cleanup() {
        WireMock.reset();
    }

    @MethodSource
    @ParameterizedTest
    void executeTaskIntegrationTest(TaskEventDTO taskEventDTO) throws JsonProcessingException {
        // given
        var message = MessageBuilder.withPayload(taskEventDTO).build();
        // and
        WireMock.stubFor(TaskManagerChronosMockServer.stubStartTask204(taskEventDTO.id().toString()));
        // and
        var closeTaskBody = new ChronosCloseTaskRequestDTO().status(StatusEnum.SUCCESSFUL);
        WireMock.stubFor(TaskManagerChronosMockServer.stubCloseTask204(
                taskEventDTO.id().toString(),
                objectMapper.writeValueAsString(closeTaskBody)
        ));

        // when
        inputDestination.send(executeTaskFunctionQueue, message);

        // then
        URI startTaskUri = UriComponentsBuilder.fromUriString(CHRONOS_START_TASK_ENDPOINT)
                .build(taskEventDTO.id());
        Awaitility.await()
                .atMost(Duration.ofSeconds(5))
                .untilAsserted(() -> WireMock.verify(WireMock.patchRequestedFor(WireMock.urlEqualTo(startTaskUri.toString()))));
        // and
        URI closeTaskUri = UriComponentsBuilder.fromUriString(CHRONOS_CLOSE_TASK_ENDPOINT)
                .build(taskEventDTO.id());
        Awaitility.await()
                .atMost(Duration.ofSeconds(5))
                .untilAsserted(() -> WireMock.verify(WireMock.patchRequestedFor(WireMock.urlEqualTo(closeTaskUri.toString()))));
    }
}
