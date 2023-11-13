package org.base.oauth2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
/*
    @Bean
    ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(
                ClientRegistration.withRegistrationId("github")
                        .clientId("d180a2c1fdb7f14108e1")
                        .clientSecret("6144aea24b43dcda66725c3a02b2f4665977d26f")
                        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                        .redirectUriTemplate("http:/localhost:9999/oauth2/callback-github")
                        .authorizationUri("https://github.com/login/oauth/authorize")
                        .tokenUri("https://github.com/login/oauth/access_token")
                        .userInfoUri("https://api.github.com/user")
                        .userNameAttributeName("login")
                        .clientName("GitHub")
                        .build()
                *//*OAuth2ClientRegistration.withRegistrationId("google")
                        .clientId("YOUR_GOOGLE_CLIENT_ID")
                        .clientSecret("YOUR_GOOGLE_CLIENT_SECRET")
                        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                        .redirectUriTemplate("{baseUrl}/oauth2/callback-google")
                        .build()*//*
        );
    }*/


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests(auth -> {
                    auth.antMatchers("/", "/static/**",  "/public/**", "/login", "/access/user", "/favicon.ico").permitAll();
                    auth.anyRequest().authenticated();
                })
                .oauth2Login()
                .loginPage("/login").defaultSuccessUrl("/access/redirect-to-react-app");
        return http.build();
    }

}
