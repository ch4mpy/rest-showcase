package com.c4soft.showcase.rest.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;

@Configuration
public class ClientConfig {

	@Bean
	ReactiveOAuth2AuthorizedClientManager authorizedClientManager(
			ReactiveClientRegistrationRepository clientRegistrationRepository,
			ReactiveOAuth2AuthorizedClientService authorizedClientService) {
		return new AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager(clientRegistrationRepository,
				authorizedClientService);
	}

	@Bean
	ServerOAuth2AuthorizedClientExchangeFilterFunction serverOAuth2AuthorizedClientExchangeFilterFunction(
			ReactiveOAuth2AuthorizedClientManager authorizedClientManager,
			@Value("${spring.security.oauth2.client.registration.keycloak.provider}") String registrationId) {
		var serverOAuth2AuthorizedClientExchangeFilterFunction = new ServerOAuth2AuthorizedClientExchangeFilterFunction(
				authorizedClientManager);
		serverOAuth2AuthorizedClientExchangeFilterFunction.setDefaultClientRegistrationId(registrationId);
		return serverOAuth2AuthorizedClientExchangeFilterFunction;
	}

}
