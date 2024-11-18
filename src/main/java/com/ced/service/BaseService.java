package com.ced.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public abstract class BaseService<T> {
    protected final DataLoader dataLoader;

    public BaseService(DataLoader dataLoader) {
        this.dataLoader = dataLoader;
    }

    protected abstract List<T> getAllItems();

    protected abstract Optional<T> getItemByIndex(String index);

    public Page<T> getAll(Pageable pageable) {
        List<T> items = getAllItems();
        return getPage(pageable, items);
    }

    public Optional<T> getByIndex(String index) {
        return getItemByIndex(index);
    }

    protected Page<T> getPage(Pageable pageable, List<T> items) {
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), items.size());
        if (start > end) {
            return Page.empty(pageable);
        }
        List<T> pageContent = items.subList(start, end);
        return new PageImpl<>(pageContent, pageable, items.size());
    }
}
