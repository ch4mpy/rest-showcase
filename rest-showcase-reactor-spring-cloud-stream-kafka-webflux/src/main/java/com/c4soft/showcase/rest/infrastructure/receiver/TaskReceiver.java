package com.c4soft.showcase.rest.infrastructure.receiver;

import com.c4soft.showcase.rest.application.model.TaskEventDTO;
import com.c4soft.showcase.rest.application.service.TaskHandlerApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.function.Consumer;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaskReceiver {

	private final TaskHandlerApplicationService taskHandlerApplicationService;

	@Bean
	public Consumer<Flux<Message<TaskEventDTO>>> executeTask() {
		return taskEventMessage -> taskHandlerApplicationService.executeTask(taskEventMessage).subscribe();
	}
}
