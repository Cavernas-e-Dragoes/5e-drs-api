package com.ced.model;

import com.ced.model.utils.APIReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Document(collection = "spells")
public record Spell(

        @Id
        @JsonIgnore
        String id,
        String index,
        String name,
        List<String> desc,
        @Field("higher_level")
        List<String> higherLevel,
        String range,
        List<String> components,
        String material,
        boolean ritual,
        String duration,
        boolean concentration,
        @Field("casting_time")
        String castingTime,
        int level,
        @Field("attack_type")
        String attackType,
        Damage damage,
        @Field("area_of_effect")
        AreaOfEffect areaOfEffect,
        DC dc,
        APIReference school,
        List<APIReference> classes,
        List<APIReference> subclasses,
        String url
) {

    public record Damage(
            Object damage_type,
            @Field("damage_at_slot_level")
            Map<String, String> damageAtSlotLevel
    ) {
        @JsonIgnore
        public List<APIReference> getDamageTypeList() {
            if (damage_type instanceof List<?>) {
                return (List<APIReference>) damage_type;
            } else if (damage_type instanceof APIReference) {
                List<APIReference> list = new ArrayList<>();
                list.add((APIReference) damage_type);
                return list;
            }
            return new ArrayList<>();
        }

        @JsonSetter("damage_type")
        public Damage withDamageType(Object damage_type) {
            return new Damage(damage_type, this.damageAtSlotLevel);
        }
    }


    public record AreaOfEffect(
            String type,
            Double size,
            Double height,
            Double depth,
            Double width

    ) {
    }

    public record DC(
            @Field("dc_type")
            APIReference dcType,
            @Field("dc_success")
            String dcSuccess
    ) {
    }

}