package com.ced.security;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {

    @Value("${security.cache.password}")
    private String cachePassword;

    public String getCachePassword() {
        return cachePassword;
    }
}