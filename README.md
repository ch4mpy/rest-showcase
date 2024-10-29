# `RestClient` & `WebClient` in Resource Servers

Demoes `RestClient` & `WebClient` usage for communicating between resource servers with OAuth2 authorization.

## Use case
This repo contains a multi-module Maven project for a pool of `oauth2ResourceServer` microservices.

`MicroserviceChouetteApplication` calls:
- `MicroserviceMachinApplication`  on behalf of the resource owner at the origin of the requests: the requests are authorized **re-using the `Bearer` token in the security-context** (`MicroserviceChouetteApplication` is a resource server, so the request it processes already is authorized with a `Bearer` token).
- `MicroserviceBiduleApplication` in its own name: **a new `Bearer` token is acquired using client-credentials flow**.

The `MicroserviceMachinApplication` exposes an OpenAPI document from which we can generate the following:
```java
@HttpExchange
public interface MachinApi {
  @GetExchange("/truc")
  String getTruc();
}
```

The `MicroserviceBiduleApplication` exposes an OpenAPI document from which we can generate the following:
```java
@HttpExchange
public interface BiduleApi {
  @GetExchange("/chose")
  String getChose();
}
```

`MicroserviceChouetteApplication` collaborates with the two REST APIs above as follows:
```java
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
```
This requires implementations for `MachinApi` and `BiduleApi` to be exposed as beans, internally using a `RestClient` or `WebClient` instance to retrieve REST resources from other services - authorizing these requests with `Bearer` tokens.

Naturally, the `rest-showcase-chouette-api-webflux-addons` module uses `Mono<String>` instead of `String` as return types.

## Usage
You need an OIDC authorization server with two clients:
- one configured for authorization code flow
- one configured with client credentials flow

Once you have the OP *Issuer Identifier*, as well as client IDs and secrets, update `application.yml` files for all 5 child modules (set `issuer` property in all sub-modules, plus `client-id` and `client-secret` in the 3 *chouette* modules).

Start `BiduleApiApplication` and `MachinApiApplication` applications, and then **one** of the `Chouette*ApiApplication`.

Use a REST client of your choice (something like Postman) to send GET requests to `http://localhost:8083/chouette-truc` and `http://localhost:8083/chouette-chose`. Do not forget to authorize requests (get tokens  from the OP using the client configured with authorization-code flow).

## Auto-Configuration
`rest-showcase-chouette-api-servlet-addons` and `rest-showcase-chouette-api-webflux-addons` modules use `spring-addons-starter-rest` to auto-configure and expose as named beans the `RestClient` or `WebClient` instances described in application properties.

As a consequence the following configuration in `rest-showcase-chouette-api-servlet` module:
```java
@Configuration
public class RestConfiguration {

  @Bean
  RestClient machinClient(@Value("machin-base-uri") URI machinBaseUri) {
    return RestClient.builder()
        .baseUrl(machinBaseUri)
        .requestInterceptor(forwardingClientHttpRequestInterceptor())
        .build();
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
    return RestClient.builder()
        .baseUrl(biduleBaseUri)
        .requestInterceptor(
            registrationClientHttpRequestInterceptor(
                authorizedClientManager,
                authorizedClientRepository,
                "bidule-registration"))
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
      OAuth2AuthorizedClientRepository authorizedClientRepository,
      String registrationId) {
    final var interceptor = new OAuth2ClientHttpRequestInterceptor(authorizedClientManager);
    interceptor.setClientRegistrationIdResolver((HttpRequest request) -> registrationId);
    interceptor.setAuthorizationFailureHandler(
        OAuth2ClientHttpRequestInterceptor.authorizationFailureHandler(authorizedClientRepository));
    return interceptor;
  }
}
```
Becomes (Replace  `RestClientHttpExchangeProxyFactoryBean` with `HttpExchangeProxyFactoryBean` when using `WebClient` instead of `RestClient`):
```yaml
com:
  c4-soft:
    springaddons:
      rest:
        client:
          machin-client:
            base-url: http://localhost:${machin-api-port}
            authorization:
              oauth2:
                forward-bearer: true
          bidule-client:
            base-url: http://localhost:${bidule-api-port}
            authorization:
              oauth2:
                oauth2-registration-id: bidule-registration
```
```java
@Configuration
public class RestConfiguration {

  @Bean
  BiduleApi biduleApi(RestClient biduleClient) throws Exception {
    return new RestClientHttpExchangeProxyFactoryBean<>(BiduleApi.class, biduleClient).getObject();
  }

  @Bean
  MachinApi machinApi(RestClient machinClient) throws Exception {
    return new RestClientHttpExchangeProxyFactoryBean<>(MachinApi.class, machinClient).getObject();
  }
}
```
Bootiful, isn't it?
