package com.ced.dto;

import java.util.List;

public record SimpleResponse(
        int count,
        List<APIReferenceDTO> results
) {
}
