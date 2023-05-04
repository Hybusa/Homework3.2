package ru.hogwarts.school.controller;

import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Stream;

@RestController
public class InfoController {

    private final Environment environment;

    public InfoController(Environment environment) {
        this.environment = environment;
    }

    @GetMapping("getPort")
    public ResponseEntity<String> getPort() {
        return ResponseEntity.ok("port is: " + environment.getProperty("local.server.port"));
    }

    @GetMapping("formula")
    public ResponseEntity<Integer> getNumber() {
        return ResponseEntity.ok(Stream
                .iterate(1, a -> a + 1)
                .limit(1000000)
                .parallel()
                .reduce(0, Integer::sum));
    }
}
