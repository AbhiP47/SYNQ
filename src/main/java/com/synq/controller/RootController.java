package com.synq.controller;

import com.synq.entity.User;
import com.synq.helpers.Helper;
import com.synq.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;


@ControllerAdvice
@Slf4j
public class RootController {

    @Autowired
    private UserService userService;

    //In the "View/HTML" context, @ControllerAdvice acts like a Global Layout Manager. It ensures that no matter which controller is hit,
    // the UI has the data it needs and the user stays within a styled, branded environment even when a crash occurs.
    @ModelAttribute
    public void addLoggedInUserInformation(Model model , Authentication authentication)
    {
        if(!authentication.isAuthenticated())
        {
            return;
        }
        String username = Helper.getEmailOfLoggedInUser(authentication);
        log.info("Adding logged in user to the model");

        User user = userService.getUserByEmail(username);
        if(user != null)
        {
            model.addAttribute("loggedInUser",user);

        }
        else
        {
            log.info("USER NOT FOUND BY EMAIL");
            model.addAttribute("loggedInUser",null);
        }
    }
}
