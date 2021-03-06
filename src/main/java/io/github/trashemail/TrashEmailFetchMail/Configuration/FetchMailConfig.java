package io.github.trashemail.TrashEmailFetchMail.Configuration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@Configuration
@ConfigurationProperties("fetch-mail")
public class FetchMailConfig {
    private Imap imap;
    private Emails emails;

    @Getter
    @Setter
    @NoArgsConstructor
    @Configuration
    @ConfigurationProperties("imap")
    public static class Imap{
        private String host;
        private String port;
        private List<String> emails;
        private List<String> passwords;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @Configuration
    @ConfigurationProperties("emails")
    public static class Emails{
        private String hostPath;
        private String downloadPath;
    }
}
