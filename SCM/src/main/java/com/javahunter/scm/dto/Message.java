package com.javahunter.scm.dto;

import com.javahunter.scm.enums.MessageType;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private String content;

    @Builder.Default
    private MessageType type = MessageType.blue;
}
