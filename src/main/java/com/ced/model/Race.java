package com.ced.model;

import com.ced.model.utils.APIReference;
import com.ced.model.utils.Options;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document(collection = "races")
public record Race(
        @Id
        @JsonIgnore
        String id,
        String index,
        String name,
        Double speed,
        @Field("ability_bonuses")
        List<AbilityBonus> abilityBonuses,
        String alignment,
        String age,
        String size,
        @Field("size_description")
        String sizeDescription,
        @Field("starting_proficiencies")
        List<APIReference> startingProficiencies,
        @Field("starting_proficiency_options")
        ProficiencyOptions startingProficiencyOptions,
        List<APIReference> languages,
        @Field("language_desc")
        String languageDesc,
        List<APIReference> traits,
        List<APIReference> subRaces,
        String url
) {
    public record AbilityBonus(
            @Field("ability_score")
            APIReference abilityScore,
            Integer bonus
    ) {
    }

    public record ProficiencyOptions(
            String desc,
            Integer choose,
            String type,
            Options from
    ) {
    }
}
