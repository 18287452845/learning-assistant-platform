package com.learning.infra.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 文件上传配置属性
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "upload")
public class UploadProperties {

    /**
     * 上传基础路径
     */
    private String basePath = "./uploads";

    /**
     * 头像上传目录
     */
    private String avatarDir = "./uploads/avatar";

    /**
     * 资源上传目录
     */
    private String resourceDir = "./uploads/resources";
}
