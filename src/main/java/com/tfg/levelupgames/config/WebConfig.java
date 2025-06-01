package com.tfg.levelupgames.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Recursos para imágenes de portada y otros
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");

        // Recursos para archivos descargables
        registry.addResourceHandler("/downloadables/**")
                .addResourceLocations("file:downloadables/");
    }
}
