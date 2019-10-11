package com.wyf.uploadserver.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "upload")
public class UploadProperties {

    private String baseUrl;

    private String ip;

}
