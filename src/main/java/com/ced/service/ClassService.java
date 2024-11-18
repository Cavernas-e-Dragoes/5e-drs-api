package com.ced.service;

import com.ced.model.CharClass;
import com.ced.model.Race;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClassService extends BaseService<CharClass> {

    public ClassService(DataLoader dataLoader) {
        super(dataLoader);
    }

    @Override
    protected List<CharClass> getAllItems() {
        return dataLoader.getAllClasses();
    }

    @Override
    protected Optional<CharClass> getItemByIndex(String index) {
        return dataLoader.getClassByIndex(index);
    }
}