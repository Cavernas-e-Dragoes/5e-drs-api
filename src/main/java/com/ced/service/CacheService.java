package com.ced.service;

import com.ced.model.Spell;
import com.ced.repository.SpellsRepository;
import com.ced.security.SecurityConfig;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class CacheService {

    private final SpellsRepository spellsRepository;
    private final Cache<String, List<Spell>> spellsCache;
    private final SecurityConfig securityConfig;

    public CacheService(final SpellsRepository spellsRepository, final SecurityConfig securityConfig) {
        this.spellsRepository = spellsRepository;
        this.securityConfig = securityConfig;
        this.spellsCache = Caffeine.newBuilder()
                .maximumSize(1)
                .expireAfterWrite(10, TimeUnit.DAYS)
                .build();
    }

    @EventListener(ContextRefreshedEvent.class)
    public void onApplicationEvent() {
        initializeCache();
    }

    private void initializeCache() {
        spellsCache.invalidateAll();
        spellsCache.put("allSpells", spellsRepository.findAll());
    }

    public void refreshCache(String password) {
        validatePassword(password);
        spellsCache.invalidateAll();
        spellsCache.put("allSpells", spellsRepository.findAll());
    }

    public List<Spell> getCachedSpells(String password) {
        validatePassword(password);
        return spellsCache.getIfPresent("allSpells");
    }

    private void validatePassword(String password) {
        if (!securityConfig.getCachePassword().equals(password)) {
            throw new SecurityException("Access Denied: Invalid password.");
        }
    }
}
