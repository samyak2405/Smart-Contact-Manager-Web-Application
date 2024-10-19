package com.javahunter.scm.services;

import com.javahunter.scm.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User saveUser(User user);
    Optional<User> getByUserId(String userId);
    Optional<User> updateUser(User user);
    void deleteUser(String userId);
    boolean isUserExist(String userId);
    boolean isUserExistByEmail(String emailId);
    List<User> getAllUsers();
    User getUserByEmail(String emailId);

    User findByEmailToken(String token);
}
