package com.example.progettoSS.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
public class LandingController{

    @GetMapping("/")
    public String landingPage(){
        return "landingpage";
    }

}