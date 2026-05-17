package com.synq.controller;

import com.synq.entity.Contact;
import com.synq.service.ContactService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final ContactService service;

    public ApiController(ContactService service) {
        this.service = service;
    }


    // Get contact of user
    @GetMapping("/contacts/{contactId}")
    public Contact getContact(@PathVariable String contactId)
    {
        return service.getById(contactId);
    }
}
