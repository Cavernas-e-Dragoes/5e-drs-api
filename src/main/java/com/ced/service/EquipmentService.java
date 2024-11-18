package com.ced.service;

import com.ced.model.Equipment;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EquipmentService extends BaseService<Equipment> {

    public EquipmentService(DataLoader dataLoader) {
        super(dataLoader);
    }

    @Override
    protected List<Equipment> getAllItems() {
        return dataLoader.getAllEquipments();
    }

    @Override
    protected Optional<Equipment> getItemByIndex(String index) {
        return dataLoader.getEquipmentsByIndex(index);
    }

}

