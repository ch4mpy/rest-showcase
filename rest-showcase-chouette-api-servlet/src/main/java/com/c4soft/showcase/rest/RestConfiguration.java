package com.c4soft.showcase.rest;

import java.net.URI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.client.OAuth2ClientHttpRequestInterceptor;
import org.springframework.security.oauth2.core.AbstractOAuth2Token;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class RestConfiguration {

  @Bean
  RestClient machinClient(@Value("machin-base-uri") URI machinBaseUri) {
    return RestClient.builder().baseUrl(machinBaseUri)
        .requestInterceptor(forwardingClientHttpRequestInterceptor()).build();
  }

  @Bean
  MachinApi machinApi(RestClient machinClient) {
    return HttpServiceProxyFactory.builderFor(RestClientAdapter.create(machinClient)).build()
        .createClient(MachinApi.class);
  }

  @Bean
  RestClient biduleClient(@Value("bidule-base-uri") URI biduleBaseUri,
      OAuth2AuthorizedClientManager authorizedClientManager,
      OAuth2AuthorizedClientRepository authorizedClientRepository) {
    return RestClient.builder().baseUrl(biduleBaseUri)
        .requestInterceptor(registrationClientHttpRequestInterceptor(authorizedClientManager,
            authorizedClientRepository, "bidule-registration"))
        .build();
  }

  @Bean
  BiduleApi biduleApi(RestClient biduleClient) {
    return HttpServiceProxyFactory.builderFor(RestClientAdapter.create(biduleClient)).build()
        .createClient(BiduleApi.class);
  }

  ClientHttpRequestInterceptor forwardingClientHttpRequestInterceptor() {
    return (HttpRequest request, byte[] body, ClientHttpRequestExecution execution) -> {
      final var auth = SecurityContextHolder.getContext().getAuthentication();
      if (auth != null && auth.getPrincipal() instanceof AbstractOAuth2Token oauth2Token) {
        request.getHeaders().setBearerAuth(oauth2Token.getTokenValue());
      }
      return execution.execute(request, body);
    };
  }

  ClientHttpRequestInterceptor registrationClientHttpRequestInterceptor(
      OAuth2AuthorizedClientManager authorizedClientManager,
      OAuth2AuthorizedClientRepository authorizedClientRepository, String registrationId) {
    final var interceptor = new OAuth2ClientHttpRequestInterceptor(authorizedClientManager);
    interceptor.setClientRegistrationIdResolver((HttpRequest request) -> registrationId);
    interceptor.setAuthorizationFailureHandler(
        OAuth2ClientHttpRequestInterceptor.authorizationFailureHandler(authorizedClientRepository));
    return interceptor;
  }
}
