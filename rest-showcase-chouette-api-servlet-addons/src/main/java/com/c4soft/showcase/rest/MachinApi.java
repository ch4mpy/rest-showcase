package com.c4soft.showcase.rest;

import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange
public interface MachinApi {
  @GetExchange("/truc")
  String getTruc();
}
