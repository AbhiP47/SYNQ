package com.synq.controller;

import com.synq.config.UserDetailsImpl;
import com.synq.entity.Contact;
import com.synq.entity.User;
import com.synq.helpers.Helper;
import com.synq.helpers.Message;
import com.synq.enums.MessageType;
import com.synq.repository.UserRepo;
import com.synq.service.ContactService;
import com.synq.service.EmailService;
import com.synq.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserRepo userRepo;
    private final UserService userService;
    private final ContactService contactService;
    private final EmailService emailService;
    private final Helper helper;



    @Autowired
    public UserController(UserRepo userRepo, UserService userService, ContactService contactService, EmailService emailService, Helper helper) {
        this.userRepo = userRepo;
        this.userService = userService;
        this.contactService = contactService;
        this.emailService = emailService;
        this.helper = helper;
    }

    @GetMapping("/dashboard")
    public String userDashboard()
    {
        return "user/dashboard";
    }

    @GetMapping("/profile")
    public String userProfile(Authentication authentication, Model model) {
        //  Get the email from the authenticated principal
        String email = helper.getEmailOfLoggedInUser(authentication);

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

    @GetMapping("/feedback")
    public String userFeedback()
    {
        return "user/feedback";
    }

    @GetMapping("/direct-email")
    public String directEmail(Authentication authentication, Model model) {
        String email = helper.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(email);
        List<Contact> contacts = contactService.getByUserId(String.valueOf(user.getId()));
        model.addAttribute("contacts", contacts);
        return "user/direct_email";
    }

    @PostMapping("/send-email")
    public String sendEmail(@RequestParam("to") String to, @RequestParam("subject") String subject, @RequestParam("message") String message, HttpSession session , Authentication authentication) {
        try {
            emailService.sendEmail(to, subject, message , authentication);
            session.setAttribute("message", Message.builder().content("Email sent successfully").type(MessageType.green).build());
        } catch (Exception e) {
            session.setAttribute("message", Message.builder().content("Failed to send email").type(MessageType.red).build());
        }
        return "redirect:/user/direct-email";
    }
}
