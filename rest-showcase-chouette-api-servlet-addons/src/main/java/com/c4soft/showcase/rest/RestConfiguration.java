package com.c4soft.showcase.rest;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientHttpServiceGroupConfigurer;
import org.springframework.web.service.registry.ImportHttpServices;

@Configuration
@ImportHttpServices(group = "bidule", types = {BiduleApi.class})
@ImportHttpServices(group = "machin", types = {MachinApi.class})
public class RestConfiguration {

  // @Bean
  RestClientHttpServiceGroupConfigurer groupConfigurer(RestClient.Builder biduleClientBuilder,
      RestClient.Builder machinClientBuilder) {

    return groups -> {
      groups.forEachClient((group) -> switch (group.name()) {
        case "bidule" -> biduleClientBuilder;
        case "machin" -> machinClientBuilder;
        default -> throw new IllegalStateException("Unexpected group: " + group.name());
      });
    };
  }

}
