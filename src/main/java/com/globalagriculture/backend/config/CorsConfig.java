package com.globalagriculture.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Global CORS config. The Vite dev server proxies every request server-side
 * (see the frontend's vite.config.js), so this only matters if the API is
 * ever called directly from a browser without that proxy in front of it
 * (e.g. a production deploy, or Postman-from-browser testing). Every
 * controller in this project also carries an explicit @CrossOrigin for the
 * same origins, so nothing is left relying on this alone.
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173", "http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
