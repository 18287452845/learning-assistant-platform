package com.learning;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 学习助手与问答平台 启动类
 * 
 * @author Learning Assistant Team
 * @version 1.0.0
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.learning"})
@OpenAPIDefinition(
    info = @Info(
        title = "学习助手与问答平台 API",
        version = "1.0.0",
        description = "基于SpringBoot与大语言模型的学习助手与问答平台后端API接口文档",
        contact = @Contact(
            name = "技术支持",
            email = "support@learning.com"
        ),
        license = @License(
            name = "Apache 2.0",
            url = "https://www.apache.org/licenses/LICENSE-2.0"
        )
    ),
    servers = {
        @Server(url = "http://localhost:8080", description = "开发环境"),
        @Server(url = "https://api.learning.com", description = "生产环境")
    }
)
@SecurityScheme(
    name = "Bearer Authentication",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer",
    description = "JWT认证令牌"
)
public class LearningAssistantApplication {

    public static void main(String[] args) {
        SpringApplication.run(LearningAssistantApplication.class, args);
    }
}
