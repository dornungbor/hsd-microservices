package com.dornu.hsd.security.configurations;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

public interface HsdSecurityConfiguration {
    void configure(HttpSecurity httpSecurity) throws Exception;
}
