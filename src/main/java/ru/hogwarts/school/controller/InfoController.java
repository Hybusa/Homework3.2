package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InfoController {



    @GetMapping("getPort")
    public ResponseEntity<String> getPort(){
        return ResponseEntity.ok("port is: " );
    }
}
