package com.ced.repository;

import com.ced.model.Spell;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpellsRepository extends MongoRepository<Spell, String> {
}
