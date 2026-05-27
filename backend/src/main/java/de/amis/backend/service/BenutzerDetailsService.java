package de.amis.backend.service;

import de.amis.backend.model.Benutzer;
import de.amis.backend.model.ModulBerechtigung;
import de.amis.backend.repository.BenutzerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Lädt Benutzer aus der Datenbank für Spring Security.
 * Kein Cache → Berechtigungsänderungen wirken sofort ohne Neuanmeldung (AP-02).
 */
@Service
@RequiredArgsConstructor
public class BenutzerDetailsService implements UserDetailsService {

    private final BenutzerRepository benutzerRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String benutzername) throws UsernameNotFoundException {
        Benutzer benutzer = benutzerRepository.findByBenutzername(benutzername)
                .orElseThrow(() -> new UsernameNotFoundException("Benutzer nicht gefunden: " + benutzername));

        if (!benutzer.isAktiv()) {
            throw new UsernameNotFoundException("Benutzer ist deaktiviert: " + benutzername);
        }

        List<GrantedAuthority> authorities = buildAuthorities(benutzer);

        return User.builder()
                .username(benutzer.getBenutzername())
                .password(benutzer.getPasswortHash())
                .authorities(authorities)
                .build();
    }

    private List<GrantedAuthority> buildAuthorities(Benutzer benutzer) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        for (var rolle : benutzer.getRollen()) {
            // Spring-Rollenauthority (z.B. ROLE_ADMIN)
            authorities.add(new SimpleGrantedAuthority("ROLE_" + rolle.getName()));

            // Modul-Berechtigungen als Authorities (z.B. BEWOHNER:SCHREIBEN)
            for (ModulBerechtigung mb : rolle.getBerechtigungen()) {
                authorities.add(new SimpleGrantedAuthority(
                        mb.getModul().name() + ":" + mb.getBerechtigung().name()
                ));
            }
        }

        return authorities;
    }
}
