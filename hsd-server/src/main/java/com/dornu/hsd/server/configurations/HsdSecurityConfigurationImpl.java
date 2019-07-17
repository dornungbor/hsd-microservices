package com.dornu.hsd.server.configurations;

import com.dornu.hsd.security.configurations.HsdSecurityConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
public class HsdSecurityConfigurationImpl implements HsdSecurityConfiguration {

    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeRequests()
                .antMatchers("/api/v1/token").permitAll()
                .antMatchers("/api/**").authenticated();
    }
}
