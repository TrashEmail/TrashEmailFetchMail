package io.github.trashemail.TrashEmailFetchMail.utils;

import io.github.trashemail.TrashEmailFetchMail.Configuration.FetchMailConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.util.UUID;

@Component
public class SaveMailToHTMLFile {
    @Autowired
    private FetchMailConfig fetchMailConfig;

    private static final Logger log = LoggerFactory.getLogger(
            SaveMailToHTMLFile.class);

    private static String safeHTMLCSPPolicy = "<meta http-equiv=\"Content-Security-Policy\" content=\"default-src 'self';style-src 'unsafe-inline';img-src 'self' data:\">";

    public Object saveToFile(String htmlContent){
        try {
            String filename = UUID.randomUUID().toString() + ".html";

            FileWriter myWriter = new FileWriter(
                    fetchMailConfig.getEmails().getDownloadPath() + filename);

            myWriter.write(safeHTMLCSPPolicy + htmlContent);
            myWriter.close();

            log.debug("File written to disk: "+ filename);
            return filename;
        }
        catch (Exception e) {
            log.error("Unable to save to HTML file. " + e.getMessage());
            return null;
        }
    }
}
