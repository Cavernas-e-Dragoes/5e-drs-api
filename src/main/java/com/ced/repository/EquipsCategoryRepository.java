package com.ced.repository;

import com.ced.model.EquipmentCategory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipsCategoryRepository extends MongoRepository<EquipmentCategory, String> {
}