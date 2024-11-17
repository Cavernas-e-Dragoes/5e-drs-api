package com.ced.model.utils;

import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

public record From(
        @Field("option_set_type")
        String optionSetType,
        List<Option> options,
        @Field("equipment_category")
        APIReference equipmentCategory
) {
}
