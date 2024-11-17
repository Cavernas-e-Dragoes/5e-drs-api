package com.ced.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public record Response(
        int count,
        List<APIReferenceDTO> results,
        @JsonIgnore
        int totalPages,
        @JsonIgnore
        int currentPage

) {
}