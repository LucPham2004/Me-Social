package com.me_social.MeSocial.configuration.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/api/**").permitAll();

                    //auth.anyRequest().authenticated();
                })
                // .oauth2ResourceServer((oauth2) -> oauth2
                //     .jwt(jwtConfigurer -> jwtConfigurer.decoder(JwtDecoder())
                //                                 .jwtAuthenticationConverter(jwtAuthenticationConverter())))
                // .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }
}
