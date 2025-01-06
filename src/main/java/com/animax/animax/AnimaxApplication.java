package com.animax.animax;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

//http://localhost:8080/swagger-ui.html

@EnableWebSecurity
@EnableConfigurationProperties
@SpringBootApplication(scanBasePackages = {"com.animax.animax", "com.animax.animax.configuration"})
//@OpenAPIDefinition(info = @Info(title = "Animax", version = "1.0", description = "testing level"))
public class AnimaxApplication {
    public static void main(String[] args) {
        SpringApplication.run(AnimaxApplication.class, args);
    }

}
