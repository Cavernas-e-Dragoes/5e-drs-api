package com.ced.util;

import com.ced.dto.APIReferenceDTO;
import com.ced.dto.Response;
import com.ced.model.Identifiable;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class ResponseBuilder {

    public static <T extends Identifiable> Response buildResponse(Page<T> page, String baseUrl) {
        List<APIReferenceDTO> summaries = page.getContent().stream()
                .map(entity -> convertToDTO(entity, baseUrl + entity.index()))
                .collect(Collectors.toList());

        return new Response(
                (int) page.getTotalElements(),
                summaries,
                page.getTotalPages(),
                page.getNumber()
        );
    }

    private static <T extends Identifiable> APIReferenceDTO convertToDTO(T entity, String baseUrl) {
        return new APIReferenceDTO(
                entity.index(),
                entity.name(),
                baseUrl
        );
    }
}
