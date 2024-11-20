package com.ced.controller;

import com.ced.dto.Response;
import com.ced.model.Equipment;
import com.ced.service.EquipmentService;
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
@RequestMapping("/api/equipamentos")
@Tag(name = "Equipamentos")
public class EquipmentController {

    private final EquipmentService equipmentService;

    public EquipmentController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    @Operation(summary = "Retorna todos os Equipamentos")
    @GetMapping()
    public ResponseEntity<Response> getAllEquips(Pageable pageable) {
        Page<Equipment> page = equipmentService.getAll(pageable);
        Response response = buildResponse(page, "/api/equipamentos/");
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Retorna detalhes de um equipamento")
    @Parameter(name = "equipamento", description = "Identificador único de equipamento", example = "clava", required = true)
    @GetMapping("/{equipamento}")
    public ResponseEntity<Equipment> getEquipsByIndex(@PathVariable String equipamento) {
        Optional<Equipment> equipment = equipmentService.getByIndex(equipamento);
        return equipment.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
