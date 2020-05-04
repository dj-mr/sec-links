package com.probemore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class HomeController {

    /**
     * Home page for this application.
     * @return name of the html file that is invoked.
     */
    @GetMapping
    public String home() {
        return "home";
    }

}
