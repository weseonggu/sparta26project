package com.sparta26.baemin.security;

import com.sparta26.baemin.jwt.JWTFilter;
import com.sparta26.baemin.jwt.JwtAuthenticationEntryPoint;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint entryPoint;
    private final JWTFilter jwtFilter;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();
    }


    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);

        http.formLogin(AbstractHttpConfigurer::disable);

        http.httpBasic(AbstractHttpConfigurer::disable);

        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));



        http.authorizeHttpRequests((auth) -> auth
                .requestMatchers( "/v1/signUp","/v1/logIn").permitAll()
                .requestMatchers("/v1/members/myinfo/**").hasRole("CUSTOMER")
                .anyRequest().authenticated()
        );

        http.addFilterAfter(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        http.exceptionHandling(handler -> handler.authenticationEntryPoint(entryPoint));
        // 권한이 없는 사용자가 접근할 때의 처리
        http.exceptionHandling(handler -> handler
                // 권한이 없는 사용자가 접근할 때의 처리
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.setContentType("application/json;charset=UTF-8");
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.getWriter().write("{\"error\": \"사용자의 권한으로는 접근이 불가합니다.\"}");
                }));
        return http.build();
    }
}
