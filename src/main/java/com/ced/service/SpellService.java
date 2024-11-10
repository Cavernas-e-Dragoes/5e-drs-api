package com.ced.service;

import com.ced.dto.criteria.SpellSearchCriteria;
import com.ced.model.Spell;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SpellService {

    private final CacheService cacheService;
    @Value("${security.cache.password}")
    private String cachePassword;

    public SpellService(final CacheService cacheService) {
        this.cacheService = cacheService;
    }

    public List<Spell> getCachedSpells() {
        List<Spell> cachedSpells = cacheService.getCachedSpells(cachePassword);
        if (cachedSpells == null) {
            cacheService.refreshCache(cachePassword);
            cachedSpells = cacheService.getCachedSpells(cachePassword);
        }
        return cachedSpells;
    }

    public Page<Spell> getAllSpells(Pageable pageable) {
        List<Spell> cachedSpells = getCachedSpells();

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), cachedSpells.size());
        List<Spell> pageContent = cachedSpells.subList(start, end);

        return new PageImpl<>(pageContent, pageable, cachedSpells.size());
    }

    public Optional<Spell> getSpellByIndex(final String index) {
        return getCachedSpells().stream()
                .filter(spell -> spell.index().equals(index))
                .findFirst();
    }

    public Page<Spell> searchSpells(SpellSearchCriteria criteria, Pageable pageable) {
        List<Spell> cachedSpells = getCachedSpells();

        List<Spell> filteredSpells = cachedSpells.stream()
                .filter(spell -> criteria.className() == null || criteria.className().isEmpty() ||
                        spell.classes().stream().anyMatch(cls -> cls.name().equalsIgnoreCase(criteria.className())))
                .filter(spell -> criteria.level() == null || criteria.level().isEmpty() ||
                        criteria.level().contains(spell.level()))
                .filter(spell -> criteria.schoolName() == null || criteria.schoolName().isEmpty() ||
                        spell.school().name().equalsIgnoreCase(criteria.schoolName()))
                .collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), filteredSpells.size());
        List<Spell> pageContent = filteredSpells.subList(start, end);

        return new PageImpl<>(pageContent, pageable, filteredSpells.size());
    }
}
