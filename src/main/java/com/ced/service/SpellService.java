package com.ced.service;

import com.ced.dto.criteria.SpellSearchCriteria;
import com.ced.filter.SpellFilters;
import com.ced.model.Spell;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SpellService extends BaseService<Spell> {

    public SpellService(DataLoader dataLoader) {
        super(dataLoader);
    }

    @Override
    protected List<Spell> getAllItems() {
        return dataLoader.getAllSpells();
    }

    @Override
    protected Optional<Spell> getItemByIndex(String index) {
        return dataLoader.getSpellByIndex(index);
    }

    public Page<Spell> searchSpells(SpellSearchCriteria criteria, Pageable pageable) {
        List<Spell> spells = dataLoader.getAllSpells();

        List<Spell> filteredSpells = spells.stream()
                .filter(SpellFilters.byCriteria(criteria))
                .toList();

        return getPage(pageable, filteredSpells);
    }
}

