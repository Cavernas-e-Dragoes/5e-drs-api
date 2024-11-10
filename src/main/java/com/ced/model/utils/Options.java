package com.ced.model.utils;

import java.util.List;

public record Options(
        String optionSetType,
        List<Option> options
) {}

