package com.c4soft.showcase.rest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.client.AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.web.reactive.function.client.WebClient;
import com.c4_soft.springaddons.rest.HttpExchangeProxyFactoryBean;

@Configuration
public class RestConfiguration {

  @Profile("custom-authorized-client-manager")
  @Bean
  ReactiveOAuth2AuthorizedClientManager authorizedClientManager(
      ReactiveClientRegistrationRepository clientRegistrationRepository,
      ReactiveOAuth2AuthorizedClientService authorizedClientService) {
    return new AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager(
        clientRegistrationRepository, authorizedClientService);
  }

  @Bean
  BiduleApi biduleApi(WebClient biduleClient) throws Exception {
    return new HttpExchangeProxyFactoryBean<>(BiduleApi.class, biduleClient).getObject();
  }


  @Bean
  MachinApi machinApi(WebClient machinClient) throws Exception {
    return new HttpExchangeProxyFactoryBean<>(MachinApi.class, machinClient).getObject();
  }

}
