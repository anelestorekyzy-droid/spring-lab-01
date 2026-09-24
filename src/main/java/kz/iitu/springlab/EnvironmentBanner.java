package kz.iitu.springlab;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class EnvironmentBanner {

    @Bean
    @Profile("dev")
    public String devBanner() {
        return "DEVELOPMENT";
    }

    @Bean
    @Profile("prod")
    public String prodBanner() {
        return "PRODUCTION";
    }

    @Bean
    @Profile("!dev & !prod")
    public String defaultBanner() {
        return "DEFAULT";
    }
}