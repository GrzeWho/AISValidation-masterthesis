package com.aisvalidator.analyzer.service.model.external;

import lombok.Data;

@Data
public class ExternalProviderProperties {
    private String url;
    private String apiKey;
    private boolean enabled;
    private String mapperClass;
}
