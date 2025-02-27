package com.c4soft.showcase.rest.domain.service.impl;

import com.c4soft.showcase.rest.domain.model.Task;
import com.c4soft.showcase.rest.domain.service.TaskHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskHandlerImpl implements TaskHandler {

	@Override
	public Mono<Void> handle(Task task) {
		var taskId = task.id();

		return Mono.just("Hello from Worker task handler taskId=" + taskId).doOnSuccess(log::info).then();
	}
}
