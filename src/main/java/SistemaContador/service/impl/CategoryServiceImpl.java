package SistemaContador.service.impl;

import SistemaContador.model.Category;
import SistemaContador.repository.CategoryRepository;
import SistemaContador.service.CategoryService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class CategoryServiceImpl
        implements CategoryService {

    private final CategoryRepository repository;

    @Override
    public List<Category> getCategoriesByUser(Integer userId) {

        return repository.findCategoriesByUser(userId);
    }

    @Override
    public void save(Category category) {

        repository.save(category);
    }

    @Override
    public void update(Category category) {

        repository.save(category);
    }

    @Override
    public void delete(Integer categoryId) {

        repository.deleteById(categoryId);
    }
}