package com.javahunter.scm.controller;

import com.javahunter.scm.dto.ContactRequest;
import com.javahunter.scm.dto.ContactSearchRequest;
import com.javahunter.scm.dto.Message;
import com.javahunter.scm.entity.Contact;
import com.javahunter.scm.entity.User;
import com.javahunter.scm.enums.MessageType;
import com.javahunter.scm.helper.AppConstants;
import com.javahunter.scm.helper.Helper;
import com.javahunter.scm.services.ContactService;
import com.javahunter.scm.services.ImageService;
import com.javahunter.scm.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.Banner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Slf4j
@Controller
@RequestMapping("/user/contacts")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;
    private final UserService userService;
    private final ImageService imageService;
    //add Contact page handle
    @GetMapping("/add")
    public String addContactView(Model model){
        ContactRequest contactRequest = new ContactRequest();
        ContactSearchRequest contactSearchRequest = new ContactSearchRequest();
        model.addAttribute("contactRequest", contactRequest);
        model.addAttribute("contactSearchRequest", contactSearchRequest);
        return "user/add_contact";
    }

    @PostMapping("/process-contact")
    public String addContact(@Valid @ModelAttribute ContactRequest contactRequest , BindingResult bindingResult, Authentication authentication, HttpSession session){
        log.info("add contact {}", contactRequest.toString());
        Message message = new Message();
        log.info("file information: {}",contactRequest.getContactImage().getOriginalFilename());
        log.info("Contact Request: {}", contactRequest.toString());
        //Validate form
        //TODO: Add Validation Logic
        if(bindingResult.hasErrors()){
            bindingResult.getAllErrors().forEach(error -> {log.info(error.toString());});
            message = Message.builder()
                    .type(MessageType.red)
                    .content("Please resolve the errors")
                    .build();
            session.setAttribute("message", message);
            return "user/add_contact";
        }



        String username = Helper.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(username);


        Contact contact = new Contact();
        contact.setName(contactRequest.getName());
        contact.setEmail(contactRequest.getEmail());
        contact.setPhoneNumber(contactRequest.getPhoneNumber());
        contact.setAddress(contactRequest.getAddress());
        contact.setDescription(contactRequest.getDescription());
        contact.setFavorite(contactRequest.isFavorite());
        contact.setLinkedInLink(contactRequest.getLinkedInLink());
        contact.setWebsiteLink(contactRequest.getWebsiteLink());
        contact.setUser(user);

        if(contactRequest.getContactImage()!=null && !contactRequest.getContactImage().isEmpty()){
            String fileName = UUID.randomUUID().toString();
            String fileUrl = imageService.uploadImage(contactRequest.getContactImage(), fileName);
            contact.setPicture(fileUrl);
            contact.setCloudinaryImagePublicId(fileName);
        }
        contactService.save(contact);

        message = Message.builder()
                .content("Added Contact Successful")
                .type(MessageType.green)
                .build();
        session.setAttribute("message",message);
        return "redirect:/user/contacts/add";
    }

    @GetMapping
    public String viewContacts(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            Authentication authentication, Model model){
        log.info("view contacts");
        String username = Helper.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(username);
        Page<Contact> contacts = contactService.getByUser(user, page, size, sortBy, direction);
        model.addAttribute("pageSize", AppConstants.PAGE_SIZE);
        model.addAttribute("contacts", contacts);
        ContactSearchRequest contactSearchRequest = new ContactSearchRequest();
        model.addAttribute("contactSearchRequest", contactSearchRequest);
        return "user/contacts";
    }

    @RequestMapping("/search")
    public String searchHandler(@ModelAttribute ContactSearchRequest contactSearchRequest,
                                @RequestParam(value = "page", defaultValue = "0") int page,
                                @RequestParam(value = "size", defaultValue = "5") int size,
                                @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
                                @RequestParam(value = "direction", defaultValue = "asc") String direction,
                                Authentication authentication, Model model, Sort sort){
        String field = contactSearchRequest.getField();
        String value = contactSearchRequest.getValue();
        log.info("Field: {}, Value: {}", field, value);
        var user = userService.getUserByEmail(Helper.getEmailOfLoggedInUser(authentication));
//        contactService.searchWith(field,value);
        Page<Contact> contacts = null;
        if(field.equalsIgnoreCase("name")){
           contacts = contactService.searchByName(value, page, size, sortBy, direction, user);
        }else if(field.equalsIgnoreCase("phoneNumber")){
            contacts = contactService.searchByPhoneNumber(value, page, size, sortBy, direction, user);
        }else if(field.equalsIgnoreCase("email")){
            contacts = contactService.searchByEmail(value, page, size, sortBy, direction, user);
        }else if(field.equalsIgnoreCase("favorite")){
            contacts = contactService.searchByFavorite(page, size, sortBy, direction, user);
        }else if(field.equalsIgnoreCase("all")){
            contacts = contactService.getByUser(user, page, size, sortBy, direction);
        }
        else{
            log.info("Unknown field: {}", field);
        }
        model.addAttribute("pageSize", AppConstants.PAGE_SIZE);
        model.addAttribute("contacts", contacts);
        model.addAttribute("contactSearchRequest", contactSearchRequest);
        return "user/search";
    }

    //Delete Contact
    @GetMapping("/delete/{contactId}")
    public String delete(@PathVariable("contactId") String contactId, HttpSession session){
        contactService.delete(contactId);
        session.setAttribute("message",Message.builder()
                        .content("Contact is Deleted Successfully")
                        .type(MessageType.red)
                .build());
        return "redirect:/user/contacts";
    }

    @GetMapping("/view/{contactId}")
    public String updateContactFormView(@PathVariable("contactId") String contactId, Model model){

        var contact = contactService.getById(contactId);
        ContactRequest contactRequest = new ContactRequest();
        contactRequest.setName(contact.getName());
        contactRequest.setEmail(contact.getEmail());
        contactRequest.setPhoneNumber(contact.getPhoneNumber());
        contactRequest.setAddress(contact.getAddress());
        contactRequest.setDescription(contact.getDescription());
        contactRequest.setFavorite(contact.isFavorite());
        contactRequest.setLinkedInLink(contact.getLinkedInLink());
        contactRequest.setImageUrl(contact.getPicture());
        contactRequest.setWebsiteLink(contact.getWebsiteLink());
        model.addAttribute("contactRequest", contactRequest);
        model.addAttribute("contactId", contactId);
        return "user/update_contact_view";
    }

    @PostMapping("/update/{contactId}")
    public String updateContact(@PathVariable("contactId") String contactId,@Valid @ModelAttribute ContactRequest contactRequest, BindingResult bindingResult, HttpSession session){

        if(bindingResult.hasErrors()){
            bindingResult.getAllErrors().forEach(error -> {log.info(error.toString());});
            Message message = Message.builder()
                    .type(MessageType.red)
                    .content("Please resolve the errors")
                    .build();
            session.setAttribute("message", message);
            return "user/update_contact_view";
        }
        Contact contact = contactService.getById(contactId);
        contact.setId(contactId);
        contact.setName(contactRequest.getName());
        contact.setEmail(contactRequest.getEmail());
        contact.setPhoneNumber(contactRequest.getPhoneNumber());
        contact.setAddress(contactRequest.getAddress());
        contact.setDescription(contactRequest.getDescription());
        contact.setFavorite(contactRequest.isFavorite());
        contact.setLinkedInLink(contactRequest.getLinkedInLink());
        contact.setWebsiteLink(contactRequest.getWebsiteLink());
        //process image
        if(contactRequest.getContactImage() != null && !contactRequest.getContactImage().isEmpty()){
            log.info("File is not empty");
            String fileName = UUID.randomUUID().toString();
            String imageUrl = imageService.uploadImage(contactRequest.getContactImage(), fileName);
            contact.setCloudinaryImagePublicId(fileName);
            contact.setPicture(imageUrl);
            contactRequest.setImageUrl(imageUrl);

        }else{
            log.info("File is empty");
        }
         contactService.updateContact(contact);
         session.setAttribute("message", Message.builder()
                 .type(MessageType.green)
                 .content("Contact Updated Successfully")
                 .build());
        return "redirect:/user/contacts/view/"+contactId;
    }

}
