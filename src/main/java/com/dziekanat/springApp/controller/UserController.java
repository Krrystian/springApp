package com.dziekanat.springApp.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @GetMapping("/getAll")
    public String getUsers(){
        return "Not secured";
    }
    @GetMapping("getAdmin")
    public String getAdmins(@AuthenticationPrincipal UserDetails userDetails){
        return "Secured, welcome " + userDetails.getUsername();
    }
}
