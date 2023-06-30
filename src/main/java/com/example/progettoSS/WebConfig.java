package com.example.progettoSS;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthenticationInterceptor())
                .addPathPatterns("/user/getplants", "/admin/getplants") // Aggiungi qui i percorsi che desideri proteggere
                .excludePathPatterns("/", "/login/register", "/userLogin") // Escludi i percorsi per la pagina di login e registrazione
                .addPathPatterns("/addbooking", "/admin/uploadplant", "/registeruser") // Aggiungi qui i percorsi POST che desideri proteggere
                .addPathPatterns("/deletebooking/*", "/admin/deleteplant/*"); // Percorsi DELETE

    }
}

