package com.c4soft.showcase.rest.domain.service;

import reactor.core.publisher.Mono;

public interface ChronosTaskService {

	Mono<Void> startTask(Long id);

	Mono<Void> finishTask(Long id);

}
