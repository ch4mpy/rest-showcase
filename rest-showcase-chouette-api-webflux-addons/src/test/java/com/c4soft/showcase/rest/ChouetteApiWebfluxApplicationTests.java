package com.c4soft.showcase.rest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class ChouetteApiWebfluxApplicationTests {

  @MockitoBean
  ReactiveClientRegistrationRepository clientRegistrationRepository;

  @Test
  void contextLoads() {}

}
