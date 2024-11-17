package com.ced.service;

import com.ced.model.Spell;
import com.ced.repository.SpellsRepository;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class SpellDataLoader {

    private static final Logger LOGGER = Logger.getLogger(SpellDataLoader.class.getName());

    private final SpellsRepository spellsRepository;
    private List<Spell> spellsCache;

    public SpellDataLoader(SpellsRepository spellsRepository) {
        this.spellsRepository = spellsRepository;
        this.spellsCache = new ArrayList<>();
    }

    @EventListener(ContextRefreshedEvent.class)
    public void onApplicationEvent() {
        initializeData();
    }

    private void initializeData() {
        LOGGER.info("Carregando dados das magias em memória...");
        try {
            List<Spell> spells = spellsRepository.findAll();
            if (!spells.isEmpty()) {
                spellsCache = new ArrayList<>(spells);
                LOGGER.info("Dados carregados com sucesso: " + spells.size() + " magias.");
            } else {
                LOGGER.warning("Nenhuma magia encontrada no repositório.");
            }
        } catch (Exception e) {
            LOGGER.severe("Falha ao carregar dados: " + e.getMessage());
        }
    }

    public List<Spell> getAllSpells() {
        return Collections.unmodifiableList(spellsCache);
    }

    public Optional<Spell> getSpellByIndex(String index) {
        return spellsCache.stream()
                .filter(spell -> spell.index().equals(index))
                .findFirst();
    }

    public void refreshData() {
        initializeData();
    }
}

