package com.ggs.STAT_TurboFetch.config;

import com.ggs.STAT_TurboFetch.client.FtClient;
import com.ggs.STAT_TurboFetch.client.FtClientImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ClientConfig {
    private final TokenConfig tokenConfig;

    @Bean
    public FtClient ftClient() {
        return new FtClientImpl(tokenConfig);
    }
}
