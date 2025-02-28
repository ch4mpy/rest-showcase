package com.c4soft.showcase.rest.application.service.impl;

import com.c4soft.showcase.rest.application.model.TaskEventDTO;
import com.c4soft.showcase.rest.application.service.TaskHandlerApplicationService;
import com.c4soft.showcase.rest.domain.model.Task;
import com.c4soft.showcase.rest.domain.service.ChronosTaskService;
import com.c4soft.showcase.rest.domain.service.TaskHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskHandlerApplicationServiceImpl implements TaskHandlerApplicationService {

    private final TaskHandler taskHandler;

    private final ChronosTaskService chronosTaskService;

    @Override
    public Flux<Void> executeTask(Flux<Message<TaskEventDTO>> taskEventMessage) {
        return taskEventMessage.map(Message::getPayload).map(taskEventDTO -> new Task(taskEventDTO.id())).flatMap(
                task -> chronosTaskService.startTask(task.id())
                        .doOnSuccess(unused -> log.info("Task id={} successfully started", task.id()))
                        .then(Mono.defer(() -> taskHandler.handle(task)))
                        .then(Mono.defer(() -> chronosTaskService.finishTask(task.id()))));
    }
}
