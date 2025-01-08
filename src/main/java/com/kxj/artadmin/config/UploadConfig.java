package com.kxj.artadmin.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "upload")
@Data
public class UploadConfig {
    private String pictureDir;
    private String pictureRecycle;
    private String workArea;
    private String auditArea;
    private String videoRecycle;
    private String passArea;

}
