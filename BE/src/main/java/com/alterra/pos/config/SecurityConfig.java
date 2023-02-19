package com.alterra.pos.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtFilter jwtFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .authorizeHttpRequests()
//                .requestMatchers(HttpMethod.GET,"/products/**").permitAll()
//                .requestMatchers(HttpMethod.POST,"/products/**").hasRole("ADMIN")
//                .requestMatchers(HttpMethod.PUT,"/products/**").hasRole("ADMIN")
//                .requestMatchers(HttpMethod.DELETE,"/products/**").hasRole("ADMIN")
//                .requestMatchers("/categories/**").hasRole("ADMIN")
//                .requestMatchers(HttpMethod.GET,"/paymentMethod/**").permitAll()
//                .requestMatchers("/membership/**").hasRole("ADMIN")
//                .requestMatchers(HttpMethod.GET,"/orders/**").hasRole("ADMIN")
//                .requestMatchers("/receipts/**").hasRole("ADMIN")
//                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
