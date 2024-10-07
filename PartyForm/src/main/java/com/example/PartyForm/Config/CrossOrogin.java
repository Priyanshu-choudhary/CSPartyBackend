package com.example.PartyForm.Config;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CrossOrogin implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Allow all paths
                .allowedOrigins("http://localhost:5173","https://csfreshersparty.vercel.app/") // Replace with your React app's URL
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allow methods
                .allowCredentials(false); // If you need to send cookies
    }
}
