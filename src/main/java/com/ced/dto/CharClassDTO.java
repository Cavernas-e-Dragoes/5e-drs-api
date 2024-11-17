package com.ced.dto;

import java.util.List;

public record CharClassDTO(
        String index,
        String name,
        Integer hitDie,
        List<String> proficiencyChoices,
        List<String> proficiencies,
        List<String> savingThrows,
        List<String> startingEquipment,
        List<String> startingEquipmentOptions,
        String classLevels,
        String multiClassing,
        List<String> subclasses,
        String spellCasting,
        String url
) {
}
