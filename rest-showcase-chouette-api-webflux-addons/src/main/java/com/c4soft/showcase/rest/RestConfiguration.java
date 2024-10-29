package com.c4soft.showcase.rest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import com.c4_soft.springaddons.rest.HttpExchangeProxyFactoryBean;

@Configuration
public class RestConfiguration {

  @Bean
  BiduleApi biduleApi(WebClient biduleClient) throws Exception {
    return new HttpExchangeProxyFactoryBean<>(BiduleApi.class, biduleClient).getObject();
  }


  @Bean
  MachinApi machinApi(WebClient machinClient) throws Exception {
    return new HttpExchangeProxyFactoryBean<>(MachinApi.class, machinClient).getObject();
  }

}
