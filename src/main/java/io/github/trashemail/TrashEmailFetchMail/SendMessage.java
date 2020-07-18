package io.github.trashemail.TrashEmailFetchMail;

import io.github.trashemail.TrashEmailFetchMail.Configuration.TrashEmailConfig;
import io.github.trashemail.TrashEmailFetchMail.DTO.Email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.apache.commons.text.StringEscapeUtils;

import java.util.List;


@Component
@EnableAsync
public class SendMessage {
    @Autowired
    private TrashEmailConfig trashEmailConfig;

    @Autowired
    private RestTemplate restTemplate;

    private static final Logger log = LoggerFactory.getLogger(
            SendMessage.class);


    @Async
    public void forwardToTrashEmailService(String message, String recipient, String emailUri,
                                           List<String> attachments) {
        String trashEmailURI = trashEmailConfig.getUri();

        String escapedMessage = StringEscapeUtils.escapeHtml4(message);

        Email email = new Email();
        email.setMessage(escapedMessage);
        email.setEmailId(recipient);
        email.setEmailHostingLocation(emailUri);
        email.setAttachmentsPaths(attachments);

        ResponseEntity response = restTemplate.postForEntity(
                trashEmailURI,
                email,
                Object.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            log.debug(String.format("Message sent to user %s", recipient) );
        } else{
            log.error(String.format("Unable to send the message to user %s", recipient));
        }
    }
}