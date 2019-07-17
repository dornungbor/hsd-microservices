package com.dornu.hsd.server.configurations;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = "classpath:application.yml", ignoreResourceNotFound = false)
@ConfigurationProperties(prefix = "hsd-app")
@Data
public class HsdServerConfiguration {
    private Credentials credentials;
    @Data
    public static class Credentials {
        private String username;
        private String password;
    }
}
