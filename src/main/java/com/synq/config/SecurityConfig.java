package com.synq.config;

import com.synq.service.serviceImplementation.SecurityCustomDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@Configuration
public class SecurityConfig {

    @Autowired
    private SecurityCustomDetailService userDetailService;

    @Autowired
    private OAuthenticationSuccessHandler handler;

    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

    // Configuration of authentication provider
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests(authorize -> {
                    // FIX 1: Whitelisted '/do-register' so signup form data can reach your controller
                    authorize
                            .requestMatchers("/login", "/register", "/do-register", "/oauth2/**", "/css/**", "/js/**", "/images/**")
                            .permitAll();

                    // Define rules for specific roles
                    authorize
                            .requestMatchers("/admin/**").hasRole("ADMIN");

                    // Secure EVERYTHING ELSE.
                    authorize
                            .anyRequest().authenticated();
                });

        // Standard Form Login configuration
        httpSecurity.formLogin(formLogin ->
                formLogin.loginPage("/login")
                        .loginProcessingUrl("/authenticate")
                        // FIX 2: Swapped out OAuth handler for a safe, default redirection path to avoid ClassCastExceptions
                        .defaultSuccessUrl("/user/profile", true)
                        .failureUrl("/login?error=true")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .failureHandler(authenticationFailureHandler)
        );

        // Disabling CSRF for easier local testing/API requests
        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        // Logout configuration
        httpSecurity.logout(logoutForm -> {
            logoutForm.logoutUrl("/logout");
            logoutForm.logoutSuccessUrl("/login?logout=true");
        });

        // OAuth2 login configuration
        httpSecurity.oauth2Login(oauth -> {
            oauth.loginPage("/login");
            oauth.userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService));
            oauth.successHandler(handler); // Perfect! Keeps the OAuth payload isolated here
        });

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}