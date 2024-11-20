package com.ced.service;

import com.ced.cache.GenericCache;
import com.ced.model.CharClass;
import com.ced.model.Equipment;
import com.ced.model.EquipmentCategory;
import com.ced.model.Race;
import com.ced.model.Spell;
import com.ced.repository.ClassesRepository;
import com.ced.repository.EquipsCategoryRepository;
import com.ced.repository.EquipsRepository;
import com.ced.repository.RacesRepository;
import com.ced.repository.SpellsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DataLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataLoader.class);

    private final GenericCache<Spell> spellCache;
    private final GenericCache<Race> raceCache;
    private final GenericCache<CharClass> classCache;
    private final GenericCache<Equipment> equipmentCache;
    private final GenericCache<EquipmentCategory> equipmentCategoryCache;

    public DataLoader(SpellsRepository spellsRepository,
                      RacesRepository racesRepository,
                      ClassesRepository classesRepository,
                      EquipsRepository equipsRepository,
                      EquipsCategoryRepository equipsCategoryRepository) {

        this.spellCache = new GenericCache<>(spellsRepository, LOGGER, "magia");
        this.raceCache = new GenericCache<>(racesRepository, LOGGER, "raça");
        this.classCache = new GenericCache<>(classesRepository, LOGGER, "classe");
        this.equipmentCache = new GenericCache<>(equipsRepository, LOGGER, "equipamento");
        this.equipmentCategoryCache = new GenericCache<>(equipsCategoryRepository, LOGGER, "categoriaDeEquipamento");
    }

    @EventListener(ContextRefreshedEvent.class)
    public void onApplicationEvent() {
        initializeData();
    }

    private void initializeData() {
        spellCache.loadData();
        raceCache.loadData();
        classCache.loadData();
        equipmentCache.loadData();
        equipmentCategoryCache.loadData();
    }

    public List<Spell> getAllSpells() {
        return spellCache.getAll();
    }

    public Optional<Spell> getSpellByIndex(String index) {
        return spellCache.getByIndex(index);
    }

    public List<Race> getAllRaces() {
        return raceCache.getAll();
    }

    public Optional<Race> getRaceByIndex(String index) {
        return raceCache.getByIndex(index);
    }

    public List<CharClass> getAllClasses() {
        return classCache.getAll();
    }

    public Optional<CharClass> getClassByIndex(String index) {
        return classCache.getByIndex(index);
    }

    public List<Equipment> getAllEquipments() {
        return equipmentCache.getAll();
    }

    public Optional<Equipment> getEquipmentsByIndex(String index) {
        return equipmentCache.getByIndex(index);
    }

    public List<EquipmentCategory> getAllEquipmentsCategory() {
        return equipmentCategoryCache.getAll();
    }

    public Optional<EquipmentCategory> getEquipmentsCategoryByIndex(String index) {
        return equipmentCategoryCache.getByIndex(index);
    }

    @Scheduled(cron = "0 0 * * * ?")
    public void refreshData() {
        initializeData();
        LOGGER.info("Dados atualizados com sucesso.");
    }
}