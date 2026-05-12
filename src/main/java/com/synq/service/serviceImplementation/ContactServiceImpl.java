package com.synq.service.serviceImplementation;

import com.synq.entity.Contact;
import com.synq.helpers.ResourceNotFoundException;
import com.synq.repository.ContactRepo;
import com.synq.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactRepo contactRepo;

    @Override
    public Contact save(Contact contact) {
        String contactId = UUID.randomUUID().toString();
        contact.setId(contactId);
        return contactRepo.save(contact);
    }

    @Override
    public void update(Contact contact) {
        return;
    }

    @Override
    public List<Contact> getAll() {
        return contactRepo.findAll();
    }

    @Override
    public Contact getById(String id) {
        return contactRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException(STR."Contact not found with the given id : \{id}"));
    }

    @Override
    public void delete(String id) {
        contactRepo.deleteById(id);

    }

    @Override
    public List<Contact> search(String name, String email, String phoneNumber) {
        return List.of();
    }

    @Override
    public List<Contact> getByUserId(String userId) {
        return contactRepo.findByUserId(userId);
    }
}
