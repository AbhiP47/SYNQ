package com.synq.config;

import com.synq.entity.User;
import com.synq.enums.Provider;
import com.synq.repository.UserRepo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(OAuthenticationSuccessHandler.class);

    @Autowired
    private UserRepo userRepository;
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        logger.info("Google OAuth2 Authentication successful. Processing user payload mapping...");

        // 1. Cast principal to generic OAuth2User to read incoming attribute collections
        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();

        String email = oauthUser.getAttribute("email");
        String name = oauthUser.getAttribute("name");
        String picture = oauthUser.getAttribute("picture");

        logger.info("User identity verified via provider: {}", email);

        // 2. Fetch profile from database using your repo signature
        // Assumes findByEmail returns an Optional<User> container
        User user = userRepository.findByEmail(email).orElse(null);

        // 3. Write profile record to schema tables if it's a first-time login
        if (user == null) {
            logger.info("Email profile {} not found locally. Registering new database record...", email);

            User newUser = new User();
            newUser.setEmail(email);
            newUser.setName(name);
            newUser.setProfilePic(picture);
            newUser.setEnabled(true);
            newUser.setEmailVerified(true);
            newUser.setAbout("This profile was automatically created via Google OAuth2 initialization.");

            newUser.setProvider(Provider.GOOGLE);
            newUser.setProviderId(oauthUser.getName());

            userRepository.save(newUser);
            logger.info("New user database row written successfully.");
        } else {
            logger.info("Existing database profile verified for user: {}. Moving to request redirect step.", email);
        }

        // 4. Trigger target route redirection
        logger.info("Redirecting user to target application route view context...");
        redirectStrategy.sendRedirect(request, response, "/user/profile");
    }
}