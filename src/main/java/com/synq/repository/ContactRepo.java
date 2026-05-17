package com.synq.repository;

import com.synq.entity.Contact;
import com.synq.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepo extends JpaRepository<Contact, String> {

    @Query("SELECT c FROM Contact c WHERE c.user.id = :userId")
    List<Contact> findByUserId(String userId);

    Page<Contact> findByUser(User user , Pageable pageable);

    Page<Contact> findByUserAndNameContaining(User user , String nameKeyword , Pageable pageable);
    Page<Contact> findByUserAndEmailContaining(User user , String emailKeyword , Pageable pageable);
    Page<Contact> findByUserAndPhoneNumberContaining(User user ,String phoneNumber , Pageable pageable);

}
