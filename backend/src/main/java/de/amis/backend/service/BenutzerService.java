package de.amis.backend.service;

import de.amis.backend.dto.BenutzerRequest;
import de.amis.backend.dto.BenutzerResponse;
import de.amis.backend.exception.ResourceNotFoundException;
import de.amis.backend.model.AfA;
import de.amis.backend.model.Benutzer;
import de.amis.backend.model.Rolle;
import de.amis.backend.repository.AfARepository;
import de.amis.backend.repository.BenutzerRepository;
import de.amis.backend.repository.RolleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BenutzerService {

    private final BenutzerRepository benutzerRepository;
    private final RolleRepository rolleRepository;
    private final AfARepository afaRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<BenutzerResponse> findAll() {
        return benutzerRepository.findAll().stream()
                .map(BenutzerResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BenutzerResponse findById(Long id) {
        return BenutzerResponse.from(loadBenutzer(id));
    }

    public BenutzerResponse create(BenutzerRequest req) {
        if (benutzerRepository.existsByBenutzername(req.getBenutzername())) {
            throw new IllegalArgumentException("Benutzername bereits vergeben: " + req.getBenutzername());
        }
        Benutzer b = new Benutzer();
        applyRequest(b, req, true);
        return BenutzerResponse.from(benutzerRepository.save(b));
    }

    public BenutzerResponse update(Long id, BenutzerRequest req) {
        Benutzer b = loadBenutzer(id);
        applyRequest(b, req, req.getPasswort() != null && !req.getPasswort().isBlank());
        return BenutzerResponse.from(benutzerRepository.save(b));
    }

    public void delete(Long id) {
        benutzerRepository.delete(loadBenutzer(id));
    }

    // ── Rollen-Zuweisung ──────────────────────────────────────────────────────

    public BenutzerResponse setRollen(Long id, List<Long> rollenIds) {
        Benutzer b = loadBenutzer(id);
        Set<Rolle> rollen = new HashSet<>(rolleRepository.findAllById(rollenIds));
        b.setRollen(rollen);
        return BenutzerResponse.from(benutzerRepository.save(b));
    }

    // ── Hilfsmethoden ─────────────────────────────────────────────────────────

    private void applyRequest(Benutzer b, BenutzerRequest req, boolean encodePasswort) {
        b.setBenutzername(req.getBenutzername());
        b.setVorname(req.getVorname());
        b.setNachname(req.getNachname());
        b.setAktiv(req.isAktiv());

        if (encodePasswort && req.getPasswort() != null) {
            b.setPasswortHash(passwordEncoder.encode(req.getPasswort()));
        }

        if (req.getAfaId() != null) {
            AfA afa = afaRepository.findById(req.getAfaId())
                    .orElseThrow(() -> ResourceNotFoundException.of("AfA", req.getAfaId()));
            b.setAfa(afa);
        } else {
            b.setAfa(null);
        }

        if (req.getRollenIds() != null) {
            Set<Rolle> rollen = new HashSet<>(rolleRepository.findAllById(req.getRollenIds()));
            b.setRollen(rollen);
        }
    }

    private Benutzer loadBenutzer(Long id) {
        return benutzerRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.of("Benutzer", id));
    }
}
