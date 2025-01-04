package com.ggs.STAT_TurboFetch.api.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class TokenConfig {

    @Value("${42.client-id}")
    private String clientId;

    @Value("${42.client-secret}")
    private String clientSecret;

    @Value("${42.redirect-uri}")
    private String redirectURI;

    private String grantType = "authorization_code";
    private String tokenURI = "https://api.intra.42.fr/oauth/token";


}
