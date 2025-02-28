package com.c4soft.showcase.rest.domain.service;

import com.c4soft.showcase.rest.domain.model.Task;
import reactor.core.publisher.Mono;

public interface TaskHandler {

    Mono<Void> handle(Task task);

}
