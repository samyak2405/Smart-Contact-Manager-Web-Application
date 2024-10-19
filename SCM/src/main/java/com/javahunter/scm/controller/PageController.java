package com.javahunter.scm.controller;

import com.javahunter.scm.dto.Message;
import com.javahunter.scm.dto.UserRequest;
import com.javahunter.scm.entity.User;
import com.javahunter.scm.enums.MessageType;
import com.javahunter.scm.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PageController {

    private final UserService userService;

    @GetMapping({"/home","/",""})
    public String home(Model model) {
        log.info("home page handler");
        model.addAttribute("name","SAMYAK MOON");
        model.addAttribute("email","samyak@gmail.com");
        model.addAttribute("password","123456");
        model.addAttribute("github","https://github.com/samyak2405");
        return "home";
    }

    @GetMapping("/about")
    public String aboutPage(){
        log.info("About Page Loading");
        return "about";
    }

    @GetMapping("/services")
    public String servicesPage(){
        log.info("Services Page Loading");
        return "services";
    }

    @GetMapping("/contact")
    public String contactPage(){
        log.info("Contact Page Loading");
        return "contact";
    }

    @GetMapping("/login")
    public String loginPage(){
        log.info("Login Page Loading");
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model){
        log.info("Register Page Loading");
        UserRequest userRequest = new UserRequest();
        model.addAttribute("userRequest", userRequest);
        return "register";
    }

    @PostMapping("/do-register")
    public String processRegister(@Valid @ModelAttribute UserRequest userRequest, BindingResult bindingResult, HttpSession session){
        log.info("Processing User Registration: {}", userRequest.toString());
        //fetch form data
//        User user = User.builder()
//                .name(userRequest.getName())
//                .email(userRequest.getEmail())
//                .password(userRequest.getPassword())
//                .about(userRequest.getAbout())
//                .phoneNumber(userRequest.getPhoneNumber())
////                .profilePic()
//                .build();
        User user = new User();
        user.setName(userRequest.getName());
        user.setEmail(userRequest.getEmail());
        user.setPassword(userRequest.getPassword());
        user.setAbout(userRequest.getAbout());
        user.setPhoneNumber(userRequest.getPhoneNumber());
        user.setEnabled(false);
        //Validate form data
        if(bindingResult.hasErrors()){
            return "register";
        }
        //save to database
        User savedUser = userService.saveUser(user);
        log.info("Saved User : {}", savedUser.toString());
        //message = "Registration Successful"
        Message message = Message.builder()
                .content("Registration Successful")
                .type(MessageType.green)
                .build();
        session.setAttribute("message",message);
        //Redirect to login page
        return "redirect:/register";
    }
}
