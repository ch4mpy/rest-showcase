package com.c4soft.showcase.rest;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BiduleController {

  @GetMapping("/chose")
  String getChose(Authentication auth) {
    return "Bidule whishes you the best on behalf of %s!".formatted(auth.getName());
  }

}
