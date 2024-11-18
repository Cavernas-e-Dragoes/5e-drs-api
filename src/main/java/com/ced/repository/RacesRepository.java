package com.ced.repository;

import com.ced.model.Race;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RacesRepository extends MongoRepository<Race, String> {
}