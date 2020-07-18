package io.github.trashemail.TrashEmailFetchMail;

import io.github.trashemail.TrashEmailFetchMail.Configuration.TrashEmailConfig;
import io.github.trashemail.TrashEmailFetchMail.utils.MailParser;
import io.github.trashemail.TrashEmailFetchMail.utils.ParsedMail;
import io.github.trashemail.TrashEmailFetchMail.utils.SaveMailToHTMLFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.mail.Message;


@Component
public class ForwardMailsToTelegram {

    @Autowired
    private TrashEmailConfig trashEmailConfig;
    @Autowired
    private SaveMailToHTMLFile saveMailToHTMLFile;

    @Autowired
    private SendMessage sendMessage;

    @Autowired
    private MailParser mailParser;

    private static final Logger log = LoggerFactory.getLogger(
            ForwardMailsToTelegram.class);

    public void sendToTelegram(Message message) throws Exception {
        ParsedMail parsedMail = mailParser.parseMail(message);

        for (String emailFor : parsedMail.getRecipients()) {
            /*
            Send a separate mail for each recipient.
            */

            /*
            If html content is set, offer to save in file and show html link.
            */
            if (parsedMail.getHtmlContentSet()) {
                Object filename = saveMailToHTMLFile.saveToFile(
                        parsedMail.getHtmlContent());

                if (parsedMail.getAttachmentSet()) {
                    if (filename != null)
                        sendMessage.forwardToTrashEmailService(
                                parsedMail.toString(),
                                emailFor,
                                (String) filename,
                                null);

                    else {
                        sendMessage.forwardToTrashEmailService(
                                parsedMail.toString(),
                                emailFor,
                                null,
                                null);
                    }
                }
                else{
                    if (filename != null)
                        sendMessage.forwardToTrashEmailService(
                                parsedMail.toString(),
                                emailFor,
                                (String) filename,
                                parsedMail.attachmentList);

                    else {
                        sendMessage.forwardToTrashEmailService(
                                parsedMail.toString(),
                                emailFor,
                                null,
                                parsedMail.attachmentList);
                    }
                }
            } else {
                if (!parsedMail.getAttachmentSet()) {
                    sendMessage.forwardToTrashEmailService(
                            parsedMail.toString(),
                            emailFor,
                            null,
                            null);
                }
                if (parsedMail.getAttachmentSet()) {
                    sendMessage.forwardToTrashEmailService(
                            parsedMail.toString(),
                            emailFor,
                            null,
                            parsedMail.attachmentList
                    );
                }
            }
        }
    }
}

