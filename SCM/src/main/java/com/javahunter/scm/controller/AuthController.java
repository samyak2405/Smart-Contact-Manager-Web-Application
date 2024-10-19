package com.javahunter.scm.controller;

import com.javahunter.scm.dto.Message;
import com.javahunter.scm.entity.User;
import com.javahunter.scm.enums.MessageType;
import com.javahunter.scm.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/auth")
public class AuthController {

    // verify email

    @Autowired
    private UserService userService;

    @GetMapping("/verify-email")
    public String verifyEmail(
            @RequestParam("token") String token, HttpSession session) {

        User user = userService.findByEmailToken(token);

        if (user != null) {
            // user fetch hua hai :: process karna hai

            if (user.getEmailToken().equals(token)) {
                user.setEmailVerified(true);
                user.setEnabled(true);
                userService.updateUser(user);
                session.setAttribute("message", Message.builder()
                        .type(MessageType.green)
                        .content("You email is verified. Now you can login  ")
                        .build());
                return "success_page";
            }

            session.setAttribute("message", Message.builder()
                    .type(MessageType.red)
                    .content("Email not verified ! Token is not associated with user .")
                    .build());
            return "error_page";

        }

        session.setAttribute("message", Message.builder()
                .type(MessageType.red)
                .content("Email not verified ! Token is not associated with user .")
                .build());

        return "error_page";
    }

}