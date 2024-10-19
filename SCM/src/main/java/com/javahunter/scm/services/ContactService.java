package com.javahunter.scm.services;

import com.javahunter.scm.entity.Contact;
import com.javahunter.scm.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ContactService {

    Contact save(Contact contact);
    Contact updateContact(Contact contact);
    List<Contact> getAll();
    Contact getById(String id);
    void delete(String id);
    List<Contact> search(String name, String email, String phoneNumber);
    List<Contact> getByUserId(String userId);
    Page<Contact> getByUser(User user, int page, int size, String sortBy, String direction);

    Page<Contact> searchByName(String name, int page, int size, String sortBy, String direction, User user);
    Page<Contact> searchByEmail(String email, int page, int size, String sortBy, String direction, User user);
    Page<Contact> searchByPhoneNumber(String phoneNumber, int page, int size, String sortBy, String direction, User user);
    Page<Contact> searchByFavorite(int page, int size, String sortBy, String direction, User user);
}
