package com.ced.service;

import com.ced.model.Race;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RaceService extends BaseService<Race> {

    public RaceService(DataLoader dataLoader) {
        super(dataLoader);
    }

    @Override
    protected List<Race> getAllItems() {
        return dataLoader.getAllRaces();
    }

    @Override
    protected Optional<Race> getItemByIndex(String index) {
        return dataLoader.getRaceByIndex(index);
    }
}