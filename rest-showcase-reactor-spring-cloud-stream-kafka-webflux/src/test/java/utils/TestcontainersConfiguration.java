package utils;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistrar;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers(parallel = true)
@TestConfiguration(proxyBeanMethods = false)
public class TestcontainersConfiguration {

  @Container
  private final static KeycloakContainer keycloakContainer = ConfiguredTestContainers.createKeycloakContainer();

  @Bean
  KeycloakContainer keycloakContainer() {
    return keycloakContainer;
  }

  @Bean
  DynamicPropertyRegistrar dynamicPropertyRegistrar(
      KeycloakContainer keycloakContainer
  ) {
    return registry -> ConfiguredTestContainers.configureKeycloakContainer(registry, keycloakContainer);
  }
}
