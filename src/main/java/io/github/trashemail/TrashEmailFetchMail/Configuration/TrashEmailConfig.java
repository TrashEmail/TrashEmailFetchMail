package io.github.trashemail.TrashEmailFetchMail.Configuration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
@ConfigurationProperties("trashemail")
@Getter
@Setter
@NoArgsConstructor
public class TrashEmailConfig {
    private String uri;
    private String path;
}
