package com.javahunter.scm.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.Objects;

@Slf4j
@Configuration
public class AppConfig {

    @Value("${cloudinary.cloud.name}")
    private String cloudName;

    @Value("${cloudinary.cloud.api.key}")
    private String apiKey;

    @Value("${cloudinary.cloud.api.secret}")
    private String apiSecret;

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name",cloudName,
                "api_key",apiKey,
                "api_secret",apiSecret
        ));
    }
}
