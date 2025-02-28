package com.c4soft.showcase.rest.infrastructure.config;

import generated.com.c4soft.showcase.rest.infrastructure.rest.chronos.TaskManagerClient;
import generated.com.c4soft.showcase.rest.infrastructure.rest.chronos.invoker.ApiClient;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;

@Getter
@Setter
@Configuration
@Validated
@ConfigurationProperties(prefix = "worker.chronos")
public class ChronosClientConfig {

    @NotNull
    private URI url;

    @Bean
    ReactiveOAuth2AuthorizedClientManager authorizedClientManager(
            ReactiveClientRegistrationRepository clientRegistrationRepository,
            ReactiveOAuth2AuthorizedClientService authorizedClientService
    ) {
        return new AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager(
                clientRegistrationRepository, authorizedClientService
        );
    }

    @Bean
    public ApiClient chronosApiClient(WebClient.Builder chronosClientBuilder) {
        // We need to force the base path because the OpenApi generated classes try to use the spec server url by default
        return new ApiClient(chronosClientBuilder.build()).setBasePath(url.toString());
    }

    @Bean
    public TaskManagerClient taskManagerClient(ApiClient apiClient) {
        return new TaskManagerClient(apiClient);
    }

}
