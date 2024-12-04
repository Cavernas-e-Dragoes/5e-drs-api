package com.ced.controller;

import com.ced.dto.Response;
import com.ced.model.CharClass;
import com.ced.service.ClassService;
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
@RequestMapping("/api/classes")
@Tag(name = "Classes")
public class CharClassController {

    private final ClassService classService;

    public CharClassController(ClassService classService) {
        this.classService = classService;
    }

    @Operation(summary = "Retorna todas as classes")
    @GetMapping()
    public ResponseEntity<Response> getAll(Pageable pageable) {
        Page<CharClass> page = classService.getAll(pageable);
        Response response = buildResponse(page, "/api/classes/");
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Retorna detalhes de uma classe")
    @Parameter(name = "classe", description = "Identificador único de classe", example = "barbaro", required = true)
    @GetMapping("/{classe}")
    public ResponseEntity<CharClass> getByIndex(@PathVariable String classe) {
        Optional<CharClass> charClass = classService.getByIndex(classe);
        return charClass.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}