package com.ced.cache;

import com.ced.model.Identifiable;
import org.slf4j.Logger;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class GenericCache<T extends Identifiable> {
    private final MongoRepository<T, String> repository;
    private List<T> cache;
    private final Logger logger;
    private final String entityName;

    public GenericCache(MongoRepository<T, String> repository, Logger logger, String entityName) {
        this.repository = repository;
        this.logger = logger;
        this.entityName = entityName;
        this.cache = new ArrayList<>();
    }

    public void loadData() {
        logger.info("Carregando dados de {} em memória...", entityName);
        try {
            List<T> data = repository.findAll();
            if (!data.isEmpty()) {
                cache = new ArrayList<>(data);
                logger.info("Dados de {} carregados com sucesso: {} registros.", entityName, data.size());
            } else {
                logger.warn("Nenhum(a) {} encontrado(a) no repositório.", entityName);
            }
        } catch (Exception e) {
            logger.error("Falha ao carregar dados de {}: {}", entityName, e.getMessage(), e);
        }
    }

    public List<T> getAll() {
        return Collections.unmodifiableList(cache);
    }

    public Optional<T> getByIndex(String index) {
        if (index == null) {
            return Optional.empty();
        }

        return cache.stream()
                .filter(item -> item.index().equals(index))
                .findFirst();
    }
}
