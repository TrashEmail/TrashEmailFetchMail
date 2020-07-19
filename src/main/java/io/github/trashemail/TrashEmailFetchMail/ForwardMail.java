package io.github.trashemail.TrashEmailFetchMail;

import io.github.trashemail.TrashEmailFetchMail.Configuration.TrashEmailConfig;
import io.github.trashemail.TrashEmailFetchMail.DTO.Email;
import io.github.trashemail.TrashEmailFetchMail.utils.MailParser;
import io.github.trashemail.TrashEmailFetchMail.utils.ParsedMail;
import io.github.trashemail.TrashEmailFetchMail.utils.SaveMailToHTMLFile;
import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.mail.Message;
import java.util.List;


@Component
@EnableAsync
public class ForwardMail {

    @Autowired
    private TrashEmailConfig trashEmailConfig;

    @Autowired
    private SaveMailToHTMLFile saveMailToHTMLFile;

    @Autowired
    private MailParser mailParser;

    @Autowired
    private RestTemplate restTemplate;

    private static final Logger log = LoggerFactory.getLogger(
            ForwardMail.class);

    public void processAndForward(Message message) throws Exception {
        ParsedMail parsedMail = mailParser.parseMail(message);

        for (String emailFor : parsedMail.getRecipients()) {
            /*
            Send a separate mail for each recipient.
            */

            /*
            If html content is set, offer to save in file and create html link.
            */
            if (parsedMail.getHtmlContentSet()) {
                Object filename = saveMailToHTMLFile.saveToFile(
                        parsedMail.getHtmlContent());

                /*
                If mail has attachments.
                */
                if (parsedMail.getAttachmentSet()) {
                    if (filename != null)
                        forwardToTrashEmailService(
                                parsedMail.toString(),
                                emailFor,
                                (String) filename,
                                parsedMail.getAttachmentList());

                    else {
                        forwardToTrashEmailService(
                                parsedMail.toString(),
                                emailFor,
                                null,
                                parsedMail.getAttachmentList());
                    }
                }
                else{
                    if (filename != null)
                        forwardToTrashEmailService(
                                parsedMail.toString(),
                                emailFor,
                                (String) filename,
                                null);

                    else {
                        forwardToTrashEmailService(
                                parsedMail.toString(),
                                emailFor,
                                null,
                                null);
                    }
                }
            } else {
                if (!parsedMail.getAttachmentSet()) {
                    forwardToTrashEmailService(
                            parsedMail.toString(),
                            emailFor,
                            null,
                            null);
                }
                if (parsedMail.getAttachmentSet()) {
                    forwardToTrashEmailService(
                            parsedMail.toString(),
                            emailFor,
                            null,
                            parsedMail.attachmentList
                    );
                }
            }
        }
    }

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
            log.debug(String.format("Message sent to recipient %s", recipient) );
        } else{
            log.error(String.format("Unable to send the message to recipient %s", recipient));
        }
    }
}

