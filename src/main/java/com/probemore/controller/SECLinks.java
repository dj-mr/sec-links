/**
 * Controller for SEC Rest API.
 */
package com.probemore.controller;

import com.probemore.controller.processor.SECLinksProcessor;
import com.probemore.model.SECData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/sec",
                produces = "application/json")
@Tag(name = "SECLinks", description = "SEC Links API")
@CrossOrigin(origins = "*")
@Slf4j
public class SECLinks {

    /**
     * Dependency Injection for SECLinksProcessor.
     */
    @Autowired
    private SECLinksProcessor secLinksProcessor;

    /**
     * Method that handles GET by filters for this controller.
     * @param cik that is used to filter data
     * @param formname that is used to filter data
     * @param filingdate that is used to filter data
     * @param offset number of initial records to skip
     * @param length number of records to fetch. Defaulted to 500
     * @return Available SEC data
     */
    @Operation(summary = "GET SEC Links filtered by params if provided",
            description = "Get SEC Links filtered by params if provided",
            tags = {"sec"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successful Return of data"),
            @ApiResponse(responseCode = "204",
                    description = "Content was not found")
    })
    @GetMapping
    public List<SECData> getFilteredUrls(
            @RequestParam final Optional<String>     cik,
            @RequestParam final Optional<String>     formname,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                    final Optional<LocalDate>  filingdate,
            @RequestParam final Optional<Integer>    offset,
            @RequestParam final Optional<Integer>    length
    ) {
        return secLinksProcessor.getFilteredUrls(
                cik,
                formname,
                filingdate,
                offset,
                length
        );
    }

    /**
     * Method that handles PUT request when no parameters are provided.
     */
    @Operation(summary = "Refresh SEC links data",
            description = "Refresh SEC Links",
            tags = {"sec"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successful refresh operation"),
            @ApiResponse(responseCode = "401",
                    description = "User not authorized to perform request")
    })
    @PutMapping
    public void refreshDatabase() {
        try {
            secLinksProcessor.downloadEdgarUrls();
        } catch (IOException ioException) {
            log.debug("Exception encountered refreshing data. {}", ioException);
        }
    }


    /**
     * Method that handles PUT request for this controller.
     * This is executed when "year" is passed as parameter.
     * @param year - Year for which data must be retrieved.
     */
    @Operation(summary = "Refresh SEC links data for given year",
               description = "Refresh SEC Links for given year",
               tags = {"sec"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                         description = "Successful refresh operation"),
            @ApiResponse(responseCode = "401",
                         description = "User not authorized to perform request")
    })
    @PutMapping(value = {"/{year}"})
    public void refreshDatabase(@PathVariable("year") final String year) {

        try {
            long numDirPresent = secLinksProcessor.getDirectoryCountInURI(year);
            for (long quarter = 1; quarter < numDirPresent + 1; quarter++) {
                secLinksProcessor.downloadEdgarUrls(year, quarter);
            }
        } catch (IOException ioException) {
            log.debug("Exception encountered refreshing data. {}", ioException);
        }

    }

}
