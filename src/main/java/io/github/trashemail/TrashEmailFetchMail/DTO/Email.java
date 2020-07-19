package io.github.trashemail.TrashEmailFetchMail.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Email {
    private String emailId;
    private String message;
    private String emailURI;
    private String emailDownloadPath;
    private List<String> attachmentsPaths;

    @Override public String toString() {
        return "Email{" +
                "emailId='" + emailId + '\'' +
                ", message='" + message + '\'' +
                ", emailHostingLocation='" + emailURI + '\'' +
                ", emailDownloadPath='" + emailDownloadPath + '\'' +
                ", attachmentsPaths=" + attachmentsPaths +
                '}';
    }
}
