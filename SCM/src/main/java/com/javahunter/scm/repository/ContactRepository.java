package com.javahunter.scm.repository;

import com.javahunter.scm.entity.Contact;
import com.javahunter.scm.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact, String> {
    Page<Contact> findByUser(User user, Pageable pageable);

    @Query("SELECT c FROM Contact c WHERE c.user.userId = :userId")
    List<Contact> findByUserId(@Param("userId") String userId);

    Page<Contact> findByUserAndNameContaining(User user, String email, Pageable pageable);

    Page<Contact> findByUserAndEmailContaining(User user,String name, Pageable pageable);
    Page<Contact> findByUserAndPhoneNumberContaining(User user,String phoneNumber, Pageable pageable);

    @Query("SELECT c FROM Contact c WHERE c.user.userId = :userId and c.favorite=true")
    Page<Contact> findByFavorite(@Param("userId") String userId,Pageable pageable);
}
