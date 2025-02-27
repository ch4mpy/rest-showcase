package utils;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import lombok.experimental.UtilityClass;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

@UtilityClass
public class ConfiguredTestContainers {

  private static final String INTEGRATION_REALM_LOCATION = "/realm-export.json";

  public static KeycloakContainer createKeycloakContainer() {
    return new KeycloakContainer().withRealmImportFile(INTEGRATION_REALM_LOCATION);
  }

  public static void configureKeycloakContainer(
      DynamicPropertyRegistry registry, KeycloakContainer keycloakContainer
  ) {
    registry.add("keycloak.base-url", keycloakContainer::getAuthServerUrl);
  }

  public static KafkaContainer createKafkaContainer() {
    return new KafkaContainer(DockerImageName.parse("apache/kafka-native").withTag("3.8.1"));
  }

  public static void configureKafkaContainer(
      DynamicPropertyRegistry registry, KafkaContainer kafkaContainer
  ) {
    registry.add("spring.cloud.stream.kafka.binder.brokers", kafkaContainer::getBootstrapServers);
  }

}
