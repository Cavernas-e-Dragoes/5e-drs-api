package com.ced.controller;

import com.ced.model.Spell;
import com.ced.service.CacheService;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Hidden
@RestController
@RequestMapping("/api/cache")
public class CacheController {

    private final CacheService cacheService;

    public CacheController(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @GetMapping("/spells")
    public ResponseEntity<List<Spell>> getCachedSpells(@RequestParam("password") String password) {
        List<Spell> cachedSpells = cacheService.getCachedSpells(password);
        if (cachedSpells == null || cachedSpells.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(cachedSpells);
    }

    @GetMapping("/refresh")
    public ResponseEntity<Void> refreshCache(@RequestParam("password") String password) {
        cacheService.refreshCache(password);
        return ResponseEntity.ok().build();
    }
}