package com.javahunter.scm.helper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.security.Principal;

@Slf4j
public class Helper {

    public static String getEmailOfLoggedInUser(Authentication authentication) {
        String username = "";
        if(authentication instanceof OAuth2AuthenticationToken) {
            var oAuth2Authentication = (OAuth2AuthenticationToken) authentication;
            String clientId = oAuth2Authentication.getAuthorizedClientRegistrationId();
            var oAuth2User = (OAuth2User) oAuth2Authentication.getPrincipal();

            if(clientId.equalsIgnoreCase("google")){
                log.info("Signin from google");
                username = oAuth2User.getAttribute("email");
            }else if(clientId.equalsIgnoreCase("github")){
                log.info("Signin from github");
                username = oAuth2User.getAttribute("email")!=null?oAuth2User.getAttribute("email"):oAuth2User.getAttribute("login")+"@gmail.com";

            }
        }else{
            username = authentication.getName();
        }
        return username;
    }

    public static String getLinkForEmailVerification(String emailToken){
        return "http://localhost:8081/auth/verify-email?token=" + emailToken;
    }
}
