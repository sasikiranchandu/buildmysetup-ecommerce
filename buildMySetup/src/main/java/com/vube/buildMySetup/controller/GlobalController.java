package com.vube.buildMySetup.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GlobalController {

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";
    }
}