package com.synq.service.serviceImplementation;

import com.synq.helpers.Helper;
import com.synq.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;


@Service
public  class EmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);


    private final JavaMailSender mailSender;
    private  final Helper helper;



    public EmailServiceImpl(JavaMailSender mailSender, Helper helper) {
        this.mailSender = mailSender;
        this.helper = helper;
    }

    @Value("${app.mail.from}")
    private String from;

    @Override
    public void sendEmail(String to, String subject, String body , Authentication authentication) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            message.setFrom(helper.getEmailOfLoggedInUser(authentication));
            mailSender.send(message);
            logger.info("Email sent successfully to {}", to);
        } catch (Exception e) {
            logger.error("Failed to send email to {}", to, e);
            throw new RuntimeException("Failed to send email", e);
        }
    }

    @Override
    public void sendEmailForVerification(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            message.setFrom(from);
            mailSender.send(message);

    }
        catch (Exception e)
        {
            logger.error("Failed to send email to {}", to, e);
            throw new RuntimeException("Failed to send email", e);

        }
    }

    @Override
    public void sendEmailWithHtml() {
        // TODO: Implement this method
    }

    @Override
    public void sendEmailWithAttatchment() {
        // TODO: Implement this method
    }
}
