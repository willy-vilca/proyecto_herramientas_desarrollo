package SistemaContador.service;

import SistemaContador.model.Category;

import java.util.List;

public interface CategoryService {

    List<Category> getCategoriesByUser(Integer userId);

    void save(Category category);

    void update(Category category);

    void delete(Integer categoryId);
}
