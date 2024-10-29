package com.c4soft.showcase.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class ChouetteController {
  private final MachinApi machinApi;
  private final BiduleApi biduleApi;

  @GetMapping("/chouette-truc")
  public Mono<String> getChouetteTruc() {
    return machinApi.getTruc();
  }

  @GetMapping("/chouette-chose")
  public Mono<String> getChouetteChose() {
    return biduleApi.getChose();
  }
}
