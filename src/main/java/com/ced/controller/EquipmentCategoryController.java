package com.ced.controller;

import com.ced.dto.Response;
import com.ced.model.Equipment;
import com.ced.model.EquipmentCategory;
import com.ced.service.EquipmentCategoryService;
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
@RequestMapping("/api/categorias-de-equipamento")
@Tag(name = "Categorias de Equipamentos")
public class EquipmentCategoryController {

    private final EquipmentCategoryService equipmentCategoryService;

    public EquipmentCategoryController(EquipmentCategoryService equipmentCategoryService) {
        this.equipmentCategoryService = equipmentCategoryService;
    }

    @Operation(summary = "Retorna todas as Categorias de Equipamentos")
    @GetMapping()
    public ResponseEntity<Response> getAllCategories(Pageable pageable) {
        Page<EquipmentCategory> page = equipmentCategoryService.getAll(pageable);
        Response response = buildResponse(page, "/api/categorias-de-equipamento/");
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Retorna detalhes de uma categoria de equipamento")
    @Parameter(name = "categoriaDeEquipamento", description = "Identificador único de uma categoria de equipamento", example = "arma", required = true)
    @GetMapping("/{categoriaDeEquipamento}")
    public ResponseEntity<EquipmentCategory> getCategoryByIndex(@PathVariable String categoriaDeEquipamento) {
        Optional<EquipmentCategory> equipmentCategory = equipmentCategoryService.getByIndex(categoriaDeEquipamento);
        return equipmentCategory.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
