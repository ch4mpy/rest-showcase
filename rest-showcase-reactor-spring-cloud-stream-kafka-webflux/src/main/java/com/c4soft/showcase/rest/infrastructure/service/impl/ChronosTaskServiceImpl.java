package com.c4soft.showcase.rest.infrastructure.service.impl;

import com.c4soft.showcase.rest.domain.service.ChronosTaskService;
import generated.com.c4soft.showcase.rest.infrastructure.rest.chronos.TaskManagerClient;
import generated.com.c4soft.showcase.rest.infrastructure.rest.chronos.model.ChronosCloseTaskRequestDTO;
import generated.com.c4soft.showcase.rest.infrastructure.rest.chronos.model.ChronosCloseTaskRequestDTO.StatusEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChronosTaskServiceImpl implements ChronosTaskService {

	private final TaskManagerClient taskManagerClient;

	@Override
	public Mono<Void> startTask(Long id) {
		return taskManagerClient.startTask(id);
	}

	@Override
	public Mono<Void> finishTask(Long id) {
		var finishBody = new ChronosCloseTaskRequestDTO().status(StatusEnum.SUCCESSFUL);

		return taskManagerClient.closeTask(id, finishBody);
	}

}
