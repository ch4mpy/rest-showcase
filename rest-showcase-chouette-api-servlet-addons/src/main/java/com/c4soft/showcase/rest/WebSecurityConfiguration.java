package com.c4soft.showcase.rest;

import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.endpoint.RestClientClientCredentialsTokenResponseClient;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfiguration {

  @Bean
  SecurityFilterChain filterChain(HttpSecurity http, ServerProperties serverProperties)
      throws Exception {

    http.oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

    http.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    http.csrf(csrf -> csrf.disable());

    http.exceptionHandling(eh -> eh.authenticationEntryPoint((request, response, authException) -> {
      response.addHeader(HttpHeaders.WWW_AUTHENTICATE, "Bearer realm=\"Restricted Content\"");
      response.sendError(HttpStatus.UNAUTHORIZED.value(),
          HttpStatus.UNAUTHORIZED.getReasonPhrase());
    }));

    http.authorizeHttpRequests(requests -> requests.anyRequest().authenticated());

    return http.build();
  }

  /**
   * @param clientRegistrationRepository
   * @param authorizedClientRepository
   * @return An authorized client manager using the new RestClient to get tokens from the
   *         authorization server.
   */
  @Bean
  OAuth2AuthorizedClientManager authorizedClientManager(
      ClientRegistrationRepository clientRegistrationRepository,
      OAuth2AuthorizedClientRepository authorizedClientRepository) {
    final var authorizedClientManager = new DefaultOAuth2AuthorizedClientManager(
        clientRegistrationRepository, authorizedClientRepository);
    // This application uses only the client credntials flow
    authorizedClientManager.setAuthorizedClientProvider(
        OAuth2AuthorizedClientProviderBuilder.builder().clientCredentials(clientCredentials -> {
          final var accessTokenResponseClient =
              new RestClientClientCredentialsTokenResponseClient();
          clientCredentials.accessTokenResponseClient(accessTokenResponseClient);
        }).build());
    return authorizedClientManager;
  }

}
