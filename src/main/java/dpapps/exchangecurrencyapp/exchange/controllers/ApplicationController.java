package dpapps.exchangecurrencyapp.exchange.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApplicationController {

    @GetMapping("/")
    public String home() {
        return "Welcome to Exchange currency app developed with Java 17, Maven 3.9, Spring 3.1.0 and Render || and testing webhook push to automate deploy";
    }

    @GetMapping("/health")
    public ResponseEntity<Void> helthCheack() {
        return ResponseEntity.ok().build();
    }
}
