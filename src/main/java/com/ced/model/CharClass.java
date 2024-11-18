package com.ced.model;

import com.ced.model.utils.APIReference;
import com.ced.model.utils.Choice;
import com.ced.model.utils.EquipmentQuantity;
import com.ced.model.utils.From;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document(collection = "classes")
public record CharClass(
        @Id
        @JsonIgnore
        String id,
        String index,
        String name,
        @Field("hit_die")
        Integer hitDie,
        @Field("proficiency_choices")
        List<ProficiencyChoice> proficiencyChoices,
        List<APIReference> proficiencies,
        @Field("saving_throws")
        List<APIReference> savingThrows,
        @Field("starting_equipment")
        List<EquipmentQuantity> startingEquipment,
        @Field("starting_equipment_options")
        List<Choice> startingEquipmentOptions,
        @Field("class_levels")
        String classLevels,
        @Field("multi_classing")
        MultiClassing multiClassing,
        List<APIReference> subclasses,
        SpellCasting spellCasting,
        String url
) implements Identifiable {

    public record SpellCasting(
            Integer level,
            @JsonProperty("spellcasting_ability")
            APIReference spellCastingAbility,
            List<SpellCastingInfo> info
    ) {

        public record SpellCastingInfo(
                String name,
                List<String> desc
        ) {
        }
    }

    public record MultiClassing(
            List<Prerequisite> prerequisites,
            @Field("proficiency_choices")
            List<ProficiencyChoice> proficiencyChoices,
            List<APIReference> proficiencies
    ) {
        public record Prerequisite(
                @Field("ability_score")
                APIReference abilityScore,
                @Field("minimum_score")
                Integer minimumScore
        ) {
        }
    }

    public record ProficiencyChoice(
            String desc,
            Integer choose,
            String type,
            From from
    ) {
    }
}