package de.amis.backend.controller;

import de.amis.backend.dto.BenutzerRequest;
import de.amis.backend.dto.BenutzerResponse;
import de.amis.backend.service.BenutzerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/benutzer")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMINISTRATION:ADMINISTRIEREN')")
public class BenutzerController {

    private final BenutzerService benutzerService;

    @GetMapping
    public List<BenutzerResponse> findAll() {
        return benutzerService.findAll();
    }

    @GetMapping("/{id}")
    public BenutzerResponse findById(@PathVariable Long id) {
        return benutzerService.findById(id);
    }

    @PostMapping
    public ResponseEntity<BenutzerResponse> create(@Valid @RequestBody BenutzerRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(benutzerService.create(req));
    }

    @PutMapping("/{id}")
    public BenutzerResponse update(@PathVariable Long id, @Valid @RequestBody BenutzerRequest req) {
        return benutzerService.update(id, req);
    }

    @PutMapping("/{id}/rollen")
    public BenutzerResponse setRollen(@PathVariable Long id, @RequestBody List<Long> rollenIds) {
        return benutzerService.setRollen(id, rollenIds);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        benutzerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
