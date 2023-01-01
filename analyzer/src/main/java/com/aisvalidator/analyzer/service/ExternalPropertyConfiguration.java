package com.aisvalidator.analyzer.service;

import com.aisvalidator.analyzer.service.model.external.ExternalProviderProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class ExternalPropertyConfiguration {
    @Bean
    @ConfigurationProperties("external.source.datalastic")
    public ExternalProviderProperties datalasticConfig() {
        return new ExternalProviderProperties();
    }

    @Bean
    @ConfigurationProperties("external.source.marinetraffic")
    public ExternalProviderProperties marineTrafficConfig() {
        return new ExternalProviderProperties();
    }
}
