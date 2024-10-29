package com.c4soft.showcase.rest;

import java.nio.charset.Charset;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import reactor.core.publisher.Mono;

@Configuration
public class WebSecurityConfiguration {

  @Bean
  SecurityWebFilterChain filterChain(ServerHttpSecurity http, ServerProperties serverProperties)
      throws Exception {

    http.oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

    http.securityContextRepository(NoOpServerSecurityContextRepository.getInstance());

    http.csrf(csrf -> csrf.disable());

    http.exceptionHandling(exceptionHandling -> {
      exceptionHandling.accessDeniedHandler(accessDeniedHandler());
    });

    http.authorizeExchange(exchange -> exchange.anyExchange().authenticated());

    return http.build();
  }

  private ServerAccessDeniedHandler accessDeniedHandler() {
    return (var exchange, var ex) -> exchange.getPrincipal().flatMap(principal -> {
      var response = exchange.getResponse();
      response
          .setStatusCode(principal instanceof AnonymousAuthenticationToken ? HttpStatus.UNAUTHORIZED
              : HttpStatus.FORBIDDEN);
      response.getHeaders().setContentType(MediaType.TEXT_PLAIN);
      var dataBufferFactory = response.bufferFactory();
      var buffer = dataBufferFactory.wrap(ex.getMessage().getBytes(Charset.defaultCharset()));
      return response.writeWith(Mono.just(buffer))
          .doOnError(error -> DataBufferUtils.release(buffer));
    });
  }

}
