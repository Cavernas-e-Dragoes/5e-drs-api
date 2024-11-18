package com.ced.controller;

import com.ced.dto.Response;
import com.ced.model.Race;
import com.ced.service.RaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static com.ced.util.ResponseBuilder.buildResponse;

@RestController
@RequestMapping("/api/racas")
@Tag(name = "Raças")
public class RaceController {

    private final RaceService raceService;

    public RaceController(RaceService raceService) {
        this.raceService = raceService;
    }

    @Operation(summary = "Retorna todas as Raças")
    @GetMapping()
    public ResponseEntity<Response> getAll(Pageable pageable) {
        Page<Race> page = raceService.getAll(pageable);
        Response response = buildResponse(page, "/api/racas/");
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Retorna detalhes de uma raça")
    @Parameter(name = "raca", description = "Identificador único de raça", example = "elfo", required = true)
    @GetMapping("/{raca}")
    public ResponseEntity<Race> getByIndex(@PathVariable String raca) {
        Optional<Race> race = raceService.getByIndex(raca);
        return race.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
