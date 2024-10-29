package com.c4soft.showcase.rest;

import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import reactor.core.publisher.Mono;

@HttpExchange
public interface BiduleApi {
  @GetExchange("/chose")
  Mono<String> getChose();
}
