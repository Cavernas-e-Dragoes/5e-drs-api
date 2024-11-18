package com.ced.repository;

import com.ced.model.Equipment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipsRepository extends MongoRepository<Equipment, String> {
}