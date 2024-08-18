package com.majlo.antares.service;

import com.majlo.antares.model.events.EventCategory;
import com.majlo.antares.repository.events.EventCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class EventCategoryService {
    private final EventCategoryRepository categoryRepository;

    @Autowired
    public EventCategoryService(EventCategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<EventCategory> getAllCategories() {
        return categoryRepository.findAll();
    }

    public EventCategory getCategoryById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public EventCategory createCategory(EventCategory category) {
        return categoryRepository.save(category);
    }
}
