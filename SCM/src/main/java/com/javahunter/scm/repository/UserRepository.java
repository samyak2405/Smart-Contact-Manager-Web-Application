package com.javahunter.scm.repository;

import com.javahunter.scm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String emailId);
    Optional<User> findByEmailAndPassword(String emailId, String password);

    Optional<User> findByEmailToken(String token);
}
