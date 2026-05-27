package de.amis.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Liefert Metainformationen über die API (Version, Kompatibilität).
 * Erlaubt dem Frontend, die Backend-Version zu prüfen.
 */
@RestController
@RequestMapping("/api/v1")
public class ApiInfoController {

    private static final String API_VERSION = "1.0.0";

    @GetMapping
    public ResponseEntity<Map<String, String>> apiInfo() {
        return ResponseEntity.ok(Map.of(
                "application", "AMIS – Asylverwaltungsprogramm",
                "apiVersion", API_VERSION,
                "status", "UP"
        ));
    }
}
