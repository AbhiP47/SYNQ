package com.synq.controller;

import com.synq.config.UserDetailsImpl;
import com.synq.entity.User;
import com.synq.helpers.Helper;
import com.synq.repository.UserRepo;
import com.synq.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserRepo userRepo;
    private final UserService userService;



    @Autowired
    public UserController(UserRepo userRepo, UserService userService) {
        this.userRepo = userRepo;
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    public String userDashboard()
    {
        return "user/dashboard";
    }

    @GetMapping("/profile")
    public String userProfile(Authentication authentication, Model model) {
        //  Get the email from the authenticated principal
        String email = Helper.getEmailOfLoggedInUser(authentication);

        //  Fetch the user from the database
        User user = userService.getUserByEmail(email);

        //  If database returned null, invalidate session safely
        if (user == null) {
            logger.warn("Profile requested for dirty session user: {}. Redirecting to logout.", email);
            return "redirect:/logout";
        }

        logger.info("Loading profile view for user: {}", user.getEmail());

        model.addAttribute("user", user);
        return "user/profile";
    }
}
