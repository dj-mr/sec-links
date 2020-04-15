/**
 * Contains methods that aid REST operations in SECLinks.
 */
package com.probemore.controller.processor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class SECLinksProcessorImpl implements SECLinksProcessor {

    /**
     * EDGAR URL that is used to fetch data.
     */
    @Value("${edgar.url}")
    private String edgarUrl;


    /**
     * EDGAR data is organized by year. Under year, data is
     * organized by quarters
     * @param year - The "Year" folder that must be navigated
     * @return Returns list of QTR-n directories present in given folder
     */
    public List<String> getDirectoriesInURI(String year) {
        log.debug("EDGAR URL is {}{}", edgarUrl, year);
        return null;
    }

}
