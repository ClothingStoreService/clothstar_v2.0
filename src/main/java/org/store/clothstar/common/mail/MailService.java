package org.store.clothstar.common.mail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;

    @Value("${email.send}")
    private String fromAddress;

    public boolean sendMail(MailSendDTO mailSendDTO) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            messageHelper.setFrom(fromAddress);
            messageHelper.setTo(mailSendDTO.getAddress());
            messageHelper.setSubject(mailSendDTO.getSubject());
            messageHelper.setText(mailSendDTO.getText(), true);
        };
        log.error("전송 메일주소 : {} -> {}", fromAddress, mailSendDTO.getAddress());

        mailSender.send(messagePreparator);

        return true;
    }
}
