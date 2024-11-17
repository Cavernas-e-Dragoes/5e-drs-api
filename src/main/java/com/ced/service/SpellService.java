package com.ced.service;

import com.ced.dto.criteria.SpellSearchCriteria;
import com.ced.filter.SpellFilters;
import com.ced.model.Spell;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SpellService {

    private final SpellDataLoader spellDataLoader;

    public SpellService(SpellDataLoader spellDataLoader) {
        this.spellDataLoader = spellDataLoader;
    }

    public Page<Spell> getAllSpells(Pageable pageable) {
        List<Spell> spells = spellDataLoader.getAllSpells();

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), spells.size());
        if (start > end) {
            return Page.empty(pageable);
        }
        List<Spell> pageContent = spells.subList(start, end);

        return new PageImpl<>(pageContent, pageable, spells.size());
    }

    public Optional<Spell> getSpellByIndex(final String index) {
        return spellDataLoader.getSpellByIndex(index);
    }

    public Page<Spell> searchSpells(SpellSearchCriteria criteria, Pageable pageable) {
        List<Spell> spells = spellDataLoader.getAllSpells();

        List<Spell> filteredSpells = spells.stream()
                .filter(SpellFilters.byCriteria(criteria))
                .toList();

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), filteredSpells.size());
        if (start > end) {
            return Page.empty(pageable);
        }
        List<Spell> pageContent = filteredSpells.subList(start, end);

        return new PageImpl<>(pageContent, pageable, filteredSpells.size());
    }

}
