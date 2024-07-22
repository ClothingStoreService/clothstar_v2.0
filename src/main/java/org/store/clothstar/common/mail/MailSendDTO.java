package org.store.clothstar.common.mail;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MailSendDTO {
    private String address;
    private String subject;
    private String text;
}
