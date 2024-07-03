package org.store.clothstar.common.mail;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Disabled
class MailServiceTest {
    @Autowired
    MailService mailService;
    @Autowired
    MailContentBuilder mailContentBuilder;

    @DisplayName("메일 전송 테스트")
    @Test
    void mailSendTest() {
        //given
        String link = "https://www.naver.com/";
        String message = mailContentBuilder.build(link);
        MailSendDTO mailSendDTO = new MailSendDTO("test@test.com", "test", message);

        //when
        Boolean success = mailService.sendMail(mailSendDTO);

        //then
        Assertions.assertThat(success).isTrue();
    }
}