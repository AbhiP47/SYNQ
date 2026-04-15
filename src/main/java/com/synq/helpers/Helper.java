package com.synq.helpers;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.dialect.unique.CreateTableUniqueDelegate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;


@Slf4j
public class Helper {

    public static String getEmailOfLoggedInUser(Authentication authentication)
    {
        var authenticationPrincipal =  authentication.getPrincipal();
        if(authenticationPrincipal instanceof OAuth2User)
        {
            OAuth2User oAuth2User = (OAuth2User) authenticationPrincipal;
            var oAuth2AuthenticationToken = (OAuth2AuthenticationToken)authentication;
            var clientId = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();

            String email = "";
            if(clientId.equalsIgnoreCase("google"))
            {
                log.info("Getting email from google");
                email = oAuth2User.getAttribute("email");

            }
            else if (clientId.equalsIgnoreCase("linkedin")) {
                log.info("Getting email from LinkedIn");
                email = oAuth2User.getAttribute("email");
            }
            return email;
        }
        else if(authenticationPrincipal instanceof UserDetails)
        {
            log.info("Getting username from the local database ");
            return ((UserDetails) authenticationPrincipal).getUsername();
        }
        return authentication.getName();
    }
}
