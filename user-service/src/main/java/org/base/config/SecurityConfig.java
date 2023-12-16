package org.base.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests(auth -> {
                        auth.antMatchers("/public/**", "/access/**", "/favicon.ico").permitAll();
                    auth.anyRequest().authenticated();
                })
                .oauth2Login()
                .loginPage("/login")
                .defaultSuccessUrl("/access/redirect-to-react-app");
        return http.build();
    }
}
