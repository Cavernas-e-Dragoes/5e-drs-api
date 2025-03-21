package com.ced.repository;

import com.ced.model.CharClass;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassesRepository extends MongoRepository<CharClass, String> {
}
