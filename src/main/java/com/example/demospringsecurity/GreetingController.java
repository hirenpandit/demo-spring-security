package com.example.demospringsecurity;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/greeting")
public class GreetingController {

    @GetMapping("/admin/{name}")
    public ResponseEntity<String> greetAdmin(@PathVariable String name) {
        return ResponseEntity.ok("Greeting Admin, "+name);
    }


    @GetMapping("/user/{name}")
    public ResponseEntity<String> greetUser(@PathVariable String name) {
        return ResponseEntity.ok("Greeting User, "+name);
    }



}
