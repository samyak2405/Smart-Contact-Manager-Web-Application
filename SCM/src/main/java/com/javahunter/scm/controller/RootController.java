package com.javahunter.scm.controller;

import com.javahunter.scm.entity.User;
import com.javahunter.scm.helper.Helper;
import com.javahunter.scm.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@Slf4j
@RequiredArgsConstructor
@ControllerAdvice
public class RootController {

    private final UserService userService;

    @ModelAttribute
    public void addLoggedInUserInformation(Model model, Authentication authentication) {
        if(authentication == null) {
            return;
        }
        String username = Helper.getEmailOfLoggedInUser(authentication);
        log.info("User addLoggedInUserInformation page username from Rootcontroller: {}", username);
        User user = userService.getUserByEmail(username);
        log.info("user: {}", user);
        model.addAttribute("loggedInUser", user);
    }
}
