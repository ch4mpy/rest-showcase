package com.c4soft.showcase.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ChouetteController {
  private final MachinApi machinApi;
  private final BiduleApi biduleApi;

  @GetMapping("/chouette-truc")
  public String getChouetteTruc() {
    return machinApi.getTruc();
  }

  @GetMapping("/chouette-chose")
  public String getChouetteChose() {
    return biduleApi.getChose();
  }
}
