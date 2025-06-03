package com.tfg.levelupgames.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dono6abrn",
                "api_key", "292832137389456",
                "api_secret", "bQr6UFPIvN29bnc4FRy2x64rbGQ",
                "secure", true
        ));
    }
}