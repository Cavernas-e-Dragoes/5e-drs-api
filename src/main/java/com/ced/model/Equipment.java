package com.ced.model;

import com.ced.model.utils.APIReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document(collection = "equipamentos")
public record Equipment(
        @Id
        @JsonIgnore
        String id,
        String index,
        String name,
        @Field("equipment_category")
        APIReference equipmentCategory,
        @Field("gear_category")
        APIReference gearCategory,
        Cost cost,
        Double weight,
        List<String> desc,
        String url
) implements Identifiable {

    public record Cost(
            int quantity,
            String unit
    ) {
    }
}
