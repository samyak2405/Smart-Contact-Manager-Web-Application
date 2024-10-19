package com.javahunter.scm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Contact {

    @Id
    private String id;
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private String picture;
    private String description;
    private boolean favorite = false;
    private String websiteLink;
    private String linkedInLink;
    @ManyToOne
    @JsonIgnore
    private User user;
    private String cloudinaryImagePublicId;

    @OneToMany(mappedBy = "contact",cascade = CascadeType.ALL, fetch = FetchType.EAGER ,orphanRemoval = true)
    private List<SocialLinks> socialLinks = new ArrayList<>();
}
