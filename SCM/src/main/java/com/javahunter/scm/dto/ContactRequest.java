package com.javahunter.scm.dto;

import com.javahunter.scm.validators.ValidFile;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ContactRequest {

    @NotBlank(message = "name is required")
    @Size(min = 3, message = "Min 3 Characters is required")
    private String name;
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid Email Address")
    private String email;
    @Pattern(regexp = "^[0-9]{10}$", message = "Invalid Phone Number")
    private String phoneNumber;
    @NotBlank(message = "Address is required")
    private String address;
    @NotBlank(message = "Description is required")
    private String description;
    private boolean favorite;
    private String websiteLink;
    private String linkedInLink;
    @ValidFile(message = "Invalid File Size")
    private MultipartFile contactImage;
    private String imageUrl;
}