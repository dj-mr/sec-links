/**
 * Controller for SEC Rest API.
 */
package com.probemore.controller;

import com.probemore.model.SECData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/sec",
                produces = "application/json")
@Tag(name = "SECLinks", description = "SEC Links API")
@CrossOrigin(origins = "*")
public class SECLinks {

    /**
     * Method that handles GET request for this controller.
     * @return Available SEC data
     */
    @Operation(summary = "GET all SEC Links",
               description = "Get all SEC Links stored in local database",
               tags = {"sec"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Return of data"),
            @ApiResponse(responseCode = "204", description = "Content was not found")
    })
    @GetMapping
    public Iterable<SECData> secLinks() {
        // TODO - Add Business Logic here
        return null;
    }

}
