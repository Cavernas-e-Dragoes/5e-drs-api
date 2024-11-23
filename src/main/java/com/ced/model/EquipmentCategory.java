package com.ced.model;

import com.ced.model.utils.APIReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "categoriasdeequipamento")
public record EquipmentCategory(
        @Id
        @JsonIgnore
        String id,
        String index,
        String name,
        List<APIReference> equipment,
        String url
) implements Identifiable {
}
