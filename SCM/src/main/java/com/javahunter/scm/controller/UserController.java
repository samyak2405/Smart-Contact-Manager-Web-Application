package com.javahunter.scm.controller;

import com.javahunter.scm.entity.User;
import com.javahunter.scm.helper.Helper;
import com.javahunter.scm.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user")
@Controller
public class UserController {

    private final UserService userService;

    //user dashboard page



    @GetMapping("/dashboard")
    public String userDashBoard(){
        log.info("userDashBoard page loading");
        return "user/dashboard";
    }

    //user profile page
    @GetMapping("/profile")
    public String userProfile(Model model, Authentication authentication){
        log.info("User profile page loading");
        //Get user from db
        return "user/profile";
    }
}
