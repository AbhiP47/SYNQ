package com.synq.config;
import com.synq.service.SecurityCustomDetailService;
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

@Configuration
public class SecurityConfig {

    @Autowired
    private SecurityCustomDetailService userDetailService;
    @Autowired
    private OAuthenticationSuccessHandler handler;
    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    //Configuration of authentication provider
    @Bean
    public AuthenticationProvider authenticationProvider()
    {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests(authorize -> {
                    authorize
                            .requestMatchers("/login", "/register", "/oauth2/**", "/css/**", "/js/**", "/images/**")
                            .permitAll();

                    // Step 2: Define rules for specific roles (optional but good practice)
                    authorize
                            .requestMatchers("/admin/**").hasRole("ADMIN");

                    // Step 3: Secure EVERYTHING ELSE. This is the "secure by default" rule.
                    authorize
                            .anyRequest().authenticated();
        });
        //form default login
        httpSecurity.formLogin(formLogin ->
                formLogin.loginPage("/login")
                        .loginProcessingUrl("/authenticate")
                        .successHandler(handler)
                        .failureUrl("/login?error=true")
                        .usernameParameter("email")
                        .passwordParameter("password")
                );
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.logout(logoutForm-> {
            logoutForm.logoutUrl("/logout");
            logoutForm.logoutSuccessUrl("/login?logout=true");
        });

        // OAuth configuration
        httpSecurity.oauth2Login(oauth -> {
            oauth.loginPage("/login");
            oauth.userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService));
            oauth.successHandler(handler);
        });


        return httpSecurity.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }
}
