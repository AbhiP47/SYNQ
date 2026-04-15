package com.synq.config;

import com.synq.entity.User;
import com.synq.enums.Provider;
import com.synq.repository.UserRepo;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    @Autowired
    private UserRepo userRepo;

    private Logger logger = LoggerFactory.getLogger(CustomOAuth2UserService.class);

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest)
    {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String authorizedClientRegistrationId = userRequest.getClientRegistration().getRegistrationId();
        String email = oAuth2User.getAttribute("email");
        Optional<User> userOptional = userRepo.findByEmail(email);


        if(userOptional.isPresent())
        {
            logger.info("User already exists in the database... Updating the Details...");
            return (OAuth2User) userOptional.get();
        }
        else
        {
            logger.info("First time login for this user. Creating a new record...");
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setEnabled(true);
            newUser.setEmailVerified(true);
            newUser.setPassword(null);
            if(authorizedClientRegistrationId.equalsIgnoreCase("google"))
            {
                newUser.setProvider(Provider.GOOGLE);
                newUser.setName(oAuth2User.getAttribute("nmae"));
                newUser.setProfilePic(oAuth2User.getAttribute("picture"));
            }
            else if(authorizedClientRegistrationId.equalsIgnoreCase("linkedin"))
            {
                String firstName = oAuth2User.getAttribute("given_name");
                String lastName = oAuth2User.getAttribute("family_name");
                newUser.setName(firstName + " " + lastName);
                newUser.setProvider(Provider.LINKEDIN);
                newUser.setProfilePic(oAuth2User.getAttribute("picture"));
            }
            userRepo.save(newUser);
        }
        return oAuth2User;
    }
}
