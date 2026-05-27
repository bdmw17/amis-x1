package de.amis.backend.controller;

import de.amis.backend.dto.RolleDto;
import de.amis.backend.service.RolleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/rollen")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMINISTRATION:ADMINISTRIEREN')")
public class RolleController {

    private final RolleService rolleService;

    @GetMapping
    public List<RolleDto> findAll() {
        return rolleService.findAll();
    }

    @GetMapping("/{id}")
    public RolleDto findById(@PathVariable Long id) {
        return rolleService.findById(id);
    }

    @PostMapping
    public ResponseEntity<RolleDto> create(@Valid @RequestBody RolleDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(rolleService.create(dto));
    }

    @PutMapping("/{id}")
    public RolleDto update(@PathVariable Long id, @Valid @RequestBody RolleDto dto) {
        return rolleService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        rolleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
