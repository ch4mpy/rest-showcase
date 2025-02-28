package com.c4soft.showcase.rest.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.reactive.function.client.WebClient;
import generated.com.c4soft.showcase.rest.infrastructure.rest.chronos.TaskManagerClient;
import generated.com.c4soft.showcase.rest.infrastructure.rest.chronos.invoker.ApiClient;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
@Validated
public class ChronosClientConfig {

  @Bean
  ReactiveOAuth2AuthorizedClientManager authorizedClientManager(
      ReactiveClientRegistrationRepository clientRegistrationRepository,
      ReactiveOAuth2AuthorizedClientService authorizedClientService) {
    return new AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager(
        clientRegistrationRepository, authorizedClientService);
  }

  @Bean
  public ApiClient chronosApiClient(WebClient.Builder chronosClientBuilder) {
    return new ApiClient(chronosClientBuilder.build());
  }

  @Bean
  public TaskManagerClient taskManagerClient(ApiClient apiClient) {
    return new TaskManagerClient(apiClient);
  }

}
