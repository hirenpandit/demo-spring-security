package com.example.demospringsecurity;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    @GetMapping("/{name}")
    public ResponseEntity<String> greet(@PathVariable String name) {
        return ResponseEntity.ok("Hello, "+name);
    }

}
