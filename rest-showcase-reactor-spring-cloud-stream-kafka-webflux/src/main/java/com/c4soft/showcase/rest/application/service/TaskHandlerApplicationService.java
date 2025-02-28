package com.c4soft.showcase.rest.application.service;

import com.c4soft.showcase.rest.application.model.TaskEventDTO;
import org.springframework.messaging.Message;
import reactor.core.publisher.Flux;

public interface TaskHandlerApplicationService {

    Flux<Void> executeTask(Flux<Message<TaskEventDTO>> taskEventMessage);

}
