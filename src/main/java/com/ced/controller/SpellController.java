package com.ced.controller;

import com.ced.dto.APIReferenceDTO;
import com.ced.dto.Response;
import com.ced.dto.criteria.SpellSearchCriteria;
import com.ced.model.Spell;
import com.ced.service.SpellService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/magias")
@Tag(name = "Magias")
public class SpellController {

    private final SpellService spellService;

    public SpellController(SpellService spellService) {
        this.spellService = spellService;
    }

    @Operation(summary = "Retorna todas as magias", description = "Um grimório.")
    @GetMapping()
    public ResponseEntity<Response> getAllSpells(Pageable pageable) {
        Page<Spell> spellsPage = spellService.getAllSpells(pageable);
        Response response = buildResponse(spellsPage);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Retorna magia específica", description = "Obtém detalhes da magia!")
    @Parameter(name = "magia", description = "Identificador único da magia", example = "bola-de-fogo", required = true)
    @GetMapping("/{magia}")
    public ResponseEntity<Spell> getSpellByIndex(@PathVariable String magia) {
        Optional<Spell> spell = spellService.getSpellByIndex(magia);
        return spell.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Pesquisa magias", description = "Pesquisa magias com base em critérios de classe, nível e escola de magia.")
    @GetMapping("/search")
    public ResponseEntity<Response> searchSpells(
            @Valid @ModelAttribute SpellSearchCriteria criteria,
            Pageable pageable) {
        Page<Spell> spellPage = spellService.searchSpells(criteria, pageable);
        Response response = buildResponse(spellPage);
        return ResponseEntity.ok(response);
    }

    private Response buildResponse(Page<Spell> spellPage) {
        List<APIReferenceDTO> summaries = spellPage.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return new Response(
                (int) spellPage.getTotalElements(),
                summaries,
                spellPage.getTotalPages(),
                spellPage.getNumber()
        );
    }

    private APIReferenceDTO convertToDTO(Spell spell) {
        return new APIReferenceDTO(
                spell.index(),
                spell.name(),
                "/api/magias/" + spell.index()
        );
    }

}
