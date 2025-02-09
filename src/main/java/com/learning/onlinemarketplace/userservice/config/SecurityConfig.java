package com.learning.onlinemarketplace.userservice.config;

import com.learning.onlinemarketplace.userservice.security.CustomOAuth2SuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends SecurityConfigurerAdapter {
    @Autowired
    private CustomOAuth2SuccessHandler customOAuth2SuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/api/v1/auth/register").permitAll()
                                .anyRequest().authenticated()
                )
                // Cấu hình HTTP Basic theo cách mới
                .httpBasic(httpBasicConfigurer -> httpBasicConfigurer.disable())
                // Cấu hình Form Login theo cách mới
                .formLogin(formLoginConfigurer -> formLoginConfigurer.disable())
                .oauth2Login(oauth2Login ->
                        oauth2Login
                                .loginPage("/login")  // Trang login tự định nghĩa (nếu có)
                                .defaultSuccessUrl("/home", true)
                                .failureUrl("/login?error")
                                .successHandler(customOAuth2SuccessHandler) // Xử lý sau khi đăng nhập thành công
                );
        return http.build();
    }
}
