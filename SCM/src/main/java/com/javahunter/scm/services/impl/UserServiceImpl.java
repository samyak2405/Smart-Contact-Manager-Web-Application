package com.javahunter.scm.services.impl;

import com.javahunter.scm.entity.User;
import com.javahunter.scm.exception.ResourceNotFoundException;
import com.javahunter.scm.helper.AppConstants;
import com.javahunter.scm.helper.Helper;
import com.javahunter.scm.repository.UserRepository;
import com.javahunter.scm.services.EmailService;
import com.javahunter.scm.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Override
    public User saveUser(User user) {
        //user id: have to generate
        String uuid = UUID.randomUUID().toString();
        user.setUserId(uuid);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoleList(List.of(AppConstants.ROLE_USER));

        String emailToken = UUID.randomUUID().toString();
        String emailLink = Helper.getLinkForEmailVerification(emailToken);
        user.setEmailToken(emailToken);
        emailService.sendEmail(user.getEmail(), "Verify Email: Email Smart Contact Manager",emailLink);
        return userRepository.save(user);
    }

    @Override
    public Optional<User> getByUserId(String userId) {
        return userRepository.findById(userId);
    }

    @Override
    public Optional<User> updateUser(User user) {
        User savedUser = userRepository.findById(user.getUserId()).orElseThrow(()->new ResourceNotFoundException("User Not found"));
        savedUser.setName(user.getName());
        savedUser.setEmail(user.getEmail());
        savedUser.setPassword(user.getPassword());
        savedUser.setAbout(user.getAbout());
        savedUser.setPhoneNumber(user.getPhoneNumber());
        savedUser.setProfilePic(user.getProfilePic());
        savedUser.setEnabled(user.isEnabled());
        savedUser.setEmailVerified(user.isEmailVerified());
        savedUser.setPhoneVerified(user.isPhoneVerified());
        savedUser.setProvider(user.getProvider());
        savedUser.setProviderUserId(user.getProviderUserId());
        User updatedUser = userRepository.save(savedUser);
        return Optional.of(updatedUser);
    }

    @Override
    public void deleteUser(String userId) {
        User savedUser = userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User Not found"));
        userRepository.delete(savedUser);
    }

    @Override
    public boolean isUserExist(String userId) {
        User savedUser = userRepository.findById(userId).orElse(null);
        return savedUser != null;
    }

    @Override
    public boolean isUserExistByEmail(String emailId) {
        User savedUser = userRepository.findByEmail(emailId).orElse(null);
        return savedUser != null;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserByEmail(String emailId) {
        return userRepository.findByEmail(emailId).orElse(null);

    }

    @Override
    public User findByEmailToken(String token) {
        return userRepository.findByEmailToken(token).orElse(null);
    }
}
