package com.ced.controller;

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

import java.util.Optional;

import static com.ced.util.ResponseBuilder.buildResponse;

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
    public ResponseEntity<Response> getAll(Pageable pageable) {
        Page<Spell> page = spellService.getAll(pageable);
        Response response = buildResponse(page, "/api/magias/");
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Retorna magia específica", description = "Obtém detalhes da magia!")
    @Parameter(name = "magia", description = "Identificador único da magia", example = "bola-de-fogo", required = true)
    @GetMapping("/{magia}")
    public ResponseEntity<Spell> getByIndex(@PathVariable String magia) {
        Optional<Spell> spell = spellService.getByIndex(magia);
        return spell.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Pesquisa magias", description = "Pesquisa magias com base em critérios de classe, nível e escola de magia.")
    @GetMapping("/search")
    public ResponseEntity<Response> search(
            @Valid @ModelAttribute SpellSearchCriteria criteria,
            Pageable pageable) {
        Page<Spell> spellPage = spellService.searchSpells(criteria, pageable);
        Response response = buildResponse(spellPage, "/api/magias/");
        return ResponseEntity.ok(response);
    }

}