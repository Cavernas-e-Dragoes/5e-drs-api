package com.ced.filter;

import com.ced.dto.criteria.SpellSearchCriteria;
import com.ced.model.Spell;

import java.util.List;
import java.util.function.Predicate;

public class SpellFilters {

    public static Predicate<Spell> byCriteria(SpellSearchCriteria criteria) {
        return spell -> matchesClassName(spell, criteria.className())
                && matchesLevel(spell, criteria.level())
                && matchesSchoolName(spell, criteria.schoolName());
    }

    private static boolean matchesClassName(Spell spell, String className) {
        return className == null || className.isEmpty()
                || spell.classes().stream().anyMatch(cls -> cls.name().equalsIgnoreCase(className));
    }

    private static boolean matchesLevel(Spell spell, List<Integer> levels) {
        return levels == null || levels.isEmpty() || levels.contains(spell.level());
    }

    private static boolean matchesSchoolName(Spell spell, String schoolName) {
        return schoolName == null || schoolName.isEmpty()
                || spell.school().name().equalsIgnoreCase(schoolName);
    }
}
