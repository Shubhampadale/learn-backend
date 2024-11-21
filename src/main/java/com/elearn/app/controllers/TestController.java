package com.elearn.app.controllers;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    //this controller we are using for testing method level security

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String test(){

        return "Testing the Method level Security";
    }
}
