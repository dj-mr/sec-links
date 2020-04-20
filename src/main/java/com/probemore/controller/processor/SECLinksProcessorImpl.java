/**
 * Contains methods that aid REST operations in SECLinks.
 */
package com.probemore.controller.processor;

import com.probemore.model.SECData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
public class SECLinksProcessorImpl implements SECLinksProcessor {

    /**
     * EDGAR URL that is used to fetch data.
     */
    @Value("${edgar.url}")
    private String edgarUrl;

    @Value("${edgar.prefix}")
    private String edgarPrefix;

    /**
     * EDGAR data is organized by year. Under year, data is
     * organized by quarters
     *
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
            log.debug("IOException encountered: {}", ioException);
            throw ioException;
        }

    }

    @Override
    public void downloadEdgarUrls(String year, long quarters) throws IOException {

        List<SECData> edgarUrlsOfInterest = new ArrayList<SECData>();
        Function<String, SECData> splitEdgarRowIntoFields = row -> new SECData(
                 row.substring(0, 62).trim()   //Company Description
                ,row.substring(62, 74).trim()  // Form Name
                ,row.substring(74, 86).trim()  // CIK Number
                ,LocalDate.parse(row.substring(86, 96).trim())  // Filing Date
                ,edgarPrefix + row.substring(96).trim()      // EDGAR content relative URL
        );

        for (int i = 1; i <= quarters; i++) {
            try {
                URL url = new URL(edgarUrl + year + "/QTR" + i + "/company.idx");
                try (BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(url.openStream()))) {
                    edgarUrlsOfInterest.addAll(
                            bufferedReader
                            .lines()
                            .parallel()
                            .filter(x -> x.contains("10-Q")
                                    || x.contains("10-K")
                                    || x.contains("10-Q/A")
                                    || x.contains("10-K/A"))
                            .map(splitEdgarRowIntoFields)
                            .peek(x -> log.debug(x.toString()))
                            .collect(Collectors.toList())
                    );
                }
            } catch (IOException ioException) {
                log.debug("IOException encountered: {}", ioException);
                throw ioException;
            }
        }
    }

}
