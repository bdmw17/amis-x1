package de.amis.backend.service;

import de.amis.backend.dto.ModulBerechtigungDto;
import de.amis.backend.dto.RolleDto;
import de.amis.backend.exception.ResourceNotFoundException;
import de.amis.backend.model.ModulBerechtigung;
import de.amis.backend.model.Rolle;
import de.amis.backend.repository.RolleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RolleService {

    private final RolleRepository rolleRepository;

    @Transactional(readOnly = true)
    public List<RolleDto> findAll() {
        return rolleRepository.findAll().stream()
                .map(RolleDto::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RolleDto findById(Long id) {
        return RolleDto.from(loadRolle(id));
    }

    public RolleDto create(RolleDto dto) {
        if (rolleRepository.existsByName(dto.getName())) {
            throw new IllegalArgumentException("Rollenname bereits vergeben: " + dto.getName());
        }
        Rolle rolle = new Rolle();
        applyDto(rolle, dto);
        return RolleDto.from(rolleRepository.save(rolle));
    }

    public RolleDto update(Long id, RolleDto dto) {
        Rolle rolle = loadRolle(id);
        applyDto(rolle, dto);
        return RolleDto.from(rolleRepository.save(rolle));
    }

    public void delete(Long id) {
        rolleRepository.delete(loadRolle(id));
    }

    // ── Hilfsmethoden ─────────────────────────────────────────────────────────

    private void applyDto(Rolle rolle, RolleDto dto) {
        rolle.setName(dto.getName());
        rolle.setBeschreibung(dto.getBeschreibung());

        rolle.getBerechtigungen().clear();
        if (dto.getBerechtigungen() != null) {
            for (ModulBerechtigungDto mbDto : dto.getBerechtigungen()) {
                rolle.getBerechtigungen().add(
                        new ModulBerechtigung(rolle, mbDto.getModul(), mbDto.getBerechtigung())
                );
            }
        }
    }

    private Rolle loadRolle(Long id) {
        return rolleRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.of("Rolle", id));
    }
}
