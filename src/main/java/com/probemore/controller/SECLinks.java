/**
 * Controller for SEC Rest API
 */
package com.probemore.controller;

import com.probemore.model.SECData;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/sec",
                produces = "application/json")
@CrossOrigin(origins = "*")
public class SECLinks {

    /**
     * Method that handles GET request for this controller
     * @return Available SEC data
     */
    @GetMapping
    public Iterable<SECData> secLinks() {
        // TODO - Add Business Logic here
        return null;
    }

}
