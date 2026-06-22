package com.synq.controller;

import com.synq.entity.User;
import com.synq.enums.MessageType;
import com.synq.forms.UserForm;
import com.synq.helpers.Message;
import com.synq.service.serviceImplementation.UserServiceImpl;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
public class PageController {
    @Autowired
    private UserServiceImpl userService;

    @GetMapping("/")
    public String index()
    {
        return "redirect:/home";
    }
    @RequestMapping("/home")
    public String home()
    {
        return "home";
    }

    @RequestMapping("/about")
    public String aboutPage(Model model)
    {
        model.addAttribute("isLogin",false);
        System.out.println("About page loading");
        return "about";
    }

    @RequestMapping("/services")
    public String servicePage()
    {
        System.out.println("Service page loading");
        return "services";
    }

    @GetMapping("/contact")
    public String contact()
    {
        System.out.println("contact");
        return "contact";
    }

    @GetMapping("/login")
    public String login()
    {
    log.info("Initiating login page");
    return "login";
    }

    // this is the registration controller - for showing the view page
    @GetMapping("/register")
    public String register(Model model)
    {
        UserForm userForm = new UserForm();
        model.addAttribute("userForm",userForm);
        log.info("Initiating register page");
        return "register";
    }

    // To process registration form
    @RequestMapping(value="/do-register", method= RequestMethod.POST)
    public String processRegister(@Valid @ModelAttribute UserForm userForm , BindingResult bindingResult , HttpSession session )  // Data Binding
    {
        System.out.println(userForm );
        /*
        Steps:
        fetch  the form data
        validate form data
        save to database
        message for successful registration
        redirection to login page
         */
//        User user = User.builder()
//                .name(userForm.getName())
//                        .email(userForm.getEmail())
//                                .password(userForm.getPassword())
//                                        .about(userForm.getAbout())
//                                                .phoneNumber(userForm.getPhoneNumber())
//                                                        .profilePic("https://imgs.search.brave.com/J1XPhA-Al_wGSPHxqwWvH0IVUHufLQHGn-wKxuV9ScE/rs:fit:500:0:1:0/g:ce/aHR0cHM6Ly9pLnBp/bmltZy5jb20vb3Jp/Z2luYWxzLzI0LzIz/Lzk4LzI0MjM5ODY2/Yzg0OTUxNTg2NjRi/OWQyZjM4NWMxYzM5/LmpwZw")
//                                                                .build();
//
        if(bindingResult.hasErrors())
        {
            return "register";
        }
        User user = new User();
        user.setName(userForm.getName());
        user.setEmail(userForm.getEmail());
        user.setPassword(userForm.getPassword());
        user.setAbout(userForm.getAbout());
        user.setPhoneNumber(userForm.getPhoneNumber());
        user.setEnabled(false);
        user.setProfilePic("https://imgs.search.brave.com/J1XPhA-Al_wGSPHxqwWvH0IVUHufLQHGn-wKxuV9ScE/rs:fit:500:0:1:0/g:ce/aHR0cHM6Ly9pLnBp/bmltZy5jb20vb3Jp/Z2luYWxzLzI0LzIz/Lzk4LzI0MjM5ODY2/Yzg0OTUxNTg2NjRi/OWQyZjM4NWMxYzM5/LmpwZw");

        User savedUser = userService.saveUser(user);
        log.info("Registration of new user is successfull");
        log.info("Registered user is {}",savedUser);

        Message  message = Message.builder().content("Registration Successful").type(MessageType.green).build();
        session.setAttribute("message",message);

        return "redirect:/register";
    }

    @GetMapping("/about")
    public String about()
    {
        log.info("Initiating About page");
        return "about";
    }

}
