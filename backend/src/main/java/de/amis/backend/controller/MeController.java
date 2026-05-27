package de.amis.backend.controller;

import de.amis.backend.dto.BenutzerResponse;
import de.amis.backend.repository.BenutzerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/me")
@RequiredArgsConstructor
public class MeController {

    private final BenutzerRepository benutzerRepository;

    @GetMapping
    public Map<String, Object> me(Authentication auth) {
        List<String> berechtigungen = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return benutzerRepository.findByBenutzername(auth.getName())
                .map(b -> Map.<String, Object>of(
                        "benutzername", b.getBenutzername(),
                        "vorname", b.getVorname() != null ? b.getVorname() : "",
                        "nachname", b.getNachname() != null ? b.getNachname() : "",
                        "afa", b.getAfa() != null ? b.getAfa().getKuerzel() : "",
                        "rollen", b.getRollen().stream().map(r -> r.getName()).collect(Collectors.toList()),
                        "berechtigungen", berechtigungen
                ))
                .orElse(Map.of("benutzername", auth.getName(), "berechtigungen", berechtigungen));
    }
}
