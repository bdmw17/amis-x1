package de.amis.backend.controller;

import de.amis.backend.dto.AfADto;
import de.amis.backend.repository.AfARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/afa")
@RequiredArgsConstructor
public class AfAController {

    private final AfARepository afaRepository;

    @GetMapping
    public List<AfADto> findAll() {
        return afaRepository.findByAktivTrue().stream()
                .map(AfADto::from)
                .collect(Collectors.toList());
    }
}
