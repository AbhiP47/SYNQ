package com.synq.service;

import com.synq.entity.Contact;
import com.synq.entity.User;

import java.util.List;

public interface ContactService {

    Contact save(Contact contact);

    void update(Contact contact);

    List<Contact> getAll();

    Contact getById(String id);

    void delete(String id);

    List<Contact> search(String name , String email , String phoneNumber);

    // get all contacts for a user
    List<Contact> getByUserId(String userId);

    List<Contact> getByUser(User user);
}
