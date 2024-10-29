package com.c4soft.showcase.rest;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MachinController {

  @GetMapping("/truc")
  String getTruc(Authentication auth) {
    return "Hi %s! Machin whishes you the best.".formatted(auth.getName());
  }

}
