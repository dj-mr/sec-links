/**
 * Contains methods that aid REST operations in SECLinks.
 */
package com.probemore.controller.processor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.regex.Pattern;

@Slf4j
@Component
public class SECLinksProcessorImpl implements SECLinksProcessor {

    /**
     * EDGAR URL that is used to fetch data.
     */
    @Value("${edgar.url}")
    private String edgarUrl;

    Pattern quarterNames = Pattern.compile("QTR[1-4]");

    /**
     * EDGAR data is organized by year. Under year, data is
     * organized by quarters
     * @param year - The "Year" folder that must be navigated
     * @return Returns list of QTR-n directories present in given folder
     */
    public long getDirectoryCountInURI(final String year)
            throws IOException {
        log.debug("Fetching EDGAR data from {}{}", edgarUrl, year);

        try {
            URL url = new URL(edgarUrl + year + "/");
            try (BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(url.openStream()))) {
                return bufferedReader
                        .lines()
                        .parallel()
                        .filter(x -> x.contains("QTR"))
                        .peek(log::debug)
                        .count();
            }
        } catch (IOException ioException) {
            log.debug("IOException encountered: {}", ioException.getMessage());
            throw ioException;
        }

    }

    @Override
    public void downloadEdgarUrls(String year, long quarters) throws IOException {

    }

}
