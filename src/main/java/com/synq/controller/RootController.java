package com.synq.controller;

import com.synq.entity.User;
import com.synq.helpers.Helper;
import com.synq.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@Slf4j
public class RootController {

    @Autowired
    private UserService userService;

    @Autowired
    private Helper helper;

    /**
     * This method acts as a global interceptor to provide user data to all Thymeleaf/HTML views.
     * It runs before every @RequestMapping method in your application.
     */
    @ModelAttribute
    public void addLoggedInUserInformation(Model model, Authentication authentication) {

        // 1. Safety Check: If authentication is null or the user is anonymous,
        // they aren't "logged in" in the way we need.
        if (authentication == null ||
                !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken) {

            log.debug("No authenticated user found for this request.");
            return;
        }

        try {
            // 2. Extract identify (Email/Username) using your helper
            String username = helper.getEmailOfLoggedInUser(authentication);
            log.info("Processing metadata for logged-in user: {}", username);

            // 3. Fetch user details from DB
            User user = userService.getUserByEmail(username);

            if (user != null) {
                // Add user object to the model so it can be accessed as ${loggedInUser} in HTML
                model.addAttribute("loggedInUser", user);
            } else {
                log.warn("Authenticated user {} exists in session but not in database", username);
                model.addAttribute("loggedInUser", null);
            }

        } catch (Exception e) {
            log.error("Error while adding global user information to model: {}", e.getMessage());
            model.addAttribute("loggedInUser", null);
        }
    }
}