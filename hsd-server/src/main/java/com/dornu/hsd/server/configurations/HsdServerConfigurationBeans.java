package com.dornu.hsd.server.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class HsdServerConfigurationBeans {

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
