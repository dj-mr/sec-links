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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(path = "/sec",
                produces = "application/json")
@Tag(name = "SECLinks", description = "SEC Links API")
@CrossOrigin(origins = "*")
@Slf4j
public class SECLinks {

    @Autowired
    private SECLinksProcessor secLinksProcessor;

    /**
     * Method that handles GET request for this controller.
     * @return Available SEC data
     */
    @Operation(summary = "GET all SEC Links",
               description = "Get all SEC Links stored in local database",
               tags = {"sec"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                         description = "Successful Return of data"),
            @ApiResponse(responseCode = "204",
                         description = "Content was not found")
    })
    @GetMapping
    public List<SECData> getAll() {
        // TODO - Add Business Logic here
        return Arrays.asList();
    }


    /**
     * Method that handles PUT request for this controller.
     */
    @Operation(summary = "Refresh SEC links data",
               description = "Refresh SEC Links from SEC database",
               tags = {"sec"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                         description = "Successful refresh operation"),
            @ApiResponse(responseCode = "401",
                         description = "User not authorized to perform request")
    })
    @PutMapping(value = {"/{year}"})
    public void refreshDatabase(@PathVariable("year") String year) {
        // TODO - Add Business Logic here
        log.debug(""); // TODO - write better logs
        secLinksProcessor.getDirectoriesInURI(year);

    }

}
