package com.ced.service;

import com.ced.model.EquipmentCategory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EquipmentCategoryService extends BaseService<EquipmentCategory> {

    public EquipmentCategoryService(DataLoader dataLoader) {
        super(dataLoader);
    }

    @Override
    protected List<EquipmentCategory> getAllItems() {
        return dataLoader.getAllEquipmentsCategory();
    }

    @Override
    protected Optional<EquipmentCategory> getItemByIndex(String index) {
        return dataLoader.getEquipmentsCategoryByIndex(index);
    }

}

