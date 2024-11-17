package com.ced.dto.criteria;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record SpellSearchCriteria(
        @Schema(example = "Bardo")
        String className,
        @Schema(example = "1")
        List<Integer> level,
        @Schema(example = "Encantamento")
        String schoolName
) {
}