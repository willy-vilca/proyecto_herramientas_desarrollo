package SistemaContador.repository;

import SistemaContador.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository
        extends JpaRepository<Category,Integer> {

    @Query(value = """
    SELECT *
    FROM categories
    WHERE user_id = :userId
    OR user_id IS NULL
    ORDER BY name
    """,
            nativeQuery = true)
    List<Category> findCategoriesByUser(
            @Param("userId") Integer userId
    );
}
