package com.synq.controller;

import com.synq.entity.User;
import com.synq.enums.MessageType;
import com.synq.helpers.Message;
import com.synq.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/verify-email")
    public String verifyEmail(
            @RequestParam("token") String token,
            HttpSession session
    )
    {
        User user = userService.getUserByEmailToken(token);
        if(user != null)
        {
            user.setEmailVerified(true);
            user.setEnabled(true);
            userService.updateUser(user);
            session.setAttribute("message", Message.builder()
                    .type(MessageType.green)
                    .content("You email is verified. Now you can login  ")
                    .build());
            return "user/success_page";
        }
        session.setAttribute("message", Message.builder()
                .type(MessageType.red)
                .content("Email not verified ! Token is not associated with user .")
                .build());

        return "user/error_page";
    }
}
