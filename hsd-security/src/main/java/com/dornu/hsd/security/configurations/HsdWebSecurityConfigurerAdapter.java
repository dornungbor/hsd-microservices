package com.dornu.hsd.security.configurations;

import com.dornu.hsd.security.auth.HsdPreAuthenticatedProcessingFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class HsdWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

    @Autowired
    private HsdPreAuthenticatedProcessingFilter hsdPreAuthenticatedProcessingFilter;

    @Autowired
    private HsdSecurityConfiguration hsdSecurityConfiguration;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().addFilter(hsdPreAuthenticatedProcessingFilter);
        hsdSecurityConfiguration.configure(httpSecurity);
    }

}