package com.synq.controller;

import com.synq.entity.Contact;
import com.synq.entity.User;
import com.synq.enums.MessageType;
import com.synq.forms.ContactForm;
import com.synq.forms.ContactSearchForm;
import com.synq.helpers.AppConstants;
import com.synq.helpers.Helper;
import com.synq.helpers.Message;
import com.synq.service.ContactService;
import com.synq.service.UserService;
import com.synq.service.ImageService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/user/contacts")
public class ContactController {

    private final ContactService contactService;
    private final UserService userService;
    private final ImageService imageService;

    public ContactController(ContactService contactService , UserService userService , ImageService imageService)
    {
        this.contactService = contactService;
        this.userService = userService;
        this.imageService = imageService;
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

        String fileURL = imageService.uploadImage(contactForm.getContactImage());
        log.info(" UPLOADED File URL is : "+fileURL);

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
        contact.setPicture(fileURL);

        contactService.save(contact);
        httpSession.setAttribute("message", Message.builder()
                        .content("You have successfully added the contact")
                        .type(MessageType.green)
                .build());
        return "redirect:/user/contacts/add";
    }

    @RequestMapping
    public String viewContacts( @RequestParam(value = "page" , defaultValue = "0") int page ,
                                @RequestParam(value = "size" , defaultValue = AppConstants.PAGE_SIZE + "") int size ,
            @RequestParam(value = "sortBy" , defaultValue = "name") String sortBy,
            @RequestParam(value = "direction" , defaultValue = "asc") String direction ,
            Model model , Authentication authentication)
    {
        String username = Helper.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(username);

        Page<Contact> contacts = contactService.getByUser(user , page , size , sortBy , direction);
        model.addAttribute("contacts",contacts);
        model.addAttribute("contactSearchForm", new ContactSearchForm());
        return "user/contacts";
    }

    @RequestMapping("/search")
    public String searchHandler(

            @ModelAttribute ContactSearchForm contactSearchForm,
            @RequestParam(value = "size", defaultValue = AppConstants.PAGE_SIZE + "") int size,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            Model model,
            Authentication authentication) {

        log.info("field {} keyword {}", contactSearchForm.getField(), contactSearchForm.getValue());

        var user = userService.getUserByEmail(Helper.getEmailOfLoggedInUser(authentication));

        Page<Contact> pageContact = null;
        if (contactSearchForm.getField().equalsIgnoreCase("name")) {
            pageContact = contactService.searchByName(contactSearchForm.getValue(), size, page, sortBy, direction,
                    user);
        } else if (contactSearchForm.getField().equalsIgnoreCase("email")) {
            pageContact = contactService.searchByEmail(contactSearchForm.getValue(), size, page, sortBy, direction,
                    user);
        } else if (contactSearchForm.getField().equalsIgnoreCase("phone")) {
            pageContact = contactService.searchByPhoneNumber(contactSearchForm.getValue(), size, page, sortBy,
                    direction, user);
        }

        log.info("pageContact {}", pageContact);

        model.addAttribute("contactSearchForm", contactSearchForm);

        model.addAttribute("pageContact", pageContact);

        model.addAttribute("pageSize", AppConstants.PAGE_SIZE);

        return "user/search";
    }

    @GetMapping("/delete/{id}")
    public String deleteContact(@PathVariable("id") String id, Authentication authentication , HttpSession session) {
        String loggedInUserEmail = Helper.getEmailOfLoggedInUser(authentication);
        User currentUser = userService.getUserByEmail(loggedInUserEmail);

        Contact contact = contactService.getById(id);

        if (contact == null || !contact.getUser().getId().equals(currentUser.getId())) {
            log.warn("Unauthorized delete attempt by user: {} on contact ID: {}", loggedInUserEmail, id);
            return "redirect:/user/contacts?error=unauthorized";
        }

        contactService.delete(id);
        session.setAttribute("message",
                Message.builder()
                        .content("Contact is deleted successfully")
                        .type(MessageType.green)
                        .build());
        return "redirect:/user/contacts";
    }

}

