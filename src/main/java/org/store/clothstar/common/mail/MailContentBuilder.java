package org.store.clothstar.common.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


@Service
@RequiredArgsConstructor
public class MailContentBuilder {
    private final TemplateEngine templateEngine;

    public String build(String certifyNum) {
        Context context = new Context();
        context.setVariable("certifyNum", certifyNum);
        return templateEngine.process("mailTemplate", context);
    }
}