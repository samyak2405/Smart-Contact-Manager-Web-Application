package com.javahunter.scm.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ContactSearchRequest {

    private String field;
    private String value;
}
