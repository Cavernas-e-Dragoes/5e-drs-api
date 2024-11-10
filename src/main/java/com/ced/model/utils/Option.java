package com.ced.model.utils;

import org.springframework.data.mongodb.core.mapping.Field;

public record Option(
        @Field("option_type")
        String optionType,
        Integer count,
        APIReference item,
        Choice choice,
        APIReference of
) {
}

