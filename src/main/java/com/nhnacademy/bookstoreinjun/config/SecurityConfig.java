package com.nhnacademy.bookstoreinjun.config;

import com.nhnacademy.bookstoreinjun.filter.HeaderFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        String GET = "GET";
        String POST = "POST";
        String DELETE = "DELETE";
        String PUT = "PUT";
        String ADMIN = "ROLE_ADMIN";

        http
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(new HeaderFilter("/api/product/client/**",POST), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new HeaderFilter("/api/product/client/**",PUT), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new HeaderFilter("/api/product/client/**",DELETE), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new HeaderFilter("/api/product/admin/**",GET, ADMIN), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new HeaderFilter("/api/product/admin/**",POST, ADMIN), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new HeaderFilter("/api/product/admin/**",PUT, ADMIN), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(req ->
                                req.anyRequest().permitAll()
                )

                .headers(h -> h.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
