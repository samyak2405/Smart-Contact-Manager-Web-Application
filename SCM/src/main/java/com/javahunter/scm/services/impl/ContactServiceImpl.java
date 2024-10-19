package com.javahunter.scm.services.impl;

import com.javahunter.scm.entity.Contact;
import com.javahunter.scm.entity.User;
import com.javahunter.scm.exception.ResourceNotFoundException;
import com.javahunter.scm.repository.ContactRepository;
import com.javahunter.scm.repository.UserRepository;
import com.javahunter.scm.services.ContactService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {
    private final ContactRepository contactRepository;
    private final UserRepository userRepository;

    @Override
    public Contact save(Contact contact) {
        String uuid = UUID.randomUUID().toString();
        contact.setId(uuid);
        return contactRepository.save(contact);
    }

    @Override
    public Contact updateContact(Contact contact) {
        Contact oldData = contactRepository.findById(contact.getId()).orElseThrow(() -> new ResourceNotFoundException("Contact not found"));
        oldData.setName(contact.getName());
        oldData.setEmail(contact.getEmail());
        oldData.setPhoneNumber(contact.getPhoneNumber());
        oldData.setAddress(contact.getAddress());
        oldData.setDescription(contact.getDescription());
        oldData.setPicture(contact.getPicture());
        oldData.setFavorite(contact.isFavorite());
        oldData.setWebsiteLink(contact.getWebsiteLink());
        oldData.setLinkedInLink(contact.getLinkedInLink());
        oldData.setCloudinaryImagePublicId(contact.getCloudinaryImagePublicId());
        return contactRepository.save(oldData);
    }

    @Override
    public List<Contact> getAll() {
        return contactRepository.findAll();
    }

    @Override
    public Contact getById(String id) {
        return contactRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Contact not found"));
    }

    @Override
    public void delete(String id) {
        Contact contact = contactRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Contact not found"));

        contactRepository.delete(contact);
    }

    @Override
    public List<Contact> search(String name, String email, String phoneNumber) {
        return List.of();
    }

    @Override
    public List<Contact> getByUserId(String userId) {
//        User user = userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User Not found"));
//        return contactRepository.findByUser(user);
        return contactRepository.findByUserId(userId);
    }

    @Override
    public Page<Contact> getByUser(User user, int page, int size, String sortBy, String direction) {
        Sort sort = direction.equals("desc")?Sort.by(sortBy).descending():Sort.by(sortBy).ascending();
        var pageable = PageRequest.of(page, size, sort);
        return contactRepository.findByUser(user, pageable);
    }

    @Override
    public Page<Contact> searchByName(String name, int page, int size, String sortBy, String direction, User user) {
        Sort sort = direction.equals("desc")?Sort.by(sortBy).descending():Sort.by(sortBy).ascending();
        return contactRepository.findByUserAndNameContaining(user,name, PageRequest.of(page,size,sort));
    }

    @Override
    public Page<Contact> searchByEmail(String email, int page, int size, String sortBy, String direction, User user) {
        Sort sort = direction.equals("desc")?Sort.by(sortBy).descending():Sort.by(sortBy).ascending();
        return contactRepository.findByUserAndEmailContaining(user,email, PageRequest.of(page,size,sort));
    }

    @Override
    public Page<Contact> searchByPhoneNumber(String phoneNumber, int page, int size, String sortBy, String direction, User user) {
        Sort sort = direction.equals("desc")?Sort.by(sortBy).descending():Sort.by(sortBy).ascending();
        return contactRepository.findByUserAndPhoneNumberContaining(user,phoneNumber, PageRequest.of(page,size,sort));
    }

    @Override
    public Page<Contact> searchByFavorite(int page, int size, String sortBy, String direction, User user) {
        Sort sort = direction.equals("desc")?Sort.by(sortBy).descending():Sort.by(sortBy).ascending();
        return contactRepository.findByFavorite(user.getUserId(),PageRequest.of(page,size,sort));
    }


}
