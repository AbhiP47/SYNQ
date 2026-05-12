package com.synq.controller;

import com.synq.entity.Contact;
import com.synq.entity.User;
import com.synq.enums.MessageType;
import com.synq.forms.ContactForm;
import com.synq.helpers.Helper;
import com.synq.helpers.Message;
import com.synq.service.ContactService;
import com.synq.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user/contacts")
public class ContactController {

    private final ContactService contactService;
    private final UserService userService;

    public ContactController(ContactService contactService , UserService userService)
    {
        this.contactService = contactService;
        this.userService = userService;
    }

    @RequestMapping("/add")
    public String addContactView(Model model) {
        ContactForm contactForm = new ContactForm();

        contactForm.setFavorite(true);
        model.addAttribute("contactForm", contactForm);
        return "user/add_contact";
    }

    @PostMapping("/add")
    public String saveContact(@Valid  @ModelAttribute ContactForm contactForm , BindingResult bindingResult , Authentication authentication , HttpSession httpSession)
    {
        if (bindingResult.hasErrors())
        {
            httpSession.setAttribute("message", Message.builder()
                            .content("Please correct the following errors")
                            .type(MessageType.red)
                    .build());
            return "user/add_contact";
        }

        String userName = Helper.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(userName);
        Contact contact = new Contact();
        contact.setName(contactForm.getName());
        contact.setFavorite(contactForm.isFavorite());
        contact.setEmail(contactForm.getEmail());
        contact.setPhoneNumber(contactForm.getPhoneNumber());
        contact.setAddress(contactForm.getAddress());
        contact.setDescription(contactForm.getDescription());
        contact.setUser(user);
        contact.setLinkedIn(contactForm.getLinkedInLink());
        contact.setWebsiteLink(contactForm.getWebsiteLink());

        contactService.save(contact);
        httpSession.setAttribute("message", Message.builder()
                        .content("You have successfully added the contact")
                        .type(MessageType.green)
                .build());
        return "redirect:/user/contacts/add";
    }
}
