package com.synq.service;

import org.springframework.security.core.Authentication;

public interface EmailService {

    void sendEmail(String to, String subject , String body , Authentication authentication);

    void sendEmailForVerification(String to, String subject , String body);


    void sendEmailWithHtml();

    void sendEmailWithAttatchment();
}
