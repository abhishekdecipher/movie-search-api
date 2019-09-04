package com.moviesearch.api.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @purpose: To fetch properties from resource
 */
@Component
@ConfigurationProperties(prefix = "api")
public class ApiProperties {

    private String uri;
    private String type;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
