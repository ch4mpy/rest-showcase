package com.c4soft.showcase.rest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import com.c4_soft.springaddons.rest.RestClientHttpExchangeProxyFactoryBean;

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
