package com.c4soft.showcase.rest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class ChouetteApiServletAddonsApplicationTests {
  @MockitoBean
  JwtDecoder jwtDecoder;

  @Test
  void contextLoads() {}

}
